package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.entity.Section;
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

    public List<BatchStock> save(List<BatchStock> batchStocks) {
        return batchStocks.stream().map(
                e -> batchStockRepository.save(e)).collect(Collectors.toList()
        );

    }
  
    public List<BatchStock> findByProductId(Long productId){
        return batchStockRepository.findByProductId(productId);
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

    public List<BatchStock> getAllBatchesByDueDate(Section section, Long days) {
        LocalDate today = LocalDate.now();
        LocalDate upperDate = today.plusDays(days);
        return section.getInboundOrders().stream().map(
                i -> i.getBatchStocks().stream().filter(batchStock ->
                        batchStock.getDueDate().isAfter(today) && batchStock.getDueDate().isBefore(upperDate)
                ).collect(Collectors.toList())
        ).flatMap(List::stream).sorted((b1, b2) -> {
            return b1.getDueDate().compareTo(b2.getDueDate());
        }).collect(Collectors.toList());
    }
}
