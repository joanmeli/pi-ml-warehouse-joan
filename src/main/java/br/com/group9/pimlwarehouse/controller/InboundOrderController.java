package br.com.group9.pimlwarehouse.controller;

import br.com.group9.pimlwarehouse.dto.InboundOrderDTO;
import br.com.group9.pimlwarehouse.service.InboundOrderService;
import br.com.group9.pimlwarehouse.service.WarehouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
public class InboundOrderController extends APIController{
    private InboundOrderService inboundOrderService;
    public InboundOrderController(InboundOrderService inboundOrderService) { this.inboundOrderService = inboundOrderService; }

    @PostMapping("/fresh-products/inboundorder")
    public ResponseEntity<InboundOrderDTO> createInboundOrder(
            @RequestBody InboundOrderDTO order, UriComponentsBuilder uriBuilder
    ){
        // Validar ordem de entradada
        inboundOrderService.validateInboundOrder(
                order.getSection().getWarehouseCode(), order.getSection().getSectionCode()
        );

        URI uri = uriBuilder
                .path("foo")
                .buildAndExpand(42)
                .toUri();

        return ResponseEntity.created(uri).body(order);
    }
}
