package br.com.group9.pimlwarehouse.repository;

import br.com.group9.pimlwarehouse.entity.InboundOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboundOrderRepository extends JpaRepository<InboundOrder, Long> {
}
