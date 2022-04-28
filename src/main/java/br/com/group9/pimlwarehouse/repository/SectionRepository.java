package br.com.group9.pimlwarehouse.repository;

import br.com.group9.pimlwarehouse.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
}
