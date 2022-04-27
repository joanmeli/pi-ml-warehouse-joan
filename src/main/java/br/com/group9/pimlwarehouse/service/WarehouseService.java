package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.entity.Warehouse;
import br.com.group9.pimlwarehouse.exception.WarehouseNotFoundException;
import br.com.group9.pimlwarehouse.repository.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WarehouseService {
    private WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public Warehouse createWarehouse(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    public boolean exists(Long id) {
        Optional<Warehouse> op = this.warehouseRepository.findById(id);
        return op.isPresent();
    }

    public Warehouse findById(Long warehouseId) {
        return this.warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new WarehouseNotFoundException("WAREHOUSE_NOT_FOUND"));
    }
}
