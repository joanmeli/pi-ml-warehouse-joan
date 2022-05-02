package br.com.group9.pimlwarehouse.controller;

import br.com.group9.pimlwarehouse.dto.*;
import br.com.group9.pimlwarehouse.dto.WarehouseDTO;
import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.Warehouse;
import br.com.group9.pimlwarehouse.service.WarehouseService;
import br.com.group9.pimlwarehouse.util.batch_stock_order.OrderBatchStockEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Map;
import java.util.List;

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
     * @param uriBuilder injeção utilizada pelo spring para declarar para o spring enviar o location
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

    /**
     * GET method to search a product by id inside a warehouse
     * @param productId receives a Long id of product in the path variable
     * @return the warehouses where contains the product informed,
     * if there is no product with informed id on any warehouses, it return "404-Not Found"
     */

    @GetMapping("/fresh-products/warehouse/{productId}")
    public ResponseEntity<ProductWarehouseDTO> findProductInWarehouse(@PathVariable Long productId) {
        Map<Long, Integer> product = warehouseService.getAllWarehousesByProduct(productId);
        return ResponseEntity.ok(ProductWarehouseDTO.convert(productId, WarehouseProductDTO.convert(product)));
    }

    /**
     * GET method to find and list all of products where it appear in stock
     * @param productIds receives a list of productsIds where the list type is Long
     * @param orderBy receives the type of sorting that will be performed
     * @return the location of the products ordered by Batch number, current quantity and due date
     */

    @GetMapping("/fresh-products/list")
    public ResponseEntity<List<SectionBatchStockDTO>> findProductsInStock(
            @RequestParam(name = "products", required = false, defaultValue = "") List<Long> productIds,
            @RequestParam(name = "order_by", required = false, defaultValue = "DEFAULT") OrderBatchStockEnum orderBy
    ) {
        Map<Long, List<BatchStock>> foundBatchStocks = this.warehouseService.getProductsInStockByIds(productIds, orderBy);
        return ResponseEntity.ok(SectionBatchStockDTO.map(foundBatchStocks));
    }
}
