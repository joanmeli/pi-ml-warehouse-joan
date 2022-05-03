package br.com.group9.pimlwarehouse.controller;

import br.com.group9.pimlwarehouse.dto.*;
import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.enums.CategoryENUM;
import br.com.group9.pimlwarehouse.service.BatchStockService;
import br.com.group9.pimlwarehouse.service.SectionService;
import br.com.group9.pimlwarehouse.service.WarehouseService;
import br.com.group9.pimlwarehouse.util.batch_stock_order.OrderBatchStockEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class BatchStockController extends APIController {
    BatchStockService batchStockService;
    SectionService sectionService;
    WarehouseService warehouseService;

    public BatchStockController(
            BatchStockService batchStockService,
            SectionService sectionService,
            WarehouseService warehouseService
    ){
        this.batchStockService = batchStockService;
        this.sectionService = sectionService;
        this.warehouseService = warehouseService;
    }

    /**
     * GET method to search a product by id inside a warehouse.
     * @param productId receives a Long id of product in the path variable.
     * @return the warehouses where contains the product informed.
     * If there is no product with informed id on any warehouses, it return "404-Not Found".
     */
    
    @GetMapping("/fresh-products/warehouse/{productId}")
    public ResponseEntity<ProductWarehouseDTO> findProductInWarehouse(@PathVariable Long productId) {
        Map<Long, Integer> product = warehouseService.getAllWarehousesByProduct(productId);
        return ResponseEntity.ok(ProductWarehouseDTO.convert(productId, WarehouseProductDTO.convert(product)));
    }

    @GetMapping("/fresh-products/due-date")
    public ResponseEntity<List<BatchStockByDuaDateDTO>> findProductsByDueDate(
            @RequestParam (required = false) Long sectionId,
            @RequestParam Long days,
            @RequestParam(required = false) CategoryENUM category
    ) {

        List<BatchStock> product = batchStockService.getAllBatchesByDueDate(sectionId, days, category);
        return ResponseEntity.ok(BatchStockByDuaDateDTO.convert(product));
    }

    /**
     * GET method to find and list all of products where it appear in stock.
     * @param productIds receives a list of productsIds where the list type is Long.
     * @param orderBy receives the type of sorting that will be performed.
     * @return the location of the products ordered by Batch number, current quantity and due date.
     */

    @GetMapping("/fresh-products/list")
    public ResponseEntity<List<SectionBatchStockDTO>> findProductsInStock(
            @RequestParam(name = "products", required = false, defaultValue = "") List<Long> productIds,
            @RequestParam(name = "order_by", required = false, defaultValue = "DEFAULT") OrderBatchStockEnum orderBy
    ) {
        Map<Long, List<BatchStock>> foundBatchStocks = this.warehouseService.getProductsInStockByIds(productIds, orderBy);
        return ResponseEntity.ok(SectionBatchStockDTO.map(foundBatchStocks));
    }

    @PostMapping("/fresh-products/")
    public ResponseEntity<List<BatchStockDTO>> withdrawStock(@RequestBody List<ProductBatchStockDTO> products) {
        List<BatchStock> batchStocks = this.batchStockService.withdrawStockByProductId(ProductBatchStockDTO.convert(products));
        return ResponseEntity.ok(BatchStockDTO.convert(batchStocks));
    }
}
