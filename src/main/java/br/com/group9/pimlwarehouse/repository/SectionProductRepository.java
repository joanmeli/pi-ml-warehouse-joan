package br.com.group9.pimlwarehouse.repository;

import br.com.group9.pimlwarehouse.entity.SectionProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionProductRepository extends JpaRepository<SectionProduct, Long> {
    boolean existsBySectionIdAndProductId(Long sectionId, Long productId);
}
