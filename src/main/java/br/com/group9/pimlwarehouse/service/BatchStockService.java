package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.repository.BatchStockRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BatchStockService {
    private BatchStockRepository batchStockRepository;

    public BatchStockService(BatchStockRepository batchStockRepository) {
        this.batchStockRepository = batchStockRepository;
    }

    public List<BatchStock> save(List<BatchStock> batchStocks) {
        return batchStocks.stream().map(
                e -> batchStockRepository.save(e)).collect(Collectors.toList()
        );

    }
}
