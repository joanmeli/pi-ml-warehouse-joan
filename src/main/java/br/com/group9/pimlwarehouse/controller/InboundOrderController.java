package br.com.group9.pimlwarehouse.controller;

import br.com.group9.pimlwarehouse.dto.*;
import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.service.BatchStockService;
import br.com.group9.pimlwarehouse.service.InboundOrderService;
import br.com.group9.pimlwarehouse.service.ProductAPIService;
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

    @PutMapping("/fresh-products/inboundorder")
    public ResponseEntity<List<UpdateBatchStockDTO>> update(
            @RequestBody UpdateInboundOrderDTO order , UriComponentsBuilder uriBuilder
    ){
        InboundOrder orderToUpdate = inboundOrderService.get(order.getOrderNumber());
        List<BatchStock> batchStocks = UpdateBatchStockDTO.convert(order.getBatchStockList() ,orderToUpdate);
        // Salvando os lotes
        List<BatchStock> inboundOrderUpdated = batchStockService.update(
                batchStocks, orderToUpdate
        );
        List<UpdateBatchStockDTO> batchStockDTOS = UpdateBatchStockDTO.convert(inboundOrderUpdated);
        URI uri = uriBuilder
                .path("/fresh-products/inboundorder")
                .buildAndExpand(orderToUpdate.getId())
                .toUri();

        return ResponseEntity.created(uri).body(batchStockDTOS);
    }
}
