package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.exception.InboundOrderValidationException;
import br.com.group9.pimlwarehouse.repository.BatchStockRepository;
import br.com.group9.pimlwarehouse.repository.InboundOrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BatchStockService {
    private BatchStockRepository batchStockRepository;
    private InboundOrderRepository inboundOrderRepository;


    public BatchStockService(BatchStockRepository batchStockRepository, InboundOrderRepository inboundOrderRepository) {
        this.batchStockRepository = batchStockRepository;
        this.inboundOrderRepository = inboundOrderRepository;
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
    private BatchStock updateBatchStockId(BatchStock newBatchStock, BatchStock oldBatchStock){
        newBatchStock.setId(oldBatchStock.getId());
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
                updateBatchStockId(newBatchStocks.stream()
                    .filter(nb -> batchStock.getBatchNumber().equals(nb.getBatchNumber()))
                    .findAny().get(), batchStock)

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
            throw new InboundOrderValidationException("INBOUND_ORDER_MISSING");
        }
        return updateBatchStocks(order.getBatchStocks(), batchStocks);
    }
}
