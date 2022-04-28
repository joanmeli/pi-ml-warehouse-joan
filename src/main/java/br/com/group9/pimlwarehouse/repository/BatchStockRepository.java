package br.com.group9.pimlwarehouse.repository;

import br.com.group9.pimlwarehouse.entity.BatchStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BatchStockRepository extends JpaRepository<BatchStock, Long> {
    List<BatchStock> findByProductId(Long productId);
}
