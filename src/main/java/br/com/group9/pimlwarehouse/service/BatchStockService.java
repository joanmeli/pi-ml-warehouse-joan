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

    /**
     * Save batch stock in database.
     * @param batchStocks receive a batch stock list.
     * @return the list of batch stock after persist in database.
     */
    public List<BatchStock> save(List<BatchStock> batchStocks) {
        return batchStocks.stream().map(
                e -> batchStockRepository.save(e)).collect(Collectors.toList()
        );

    }

    /**
     * Search a product by Id in batch stock.
     * @param productId receives a Long id of product.
     * @return the product according to the Id informed in a list of batch stock.
     */
    public List<BatchStock> findByProductId(Long productId){
        return batchStockRepository.findByProductId(productId);
    }

    /**
     * Search for a product with valid shelf life.
     * @param productId receives a Long id of product.
     * @return the product ID informed that it is within the validity period.
     */
    public List<BatchStock> findByProductIdWithValidShelfLife(Long productId){
        LocalDate maxDueDate = LocalDate.now().minusDays(21);
        List<BatchStock> byProductIdAndDueDateIsBefore = batchStockRepository.findByProductIdAndDueDateIsAfter(productId, maxDueDate);
        return byProductIdAndDueDateIsBefore;
    }

    /**
     * Update batch stock Id.
     * @param newBatchStock get oldBatchStock and retrieve the Id.
     * @param oldBatchStock get oldBatchStock Id.
     * @return return a new batch stock.
     */

    private BatchStock updateBatchStock(BatchStock newBatchStock, BatchStock oldBatchStock){
        oldBatchStock.setInitialQuantity(newBatchStock.getInitialQuantity());
        return newBatchStock;
    }

    /**
     * Update batch stock.
     * @param batchStocks receive a valid batch stock list.
     * @param newBatchStocks receive new batch stock list.
     * @return save the batch stock.
     */
    private List<BatchStock> updateBatchStocks(List<BatchStock> batchStocks, List<BatchStock> newBatchStocks){
        List<BatchStock> toSaveBatchStocks =  batchStocks.stream().map(batchStock ->
                updateBatchStock(newBatchStocks.stream()
                    .filter(nb -> batchStock.getBatchNumber().equals(nb.getBatchNumber()))
                    .findAny().orElseThrow(() -> new InboundOrderValidationException("BATCH_STOCK_NOT_FOUND")), batchStock)

        ).collect(Collectors.toList());
        return  save(toSaveBatchStocks);
    }

    /**
     * Checks the conditions of the inbound order and returns to updateBatchStocks.
     * @param batchStocks receive a batch stock list.
     * @param order receive the order that will be updated.
     * @return if order equal null or the size of order batch stock is different of batch stocks size, it throws a InboundOrderValidationException.
     * If not, will run updateBatchStocks.
     */
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
            List<BatchStock> batchStocks= getAllBatchesByDueDate(days);
            return batchStocks.stream().filter(batchStock ->
                    batchStock.getCategory().equals(category)
            ).collect(Collectors.toList());
        }

        Section section = sectionService.findById(sectionId);

        return getAllBatchesByDueDate(section,days);
    }

    public List<BatchStock> getAllBatchesByDueDate(Section section, Long days) {
        LocalDate today = LocalDate.now();
        LocalDate upperDate = today.plusDays(days);
        return section.getInboundOrders().stream().map(
                i -> batchStockRepository.findByDueDateBetweenAndInboundOrder(today,upperDate, i)
        ).flatMap(List::stream).sorted(Comparator.comparing(BatchStock::getDueDate)).collect(Collectors.toList());
    }

    public List<BatchStock> getAllBatchesByDueDate(Long days) {
        LocalDate today = LocalDate.now();
        LocalDate upperDate = today.plusDays(days);
        List<BatchStock> batchStocks = batchStockRepository.findAll();
        return batchStocks.stream().map(batchStock ->
                batchStockRepository.findByDueDateBetween(today,upperDate)
        ).flatMap(List::stream).sorted(Comparator.comparing(BatchStock::getDueDate)).collect(Collectors.toList());
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

