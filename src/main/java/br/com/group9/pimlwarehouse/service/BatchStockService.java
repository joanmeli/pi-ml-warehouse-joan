package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.dto.BatchStockDTO;
import br.com.group9.pimlwarehouse.dto.ProductDTO;
import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.entity.Section;
import br.com.group9.pimlwarehouse.enums.CategoryENUM;
import br.com.group9.pimlwarehouse.exception.InboundOrderValidationException;
import br.com.group9.pimlwarehouse.repository.BatchStockRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BatchStockService {
    private BatchStockRepository batchStockRepository;
    private SectionService sectionService;
    private ProductAPIService productAPIService;


    public BatchStockService(
            BatchStockRepository batchStockRepository,
            SectionService sectionService,
            ProductAPIService productAPIService

    ) {
        this.batchStockRepository = batchStockRepository;
        this.sectionService = sectionService;
        this.productAPIService = productAPIService;
    }

    public List<BatchStock> save(List<BatchStock> batchStocks) {
        return batchStocks.stream().map(
                e -> batchStockRepository.save(e)).collect(Collectors.toList()
        );

    }
  
    public List<BatchStock> findByProductId(Long productId){
        return batchStockRepository.findByProductId(productId);
    }
  
    private BatchStock updateBatchStock(BatchStock newBatchStock, BatchStock oldBatchStock){
        oldBatchStock.setInitialQuantity(newBatchStock.getInitialQuantity());
        return newBatchStock;
    }

    private List<BatchStock> updateBatchStocks(List<BatchStock> batchStocks, List<BatchStock> newBatchStocks){
        List<BatchStock> toSaveBatchStocks =  batchStocks.stream().map(batchStock ->
                updateBatchStock(newBatchStocks.stream()
                    .filter(nb -> batchStock.getBatchNumber().equals(nb.getBatchNumber()))
                    .findAny().orElseThrow(() -> new InboundOrderValidationException("BATCH_STOCK_NOT_FOUND")), batchStock)

        ).collect(Collectors.toList());
        return  save(toSaveBatchStocks);
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
        return batchStocks.stream().filter(batchStock ->
                batchStock.getDueDate().isAfter(today) && batchStock.getDueDate().isBefore(upperDate)
        ).sorted(Comparator.comparing(BatchStock::getDueDate)).collect(Collectors.toList());
    }


}

