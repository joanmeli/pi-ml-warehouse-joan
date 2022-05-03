package br.com.group9.pimlwarehouse.controller;

import br.com.group9.pimlwarehouse.dto.BatchStockDTO;
import br.com.group9.pimlwarehouse.dto.InboundOrderDTO;
import br.com.group9.pimlwarehouse.dto.ProductDTO;
import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.service.BatchStockService;
import br.com.group9.pimlwarehouse.service.InboundOrderService;
import br.com.group9.pimlwarehouse.service.ProductAPIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
public class InboundOrderController extends APIController{
    private InboundOrderService inboundOrderService;
    private BatchStockService batchStockService;
    private ProductAPIService productAPIService;

    public InboundOrderController(
            InboundOrderService inboundOrderService,
            BatchStockService batchStockService,
            ProductAPIService productAPIService
    ) {
        this.inboundOrderService = inboundOrderService;
        this.batchStockService = batchStockService;
        this.productAPIService = productAPIService;
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

        List<Map<ProductDTO, BatchStockDTO>> batchStocks = productAPIService.getProductInfo(order.getBatchStockList());
        // Salvando a ordem
        InboundOrder orderSaved = inboundOrderService.save(
            order.convert(),
            BatchStockDTO.convert(batchStocks, order.convert())
        );
        List<BatchStockDTO> batchStockDTOS = BatchStockDTO.convert(orderSaved.getBatchStocks());
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
        List<Map<ProductDTO, BatchStockDTO>> batchStocks = productAPIService.getProductInfo(order.getBatchStockList());
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
