package br.com.group9.pimlwarehouse.repository;

import br.com.group9.pimlwarehouse.entity.BatchStock;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BatchStockRepository extends ElasticsearchRepository<BatchStock, Long> {
}
