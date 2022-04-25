package br.com.group9.pimlwarehouse.repository;

import br.com.group9.pimlwarehouse.entity.BatchStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchStockRepository extends JpaRepository<BatchStock, Long> {
}
