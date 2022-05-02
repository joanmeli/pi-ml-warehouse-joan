package br.com.group9.pimlwarehouse.controller;

import br.com.group9.pimlwarehouse.dto.BatchStockDTO;
import br.com.group9.pimlwarehouse.dto.InboundOrderDTO;
import br.com.group9.pimlwarehouse.dto.ProductDTO;
import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.service.BatchStockService;
import br.com.group9.pimlwarehouse.service.InboundOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
public class InboundOrderController extends APIController{
    private InboundOrderService inboundOrderService;
    private BatchStockService batchStockService;

    public InboundOrderController(
            InboundOrderService inboundOrderService,
            BatchStockService batchStockService
    ) {
        this.inboundOrderService = inboundOrderService;
        this.batchStockService = batchStockService;
    }

    @PostMapping("/fresh-products/inboundorder")
    public ResponseEntity<List<BatchStockDTO>> createInboundOrder(
            @RequestBody InboundOrderDTO order, UriComponentsBuilder uriBuilder
    ){
        inboundOrderService.validateExistence(order.getOrderNumber());
        List<Map<ProductDTO, BatchStockDTO>> batchStocks = batchStockService.getProductInfo(order.getBatchStockList());
        // Salvando a ordem
        InboundOrder orderSaved = inboundOrderService.save(
            order.convert(),
            BatchStockDTO.convert(batchStocks, order.convert())
        );
        // Salvando os lotes
        List<BatchStock> batchStocksSaved = batchStockService.save(
                BatchStockDTO.convert(batchStocks, orderSaved)
        );
        List<BatchStockDTO> batchStockDTOS = BatchStockDTO.convert(batchStocksSaved);
        URI uri = uriBuilder
                .path("/fresh-products/inboundorder")
                .buildAndExpand(orderSaved.getId())
                .toUri();

        return ResponseEntity.created(uri).body(batchStockDTOS);
    }

    @PutMapping("/fresh-products/inboundorder")
    public ResponseEntity<List<BatchStockDTO>> update(
            @RequestBody  InboundOrderDTO order , UriComponentsBuilder uriBuilder
    ){
        InboundOrder orderToUpdate = inboundOrderService.get(order.getOrderNumber());
        List<Map<ProductDTO, BatchStockDTO>> batchStocks = batchStockService.getProductInfo(order.getBatchStockList());
        // Salvando os lotes
        List<BatchStock> inboundOrderUpdated = batchStockService.update(
                BatchStockDTO.convert(batchStocks, orderToUpdate), orderToUpdate
        );
        List<BatchStockDTO> batchStockDTOS = BatchStockDTO.convert(inboundOrderUpdated);
        URI uri = uriBuilder
                .path("/fresh-products/inboundorder")
                .buildAndExpand(orderToUpdate.getId())
                .toUri();

        return ResponseEntity.created(uri).body(batchStockDTOS);
    }
}
