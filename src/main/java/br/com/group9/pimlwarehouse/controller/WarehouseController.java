package br.com.group9.pimlwarehouse.controller;

import br.com.group9.pimlwarehouse.dto.WarehouseDTO;
import br.com.group9.pimlwarehouse.entity.Warehouse;
import br.com.group9.pimlwarehouse.service.WarehouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class WarehouseController extends APIController{
    private static final String BASE_PATH = "/warehouse";
    private WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    /**
     * POST method for create a new Warehouse
     * @param warehouseDTO receives a valid WarehouseDTO in body of the requisition
     * @param uriBuilder Injection used by Spring to send the location
     * @return the URI of warehouse on header location, the entity response with status code "201-Created" and
     * the WarehouseDTO created as result
     */

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

    /**
     * GET method to search a existing warehouse by id
     * @param warehouseId receives a Long id of warehouse in the path variable
     * @return the Warehouse registered with the id informed,
     * If there is no warehouse with the given id, it returns "WAREHOUSE_NOT_FOUND"
     */

    @GetMapping(BASE_PATH + "/{warehouseId}")
    public ResponseEntity<WarehouseDTO> findWarehouse(@PathVariable(name = "warehouseId") Long warehouseId) {
        Warehouse foundWarehouse = this.warehouseService.findById(warehouseId);
        return ResponseEntity.ok(WarehouseDTO.map(foundWarehouse));
    }
}
