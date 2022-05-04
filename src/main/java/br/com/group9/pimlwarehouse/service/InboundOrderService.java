package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.dto.BatchStockDTO;
import br.com.group9.pimlwarehouse.dto.ProductDTO;
import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.exception.InboundOrderValidationException;
import br.com.group9.pimlwarehouse.repository.InboundOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class InboundOrderService {

    private WarehouseService warehouseService;
    private SectionService sectionService;
    private BatchStockService batchStockService;
    private InboundOrderRepository inboundOrderRepository;

    public InboundOrderService(
            WarehouseService warehouseService, SectionService sectionService, BatchStockService batchStockService,
            InboundOrderRepository inboundOrderRepository
    ) {
        this.warehouseService = warehouseService;
        this.sectionService  = sectionService;
        this.batchStockService = batchStockService;
        this.inboundOrderRepository = inboundOrderRepository;
    }


    public InboundOrder get(Long id) {
        Optional<InboundOrder> op = this.inboundOrderRepository.findById(id);
        return op.orElse(null);
    }

    public void validateInboundOrder(
            Long warehouseId, Long sectorId, Long orderId, List<BatchStock> batchStocks
    ) {
        validateExistence(orderId);

        // Verifica se armazem existe
        if (!warehouseService.exists(warehouseId)){
            throw new InboundOrderValidationException("WAREHOUSE_NOT_FOUND");
        }
        // validar o setor
        sectionService.validateBatchStocksBySection(sectorId, warehouseId, batchStocks);

    }

    public InboundOrder save (InboundOrder order, List<BatchStock> batchStocks) {
        order.setBatchStocks(batchStocks);
        // Validar ordem de entrada
        validateInboundOrder(
                order.getSection().getWarehouse().getId(), order.getSection().getId(), order.getId(),
                batchStocks
        );
        InboundOrder orderSaved = inboundOrderRepository.save(order);
        return orderSaved;
    }

    public void validateExistence(Long id) {
        if(get(id) != null) {
            throw new InboundOrderValidationException("INBOUND_ORDER_ALREADY_EXISTS");
        }
    }
}
