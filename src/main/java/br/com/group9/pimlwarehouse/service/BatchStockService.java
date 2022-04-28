package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.exceptions.InboundOrderValidationException;
import br.com.group9.pimlwarehouse.repository.BatchStockRepository;
import br.com.group9.pimlwarehouse.repository.InboundOrderRepository;
import org.springframework.stereotype.Service;

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

    public List<BatchStock> save(List<BatchStock> batchStocks) {
        return batchStocks.stream().map(
                e -> batchStockRepository.save(e)).collect(Collectors.toList()
        );

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
            throw new InboundOrderValidationException("INBOUNDORDER_MISSING");
        }
        return updateBatchStocks(order.getBatchStocks(), batchStocks);


    }
}
