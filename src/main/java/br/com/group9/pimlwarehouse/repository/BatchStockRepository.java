package br.com.group9.pimlwarehouse.repository;

import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.enums.CategoryENUM;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BatchStockRepository extends JpaRepository<BatchStock, Long> {
    List<BatchStock> findByProductId(Long productId);
    List<BatchStock> findByProductIdAndDueDateIsAfter(Long productId, LocalDate dueDate);
    List<BatchStock> findByDueDateBetweenAndInboundOrder(LocalDate startDate, LocalDate endDate, InboundOrder inboundOrder);
    List<BatchStock> findByDueDateBetweenAndCategory(LocalDate startDate, LocalDate endDate, CategoryENUM categoryENUM);
}
