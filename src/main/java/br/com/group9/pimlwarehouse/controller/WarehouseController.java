package br.com.group9.pimlwarehouse.controller;

import br.com.group9.pimlwarehouse.dto.*;
import br.com.group9.pimlwarehouse.entity.Section;
import br.com.group9.pimlwarehouse.entity.Warehouse;
import br.com.group9.pimlwarehouse.service.WarehouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Map;

@RestController
public class WarehouseController extends APIController{
    private static final String BASE_PATH = "/warehouse";
    private WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping(BASE_PATH)
    public ResponseEntity<WarehouseDTO> createWarehouse(@Valid @RequestBody WarehouseDTO warehouseDTO, UriComponentsBuilder uriBuilder) {
        Warehouse mappedWarehouse = warehouseDTO.map();
        Warehouse savedWarehouse = warehouseService.createWarehouse(mappedWarehouse);
        WarehouseDTO resultWarehouse = WarehouseDTO.map(savedWarehouse);
        URI uri = uriBuilder
                .path(BASE_PATH.concat("/{id}"))
                .buildAndExpand(resultWarehouse.getId())
                .toUri();
        return ResponseEntity.created(uri).body(resultWarehouse);
    }

    @GetMapping(BASE_PATH + "/{warehouseId}")
    public ResponseEntity<WarehouseDTO> findWarehouse(@PathVariable(name = "warehouseId") Long warehouseId) {
        Warehouse foundWarehouse = this.warehouseService.findById(warehouseId);
        return ResponseEntity.ok(WarehouseDTO.map(foundWarehouse));
    }

    @GetMapping("/fresh-products/warehouse/{productId}")
    public ResponseEntity<ProductWarehouseDTO> findProductInWarehouse(@PathVariable Long productId) {
        Map<Long, Integer> product = warehouseService.getAllWarehousesByProduct(productId);
        return ResponseEntity.ok(ProductWarehouseDTO.convert(productId, WarehouseProductDTO.convert(product)));
    }
}
