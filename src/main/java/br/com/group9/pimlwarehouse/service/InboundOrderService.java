package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.exception.InboundOrderValidationException;
import br.com.group9.pimlwarehouse.repository.InboundOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InboundOrderService {

    private WarehouseService warehouseService;
    private SectionService sectionService;
    private InboundOrderRepository inboundOrderRepository;

    public InboundOrderService(
            WarehouseService warehouseService, SectionService sectionService,
            InboundOrderRepository inboundOrderRepository
    ) {
        this.warehouseService = warehouseService;
        this.sectionService  = sectionService;
        this.inboundOrderRepository = inboundOrderRepository;
    }

    /**
     * @param id receives a order number
     * @return the result of search in database if exists,
     * if not, it will return null
     */
    public InboundOrder get(Long id) {
        Optional<InboundOrder> op = this.inboundOrderRepository.findById(id);
        return op.orElse(null);
    }

    /**
     * @param warehouseId receives a warehouseId of inboundOrder
     * @param sectorId receives a sectorId of inboundOrder
     * @param batchStocks receives a Batch stock list
     */

    public void validateInboundOrder(
            Long warehouseId, Long sectorId, List<BatchStock> batchStocks
    ) {
        // Verifica se armazem existe
        if (!warehouseService.exists(warehouseId)){
            throw new InboundOrderValidationException("WAREHOUSE_NOT_FOUND");
        }
        // validar o setor
        sectionService.validateBatchStocksBySection(sectorId, batchStocks);

    }

    /**
     * @param order receives a InboundOrderDTO
     * @param batchStocks receives a Batch stock list
     * @return a new Inbound order after validates if warehouse and sections exists
     */

    public InboundOrder save (InboundOrder order, List<BatchStock> batchStocks) {

        // Validar ordem de entrada
        validateInboundOrder(
                order.getSection().getWarehouse().getId(), order.getSection().getId(),
                batchStocks
        );
        return inboundOrderRepository.save(order);
    }

    /**
     * @param id receives a order number
     */
    public void validateExistence(Long id) {
        if(get(id) != null) {
            throw new InboundOrderValidationException("INBOUND_ORDER_ALREADY_EXISTS");
        }
    }
}
