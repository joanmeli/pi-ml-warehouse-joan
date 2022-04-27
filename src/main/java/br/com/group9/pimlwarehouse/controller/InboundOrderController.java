package br.com.group9.pimlwarehouse.controller;

import br.com.group9.pimlwarehouse.dto.BatchStockDTO;
import br.com.group9.pimlwarehouse.dto.InboundOrderDTO;
import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.service.BatchStockService;
import br.com.group9.pimlwarehouse.service.InboundOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping("/fresh-products/inboundorder")
    public ResponseEntity<List<BatchStockDTO>> createInboundOrder(
            @RequestBody InboundOrderDTO order, UriComponentsBuilder uriBuilder
    ){
        // Salvando a ordem
        InboundOrder orderSaved = inboundOrderService.save(order.convert());
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
}
