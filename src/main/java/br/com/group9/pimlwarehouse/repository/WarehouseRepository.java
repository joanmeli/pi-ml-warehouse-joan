package br.com.group9.pimlwarehouse.repository;

import br.com.group9.pimlwarehouse.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long>{
}
