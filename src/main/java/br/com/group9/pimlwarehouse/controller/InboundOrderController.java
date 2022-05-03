package br.com.group9.pimlwarehouse.controller;

import br.com.group9.pimlwarehouse.dto.BatchStockDTO;
import br.com.group9.pimlwarehouse.dto.InboundOrderDTO;
import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.service.BatchStockService;
import br.com.group9.pimlwarehouse.service.InboundOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

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

    /**
     * POST method to create an InboundOrder.
     * @param order generate order payload.
     * @param uriBuilder Injection used by Spring to send the location.
     * @return URI of InboundOrder on header location, the entity response with status code "201-Created".
     * Register a batch with product stock.
     */

    @PostMapping("/fresh-products/inboundorder")
    public ResponseEntity<List<BatchStockDTO>> createInboundOrder(
            @RequestBody InboundOrderDTO order, UriComponentsBuilder uriBuilder
    ){
        inboundOrderService.validateExistence(order.getOrderNumber());
        // Salvando a ordem
        InboundOrder orderSaved = inboundOrderService.save(
            order.convert(), BatchStockDTO.convert(order.getBatchStockList(), order.convert())
        );
        // Salvando os lotes
        List<BatchStock> batchStocks = batchStockService.save(
                BatchStockDTO.convert(order.getBatchStockList(), orderSaved)
        );
        List<BatchStockDTO> batchStockDTOS = BatchStockDTO.convert(batchStocks);
        URI uri = uriBuilder
                .path("/fresh-products/inboundorder")
                .buildAndExpand(orderSaved.getId())
                .toUri();

        return ResponseEntity.created(uri).body(batchStockDTOS);
    }

    /**
     * PUT method to update List BatchStock
     * @param order generate order payload
     * @param uriBuilder Injection used by Spring to send the location
     * @return URI of InboundOrder on header location, the entity response with status code "201-Created" and update in BatchStock
     */
    @PutMapping("/fresh-products/inboundorder")
    public ResponseEntity<List<BatchStockDTO>> update(
            @RequestBody  InboundOrderDTO order , UriComponentsBuilder uriBuilder
    ){
        InboundOrder orderToUpdate = inboundOrderService.get(order.getOrderNumber());
        // Salvando os lotes
        List<BatchStock> inboundOrderUpdated = batchStockService.update(
                BatchStockDTO.convert(order.getBatchStockList(), orderToUpdate), orderToUpdate
        );
        List<BatchStockDTO> batchStockDTOS = BatchStockDTO.convert(inboundOrderUpdated);
        URI uri = uriBuilder
                .path("/fresh-products/inboundorder")
                .buildAndExpand(orderToUpdate.getId())
                .toUri();

        return ResponseEntity.created(uri).body(batchStockDTOS);
    }
}
