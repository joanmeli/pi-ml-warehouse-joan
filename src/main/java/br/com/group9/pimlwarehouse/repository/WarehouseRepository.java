package br.com.group9.pimlwarehouse.repository;

import br.com.group9.pimlwarehouse.entity.Warehouse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface WarehouseRepository extends ElasticsearchRepository<Warehouse, Long> {
}
