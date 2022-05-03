package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.exception.BatchStockWithdrawException;
import br.com.group9.pimlwarehouse.exception.InboundOrderValidationException;
import br.com.group9.pimlwarehouse.repository.BatchStockRepository;
import br.com.group9.pimlwarehouse.util.batch_stock_order.OrderByDueDate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import br.com.group9.pimlwarehouse.entity.Section;
import br.com.group9.pimlwarehouse.enums.CategoryENUM;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BatchStockService {
    private BatchStockRepository batchStockRepository;
    private SectionService sectionService;


    public BatchStockService(
            BatchStockRepository batchStockRepository,
            SectionService sectionService
    ) {
        this.batchStockRepository = batchStockRepository;
        this.sectionService = sectionService;
    }

    public List<BatchStock> save(List<BatchStock> batchStocks) {
        return batchStocks.stream().map(
                e -> batchStockRepository.save(e)).collect(Collectors.toList()
        );
    }
  
    public List<BatchStock> findByProductId(Long productId){
        return batchStockRepository.findByProductId(productId);
    }

    public List<BatchStock> findByProductIdWithValidShelfLife(Long productId){
        LocalDate maxDueDate = LocalDate.now().plusDays(21);
        return batchStockRepository.findByProductIdAndDueDateIsAfter(productId, maxDueDate);
    }
  
    private void updateBatchStock(BatchStock newBatchStock, BatchStock oldBatchStock){
        oldBatchStock.setCurrentQuantity(newBatchStock.getCurrentQuantity());
        batchStockRepository.save(oldBatchStock);
    }

    private List<BatchStock> updateBatchStocks(List<BatchStock> batchStocks, List<BatchStock> newBatchStocks){
        batchStocks.forEach(batchStock ->
                updateBatchStock(newBatchStocks.stream()
                    .filter(nb -> batchStock.getBatchNumber().equals(nb.getBatchNumber()))
                    .findAny().orElseThrow(() -> new InboundOrderValidationException("BATCH_STOCK_NOT_FOUND")), batchStock)

        );
        return batchStocks;
    }

    public List<BatchStock> update(List<BatchStock> batchStocks, InboundOrder order) {

        if(order == null) {
            throw new InboundOrderValidationException("INBOUND_ORDER_NOT_FOUND");
        }
        if (order.getBatchStocks().size() != batchStocks.size()){
            throw new InboundOrderValidationException("BATCH_STOCK_MISSING");
        }
        return updateBatchStocks(order.getBatchStocks(), batchStocks);
    }

    public List<BatchStock> getAllBatchesByDueDate(Long sectionId, Long days, CategoryENUM category) {
        if (sectionId == null){
            return getAllBatchesByDueDateAndCategory(days, category);
        }

        Section section = sectionService.findById(sectionId);
        return getAllBatchesByDueDateAndSection(section,days);
    }

    private List<BatchStock> getAllBatchesByDueDateAndSection(Section section, Long days) {
        LocalDate today = LocalDate.now();
        LocalDate upperDate = today.plusDays(days);
        return section.getInboundOrders().stream().map(
                i -> batchStockRepository.findByDueDateBetweenAndInboundOrder(today,upperDate, i)
        ).flatMap(List::stream).sorted(Comparator.comparing(BatchStock::getDueDate)).collect(Collectors.toList());
    }

    private List<BatchStock> getAllBatchesByDueDateAndCategory(Long days, CategoryENUM category) {
        LocalDate today = LocalDate.now();
        LocalDate upperDate = today.plusDays(days);
        return batchStockRepository.findByDueDateBetweenAndCategory(today,upperDate ,category).stream()
                .sorted(Comparator.comparing(BatchStock::getDueDate)).collect(Collectors.toList());
    }

    private void validateStockQuantity(List<BatchStock> batchStocks, Integer checkoutQuantity) {
        Integer quantityInStock = batchStocks.stream().map(b -> b.getCurrentQuantity()).reduce(0, Integer::sum);
        if(quantityInStock < checkoutQuantity)
            throw new BatchStockWithdrawException("STOCK_QUANTITY_NOT_ENOUGH");
    }

    private List<BatchStock> withdrawFromStock(List<BatchStock> batchStocks, Integer checkoutQuantity) {
        batchStocks = new OrderByDueDate().apply(batchStocks);
        List<BatchStock> changedStocks = new ArrayList<>();
        while(checkoutQuantity > 0) {
            BatchStock batchStock = batchStocks.stream().filter(b -> b.getCurrentQuantity() > 0).findFirst()
                    .orElseThrow(() -> new BatchStockWithdrawException("STOCK_QUANTITY_SUDDENLY_CHANGED"));
            Integer batchQuantity = batchStock.getCurrentQuantity() > checkoutQuantity
                    ? checkoutQuantity
                    : batchStock.getCurrentQuantity();
            batchStock.withdrawQuantity(batchQuantity);
            changedStocks.add(batchStock);
            checkoutQuantity -= batchQuantity;
        }
        return this.batchStockRepository.saveAll(changedStocks);
    }

    public List<BatchStock> withdrawStockByProductId(Map<Long, Integer> quantityByProductMap) {
        Map<Long, List<BatchStock>> stockByProductMap = quantityByProductMap.entrySet()
                .stream().map(quantityByProduct -> {
                    List<BatchStock> batchStocks = findByProductIdWithValidShelfLife(quantityByProduct.getKey());
                    validateStockQuantity(batchStocks, quantityByProduct.getValue());
                    return Map.entry(quantityByProduct.getKey(), batchStocks);
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> Stream.concat(a.stream(), b.stream()).collect(Collectors.toList())));
        return stockByProductMap.entrySet().stream().map(s ->
                withdrawFromStock(s.getValue(), quantityByProductMap.get(s.getKey()))
        ).flatMap(List::stream).collect(Collectors.toList());
    }
}

