package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.entity.Warehouse;
import br.com.group9.pimlwarehouse.repository.WarehouseRepository;
import org.springframework.stereotype.Service;

@Service
public class WarehouseService {
    private WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public Warehouse createWarehouse(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }
}
