package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.exception.BatchStockWithdrawException;
import br.com.group9.pimlwarehouse.exception.InboundOrderValidationException;
import br.com.group9.pimlwarehouse.repository.BatchStockRepository;
import br.com.group9.pimlwarehouse.repository.InboundOrderRepository;
import br.com.group9.pimlwarehouse.util.batch_stock_order.OrderByDueDate;
import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BatchStockService {
    private BatchStockRepository batchStockRepository;
    private InboundOrderRepository inboundOrderRepository;

    public BatchStockService(BatchStockRepository batchStockRepository, InboundOrderRepository inboundOrderRepository) {
        this.batchStockRepository = batchStockRepository;
        this.inboundOrderRepository = inboundOrderRepository;
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
        LocalDate maxDueDate = LocalDate.now().minusDays(21);
        List<BatchStock> byProductIdAndDueDateIsBefore = batchStockRepository.findByProductIdAndDueDateIsAfter(productId, maxDueDate);
        return byProductIdAndDueDateIsBefore;
    }
  
    private BatchStock updateBatchStockId(BatchStock newBatchStock, BatchStock oldBatchStock){
        newBatchStock.setId(oldBatchStock.getId());
        return newBatchStock;
    }

    private List<BatchStock> updateBatchStocks(List<BatchStock> batchStocks, List<BatchStock> newBatchStocks){
        List<BatchStock> toSaveBatchStocks =  batchStocks.stream().map(batchStock ->
                updateBatchStockId(newBatchStocks.stream()
                    .filter(nb -> batchStock.getBatchNumber().equals(nb.getBatchNumber()))
                    .findAny().get(), batchStock)

        ).collect(Collectors.toList());
        return  save(toSaveBatchStocks);
    }

    public List<BatchStock> update(List<BatchStock> batchStocks, InboundOrder order) {

        if(order == null) {
            throw new InboundOrderValidationException("INBOUND_ORDER_NOT_FOUND");
        }
        if (order.getBatchStocks().size() != batchStocks.size()){
            throw new InboundOrderValidationException("INBOUND_ORDER_MISSING");
        }
        return updateBatchStocks(order.getBatchStocks(), batchStocks);
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
