package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.Warehouse;
import br.com.group9.pimlwarehouse.exception.ProductNotFoundException;
import br.com.group9.pimlwarehouse.exception.WarehouseNotFoundException;
import br.com.group9.pimlwarehouse.repository.WarehouseRepository;
import br.com.group9.pimlwarehouse.util.batch_stock_order.OrderBatchStockEnum;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WarehouseService {
    private WarehouseRepository warehouseRepository;

    private BatchStockService batchStockService;

    public WarehouseService(WarehouseRepository warehouseRepository, BatchStockService batchStockService) {
        this.warehouseRepository = warehouseRepository;
        this.batchStockService = batchStockService;
    }

    /**
     * Create Warehouse.
     * @param warehouse receive a mapped warehouse.
     * @return warehouse created and saved in repository.
     */
    public Warehouse createWarehouse(Warehouse warehouse) {
        warehouse.getSections().forEach(s -> s.setWarehouse(warehouse));
        return warehouseRepository.save(warehouse);
    }

    /**
     * Verify if warehouse exists.
     * @param id receives a Long Id to verify if exists.
     * @return true or false if the warehouse exists or not with the informed warehouse Id.
     */
    public boolean exists(Long id) {
        Optional<Warehouse> op = this.warehouseRepository.findById(id);
        return op.isPresent();
    }

    /**
     * Search warehouse by id.
     * @param warehouseId receives a warehouse Id to make the search.
     * @return returns the warehouse with the corresponding Id, if not, returns WarehouseNotFoundException.
     */
    public Warehouse findById(Long warehouseId) {
        return this.warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new WarehouseNotFoundException("WAREHOUSE_NOT_FOUND"));
    }

    /**
     * Search a product in all warehouses by Id.
     * @param productId Receives a Long where is a productId to be searched.
     * @return all warehouses where contains the product.
     */
    public Map<Long, Integer> getAllWarehousesByProduct(Long productId) {

        List<BatchStock> batchStocks = batchStockService.findByProductId(productId);

        if(batchStocks.isEmpty()) {
            throw new ProductNotFoundException("PRODUCT_NOT_FOUND");
        }

        Map<Long, Integer> warehouses = batchStocks
                .stream()
                .collect(Collectors.toMap(k -> k.getInboundOrder().getSection().getWarehouse().getId(),
                        BatchStock::getCurrentQuantity,
                        (a, b) -> a + b ));
        return warehouses;

    }

    /**
     * Search all warehouses and sections for BatchStocks containing the given productIds.
     * @param productsId Receives a List<Long> where each Long is a productId to be searched.
     * @return a Map<Long, List<BatchStock>> where the Long value is the productId and the List is the
     * BatchStock that contains the given productId.
     */
    public Map<Long, List<BatchStock>> getProductsInStockByIds(List<Long> productsId, OrderBatchStockEnum orderBy) {
        Map<Long, List<BatchStock>> productsMap = productsId.stream()
                .map(p ->
                    Map.entry(p, orderBy.getOrderByStrategy().apply(batchStockService.findByProductIdWithValidShelfLife(p))))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return productsMap;
    }

}
