package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.exceptions.InboundOrderValidationException;
import br.com.group9.pimlwarehouse.repository.InboundOrderRepository;
import org.springframework.stereotype.Service;

@Service
public class InboundOrderService {

    private WarehouseService warehouseService;
    private SectionService sectionService;
    private InboundOrderRepository inboundOrderRepository;

    public InboundOrderService(WarehouseService warehouseService, SectionService sectionService, InboundOrderRepository inboundOrderRepository) {
        this.warehouseService = warehouseService;
        this.sectionService  = sectionService;
        this.inboundOrderRepository = inboundOrderRepository;
    }

    public void validateInboundOrder(String warehouseId, String sectorId){
        // Verifica se armazem existe
        if (!warehouseService.exists(Long.valueOf(warehouseId))){
            throw new InboundOrderValidationException("WAREHOUSE_NOT_FOUND");
        }
        // verifica se o setor existe
        if (!sectionService.exists(Long.valueOf(sectorId))){
            throw new InboundOrderValidationException("SECTION_NOT_FOUND");
        }


    }

    public InboundOrder save (InboundOrder order) {
        return inboundOrderRepository.save(order);
    }
}
