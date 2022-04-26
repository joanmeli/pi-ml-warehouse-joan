package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.entity.Warehouse;
import br.com.group9.pimlwarehouse.exceptions.InboundOrderValidationException;
import br.com.group9.pimlwarehouse.repository.WarehouseRepository;
import org.springframework.stereotype.Service;

@Service
public class InboundOrderService {

    private WarehouseService warehouseService;

    public InboundOrderService(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    public void validateInboundOrder(String warehouseId, String sectorId){
        // Verifica se armazem existe
        if (!warehouseService.exists(Long.valueOf(warehouseId))){
            throw new InboundOrderValidationException("WAREHOUSE_NOT_FOUND");
        }


    }
}
