package br.com.group9.pimlwarehouse.repository;

import br.com.group9.pimlwarehouse.entity.InboundOrder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface InboundOrderRepository extends ElasticsearchRepository<InboundOrder, Long> {
}
