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
     * Search an inbound order by Id.
     * @param id receives an order number.
     * @return the result of search in database if exists.
     * If not, it will return null.
     */
    public InboundOrder get(Long id) {
        Optional<InboundOrder> op = this.inboundOrderRepository.findById(id);
        return op.orElse(null);
    }

    /**
     * Validate if inbound order and section exists.
     * @param warehouseId receives a warehouseId of inboundOrder.
     * @param sectorId receives a sectorId of inboundOrder.
     * @param batchStocks receives a Batch stock list.
     */

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

    /**
     * Save in database a new inbound order.
     * @param order receives a InboundOrderDTO.
     * @param batchStocks receives a Batch stock list.
     * @return a new Inbound order after validates if warehouse and sections exists.
     */

    public InboundOrder save (InboundOrder order, List<BatchStock> batchStocks) {
        order.setBatchStocks(batchStocks);
        // Validar ordem de entrada
        validateInboundOrder(
                order.getSection().getWarehouse().getId(), order.getSection().getId(), order.getId(),
                batchStocks
        );
        return inboundOrderRepository.save(order);
    }

    /**
     * Validate inbound order if exists.
     * @param id receives an order number.
     */
    public void validateExistence(Long id) {
        if(get(id) != null) {
            throw new InboundOrderValidationException("INBOUND_ORDER_ALREADY_EXISTS");
        }
    }
}
