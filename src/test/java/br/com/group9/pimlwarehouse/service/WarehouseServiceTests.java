package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.entity.Section;
import br.com.group9.pimlwarehouse.entity.Warehouse;
import br.com.group9.pimlwarehouse.enums.CategoryENUM;
import br.com.group9.pimlwarehouse.repository.WarehouseRepository;
import br.com.group9.pimlwarehouse.util.batch_stock_order.OrderBatchStockEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class WarehouseServiceTests {
    private WarehouseService warehouseService;
    private WarehouseRepository warehouseRepositoryMock;
    private BatchStockService batchStockServiceMock;

    @BeforeEach
    public void setupTests() {
        warehouseRepositoryMock = Mockito.mock(WarehouseRepository.class);
        batchStockServiceMock = Mockito.mock(BatchStockService.class);
        warehouseService = new WarehouseService(warehouseRepositoryMock, batchStockServiceMock);
    }

    /**
     * Métodos de WarehouseService:
     *  - public Warehouse createWarehouse(Warehouse warehouse)
     *  - public boolean exists(Long id)
     *  - public Warehouse findById(Long warehouseId)
     *  - public Map<Long, Integer> getAllWarehousesByProduct(Long productId)
     *  - public Map<Long, List<BatchStock>> getProductsInStockByIds(List<Long> productsId, OrderBatchStockEnum orderBy)
     */

    /**
     *  - public Warehouse createWarehouse(Warehouse warehouse)
     *      Cenários:
     *          - Cria um novo Warehouse com sucesso.
     *          - Cria um Warehouse com mais de um setor.
     */

    @Test
    public void shouldCreateNewWarehouse() {
        List<Section> validSections = Arrays.asList(
                Section.builder()
                        .maximalTemperature(0.0)
                        .minimalTemperature(-1.0)
                        .size(500)
                        .build()
        );
        Warehouse validWarehouse = Warehouse.builder()
                .name("Warehouse Test 1")
                .sections(validSections)
                .build();

        Warehouse registeredWarehouse = Warehouse.builder()
                .id(1L)
                .name(validWarehouse.getName())
                .build();
        List<Section> registeredSections = Arrays.asList(
                Section.builder()
                        .id(1L)
                        .maximalTemperature(validSections.get(0).getMaximalTemperature())
                        .minimalTemperature(validSections.get(0).getMinimalTemperature())
                        .size(validSections.get(0).getSize())
                        .warehouse(registeredWarehouse)
                        .build()
        );
        registeredWarehouse.setSections(registeredSections);


        Mockito.when(warehouseRepositoryMock.save(Mockito.any(Warehouse.class)))
                .thenReturn(registeredWarehouse);

        Assertions.assertDoesNotThrow(() -> {
            Warehouse createdWarehouse = warehouseService.createWarehouse(validWarehouse);
            Assertions.assertNotNull(createdWarehouse.getId());
            Assertions.assertNotNull(createdWarehouse.getSections().stream()
                    .map(s -> s.getId()).collect(Collectors.toList()));
            Assertions.assertNotEquals(0, createdWarehouse.getSections().stream()
                    .filter(s -> s.getWarehouse() != null).collect(Collectors.toList()).size());
        });
    }

    @Test
    public void shouldCreateWarehouseWithMoreThan1Section() {

        List<Section> validSections = Arrays.asList(
                Section.builder()
                        .maximalTemperature(0.0)
                        .minimalTemperature(-1.0)
                        .size(500)
                        .build(),
                Section.builder()
                        .maximalTemperature(-2.0)
                        .minimalTemperature(-5.0)
                        .size(500)
                        .build(),
                Section.builder()
                        .maximalTemperature(-4.0)
                        .minimalTemperature(-8.0)
                        .size(500)
                        .build()
        );
        Warehouse validWarehouse = Warehouse.builder()
                .name("Warehouse Test 2")
                .sections(validSections)
                .build();

        Warehouse registeredWarehouse = Warehouse.builder()
                .id(1L)
                .name(validWarehouse.getName())
                .build();
        List<Section> registeredSections = Arrays.asList(
                Section.builder()
                        .id(1L)
                        .maximalTemperature(validSections.get(0).getMaximalTemperature())
                        .minimalTemperature(validSections.get(0).getMinimalTemperature())
                        .size(validSections.get(0).getSize())
                        .warehouse(registeredWarehouse)
                        .build(),
                Section.builder()
                        .id(2L)
                        .maximalTemperature(validSections.get(1).getMaximalTemperature())
                        .minimalTemperature(validSections.get(1).getMinimalTemperature())
                        .size(validSections.get(1).getSize())
                        .warehouse(registeredWarehouse)
                        .build(),
                Section.builder()
                        .id(3L)
                        .maximalTemperature(validSections.get(2).getMaximalTemperature())
                        .minimalTemperature(validSections.get(2).getMinimalTemperature())
                        .size(validSections.get(2).getSize())
                        .warehouse(registeredWarehouse)
                        .build()
        );
        registeredWarehouse.setSections(registeredSections);

        Mockito.when(warehouseRepositoryMock.save(Mockito.any(Warehouse.class)))
                .thenReturn(registeredWarehouse);

        Assertions.assertDoesNotThrow(() -> {
            Warehouse createdWarehouse = warehouseService.createWarehouse(validWarehouse);
            Assertions.assertNotNull(createdWarehouse.getId());
            Assertions.assertNotNull(createdWarehouse.getSections().stream()
                    .map(s -> s.getId()).collect(Collectors.toList()));
            Assertions.assertNotEquals(0, createdWarehouse.getSections().stream()
                    .filter(s -> s.getWarehouse() != null).collect(Collectors.toList()).size());
        });
    }

    /**
     *  - public boolean exists(Long id)
     *      Cenários:
     *          - Warehouse existe
     *          - Warehouse não existe
     */

    @Test
    public void shouldReturnTrueWhenWarehouseExists() {
        Warehouse registeredWarehouse = Warehouse.builder()
                .id(1L)
                .name("Warehouse Test 3")
                .sections(Arrays.asList())
                .build();

        Mockito.when(warehouseRepositoryMock.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(registeredWarehouse));

        Assertions.assertDoesNotThrow(() -> {
            boolean exists = warehouseService.exists(1L);
            Assertions.assertTrue(exists);
        });
    }

    @Test
    public void shouldReturnFalseWhenWarehouseDoesNotExists() {
        Mockito.when(warehouseRepositoryMock.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertDoesNotThrow(() -> {
            boolean exists = warehouseService.exists(1L);
            Assertions.assertFalse(exists);
        });
    }

    /**
     *  - public Warehouse findById(Long warehouseId)
     *      Cenários:
     *          - Warehouse é encontrado com sucesso.
     *          - Warehouse não é encontrado (lança exceção)
     */

    @Test
    public void shouldFindWhenWarehouseIsFound() {
        Warehouse registeredWarehouse = Warehouse.builder()
                .id(1L)
                .name("Warehouse Test 4")
                .sections(Arrays.asList())
                .build();

        Mockito.when(warehouseRepositoryMock.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(registeredWarehouse));

        Assertions.assertDoesNotThrow(() -> {
            Warehouse foundWarehouse = warehouseService.findById(1L);
            Assertions.assertEquals(registeredWarehouse, foundWarehouse);
        });
    }

    @Test
    public void shouldThrowExceptionWhenWarehouseNotFound() {
        Mockito.when(warehouseRepositoryMock.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () -> {
            warehouseService.findById(1L);
        });
        Assertions.assertEquals("WAREHOUSE_NOT_FOUND", runtimeException.getMessage());
    }

    /**
     *  - public Map<Long, Integer> getAllWarehousesByProduct(Long productId)
     *      Cenários:
     *          - Não encontra o lote em nenhum Warehouse
     *          - Encontra o lote do produto em mais de um Warehouse
     *          - Encontra o lote do produto em mais de um Setor no mesmo Warehouse
     */

    @Test
    public void shouldReturnEmptyWhenWarehousesOutOfStock(){
        Mockito.when(batchStockServiceMock.findByProductId(Mockito.anyLong()))
                .thenReturn(Arrays.asList());

        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () -> {
            warehouseService.getAllWarehousesByProduct(1L);
        });

        Assertions.assertEquals("PRODUCT_NOT_FOUND", runtimeException.getMessage());
    }

    @Test
    public void shouldFindStockWhenBatchStockExistsInMoreThan1Warehouse() {
        Warehouse warehouse1 = createWarehouseWith1Section(1L);

        InboundOrder inboundOrder1 = createInboundWith1BatchStockSameProduct(1L, warehouse1.getSections().get(0));
        warehouse1.getSections().get(0).setInboundOrders(Arrays.asList(inboundOrder1));

        Warehouse warehouse2 = createWarehouseWith1Section(2L);

        InboundOrder inboundOrder2 = createInboundWith1BatchStockSameProduct(2L, warehouse2.getSections().get(0));
        warehouse2.getSections().get(0).setInboundOrders(Arrays.asList(inboundOrder2));

        BatchStock batchStock1 = warehouse1.getSections().get(0).getInboundOrders().get(0).getBatchStocks().get(0);
        BatchStock batchStock2 = warehouse2.getSections().get(0).getInboundOrders().get(0).getBatchStocks().get(0);

        Mockito.when(batchStockServiceMock.findByProductId(Mockito.anyLong()))
                .thenReturn(Arrays.asList(batchStock1, batchStock2));

        Assertions.assertDoesNotThrow(() -> {
            Map<Long, Integer> warehousesByProductMap = warehouseService.getAllWarehousesByProduct(1L);

            Assertions.assertEquals(2, warehousesByProductMap.size());
            Assertions.assertEquals(100, warehousesByProductMap.get(1L));
            Assertions.assertEquals(100, warehousesByProductMap.get(2L));
        });
    }

    @Test
    public void shouldFindStockWhenBatchStockExistsInMoreThan1Section() {
        Warehouse warehouse1 = createWarehouseWith2Section(1L);

        InboundOrder inboundOrder1 = createInboundWith1BatchStockSameProduct(2L, warehouse1.getSections().get(0));
        InboundOrder inboundOrder2 = createInboundWith1BatchStockSameProduct(2L, warehouse1.getSections().get(0));

        warehouse1.getSections().get(0).setInboundOrders(Arrays.asList(inboundOrder1));
        warehouse1.getSections().get(1).setInboundOrders(Arrays.asList(inboundOrder2));

        BatchStock batchStock1 = warehouse1.getSections().get(0).getInboundOrders().get(0).getBatchStocks().get(0);
        BatchStock batchStock2 = warehouse1.getSections().get(1).getInboundOrders().get(0).getBatchStocks().get(0);

        Mockito.when(batchStockServiceMock.findByProductId(Mockito.anyLong()))
                .thenReturn(Arrays.asList(batchStock1, batchStock2));

        Assertions.assertDoesNotThrow(() -> {
            Map<Long, Integer> warehousesByProductMap = warehouseService.getAllWarehousesByProduct(1L);

            Assertions.assertEquals(1, warehousesByProductMap.size());
            Assertions.assertEquals(200, warehousesByProductMap.get(1L));
        });
    }

    /**
     *  - public Map<Long, List<BatchStock>> getProductsInStockByIds(List<Long> productsId, OrderBatchStockEnum orderBy)
     *      Cenários:
     *          - Não encontra nenhum lote de estoque do produto
     *          - Encontra lotes de estoque para 1 produto
     *          - Encontra lotes de estoque para mais de 1 produto
     *          - Encontra lotes de estoque ordenando por núm. do lote
     *          - Encontra lotes de estoque ordenando por quantidade atual
     *          - Encontra lotes de estoque ordenando por data de validade
     */

    @Test
    public void shouldReturnEmptyWhenProductOutOfStock() {
        Mockito.when(batchStockServiceMock.findByProductIdWithValidShelfLife(Mockito.anyLong()))
                .thenReturn(Arrays.asList());
        Assertions.assertDoesNotThrow(() -> {
            Map<Long, List<BatchStock>> productsMap = warehouseService.getProductsInStockByIds(
                    Arrays.asList(1L),
                    OrderBatchStockEnum.DEFAULT
            );
            Assertions.assertEquals(1, productsMap.size());
            Assertions.assertEquals(0, productsMap.get(1L).size());
        });
    }

    @Test
    public void shouldFindBatchStocksFor1Product() {
        Warehouse warehouse1 = createWarehouseWith1Section(1L);

        InboundOrder inboundOrder1 = createInboundWith1BatchStockSameProduct(1L, warehouse1.getSections().get(0));
        InboundOrder inboundOrder2 = createInboundWith2BatchStockSameProduct(2L, warehouse1.getSections().get(0));
        warehouse1.getSections().get(0).setInboundOrders(Arrays.asList(inboundOrder1, inboundOrder2));

        BatchStock batchStock1 = inboundOrder1.getBatchStocks().get(0);
        BatchStock batchStock2 = inboundOrder2.getBatchStocks().get(0);
        BatchStock batchStock3 = inboundOrder2.getBatchStocks().get(1);

        Mockito.when(batchStockServiceMock.findByProductIdWithValidShelfLife(Mockito.anyLong()))
                .thenReturn(Arrays.asList(batchStock1, batchStock2, batchStock3));

        Assertions.assertDoesNotThrow(() -> {
            Map<Long, List<BatchStock>> productsMap = warehouseService.getProductsInStockByIds(
                    Arrays.asList(1L),
                    OrderBatchStockEnum.DEFAULT
            );
            Assertions.assertEquals(1, productsMap.size());
            Assertions.assertEquals(3, productsMap.get(1L).size());
            Assertions.assertTrue(productsMap.get(1L).contains(batchStock1));
            Assertions.assertTrue(productsMap.get(1L).contains(batchStock2));
            Assertions.assertTrue(productsMap.get(1L).contains(batchStock3));
        });
    }

    @Test
    public void shouldFindBatchStocksForMoreThan1Product() {
        Warehouse warehouse1 = createWarehouseWith1Section(1L);

        InboundOrder inboundOrder1 = createInboundWith1BatchStockSameProduct(1L, warehouse1.getSections().get(0));
        InboundOrder inboundOrder2 = createInboundWith2BatchStockDifferentProduct(2L, warehouse1.getSections().get(0));
        warehouse1.getSections().get(0).setInboundOrders(Arrays.asList(inboundOrder1, inboundOrder2));

        BatchStock batchStock1 = inboundOrder1.getBatchStocks().get(0);
        BatchStock batchStock2 = inboundOrder2.getBatchStocks().get(0);
        BatchStock batchStock3 = inboundOrder2.getBatchStocks().get(1);

        Mockito.when(batchStockServiceMock.findByProductIdWithValidShelfLife(1L))
                .thenReturn(Arrays.asList(batchStock1));
        Mockito.when(batchStockServiceMock.findByProductIdWithValidShelfLife(2L))
                .thenReturn(Arrays.asList(batchStock2, batchStock3));

        Assertions.assertDoesNotThrow(() -> {
            Map<Long, List<BatchStock>> productsMap = warehouseService.getProductsInStockByIds(
                    Arrays.asList(1L, 2L),
                    OrderBatchStockEnum.DEFAULT
            );
            Assertions.assertEquals(2, productsMap.size());
            Assertions.assertEquals(1, productsMap.get(1L).size());
            Assertions.assertEquals(2, productsMap.get(2L).size());
            Assertions.assertTrue(productsMap.get(1L).contains(batchStock1));
            Assertions.assertTrue(productsMap.get(2L).contains(batchStock2));
            Assertions.assertTrue(productsMap.get(2L).contains(batchStock3));
        });
    }

    @Test
    public void shouldFindBatchStockOrderedByBatchNumber() {
        Warehouse warehouse1 = createWarehouseWith1Section(1L);

        InboundOrder inboundOrder = createInboundWith2BatchStockSameProduct(2L, warehouse1.getSections().get(0));
        warehouse1.getSections().get(0).setInboundOrders(Arrays.asList(inboundOrder));

        BatchStock batchStock1 = inboundOrder.getBatchStocks().get(0);
        BatchStock batchStock2 = inboundOrder.getBatchStocks().get(1);

        Mockito.when(batchStockServiceMock.findByProductIdWithValidShelfLife(Mockito.anyLong()))
                .thenReturn(Arrays.asList(batchStock1, batchStock2));

        Assertions.assertDoesNotThrow(() -> {
            Map<Long, List<BatchStock>> productsMap = warehouseService.getProductsInStockByIds(
                    Arrays.asList(1L),
                    OrderBatchStockEnum.L
            );
            Assertions.assertEquals(1, productsMap.size());
            Assertions.assertEquals(2, productsMap.get(1L).size());
            Integer batchNumber1 = productsMap.get(1L).get(0).getBatchNumber();
            Integer batchNumber2 = productsMap.get(1L).get(1).getBatchNumber();
            Assertions.assertTrue(batchNumber1 <= batchNumber2);
        });
    }

    @Test
    public void shouldFindBatchStockOrderedByCurrentQuantity() {
        Warehouse warehouse1 = createWarehouseWith1Section(1L);

        InboundOrder inboundOrder = createInboundWith2BatchStockSameProduct(2L, warehouse1.getSections().get(0));
        warehouse1.getSections().get(0).setInboundOrders(Arrays.asList(inboundOrder));

        BatchStock batchStock1 = inboundOrder.getBatchStocks().get(0);
        BatchStock batchStock2 = inboundOrder.getBatchStocks().get(1);

        Mockito.when(batchStockServiceMock.findByProductIdWithValidShelfLife(Mockito.anyLong()))
                .thenReturn(Arrays.asList(batchStock1, batchStock2));

        Assertions.assertDoesNotThrow(() -> {
            Map<Long, List<BatchStock>> productsMap = warehouseService.getProductsInStockByIds(
                    Arrays.asList(1L),
                    OrderBatchStockEnum.C
            );
            Assertions.assertEquals(1, productsMap.size());
            Assertions.assertEquals(2, productsMap.get(1L).size());
            Integer currentQuantity1 = productsMap.get(1L).get(0).getCurrentQuantity();
            Integer currentQuantity2 = productsMap.get(1L).get(1).getCurrentQuantity();
            Assertions.assertTrue(currentQuantity1 <= currentQuantity2);
        });
    }

    @Test
    public void shouldFindBatchStockOrderedByDueDate() {
        Warehouse warehouse1 = createWarehouseWith1Section(1L);

        InboundOrder inboundOrder = createInboundWith2BatchStockSameProduct(2L, warehouse1.getSections().get(0));
        warehouse1.getSections().get(0).setInboundOrders(Arrays.asList(inboundOrder));

        BatchStock batchStock1 = inboundOrder.getBatchStocks().get(0);
        BatchStock batchStock2 = inboundOrder.getBatchStocks().get(1);

        Mockito.when(batchStockServiceMock.findByProductIdWithValidShelfLife(Mockito.anyLong()))
                .thenReturn(Arrays.asList(batchStock1, batchStock2));

        Assertions.assertDoesNotThrow(() -> {
            Map<Long, List<BatchStock>> productsMap = warehouseService.getProductsInStockByIds(
                    Arrays.asList(1L),
                    OrderBatchStockEnum.F
            );
            Assertions.assertEquals(1, productsMap.size());
            Assertions.assertEquals(2, productsMap.get(1L).size());
            LocalDate dueDate1 = productsMap.get(1L).get(0).getDueDate();
            LocalDate dueDate2 = productsMap.get(1L).get(1).getDueDate();
            Assertions.assertTrue(dueDate1.isBefore(dueDate2) || dueDate1.isEqual(dueDate2));
        });
    }

    /**
     * Métodos utilitários
     */

    private Warehouse createWarehouseWith1Section(Long warehouseId) {
        Warehouse warehouse = Warehouse.builder()
                .id(warehouseId)
                .build();
        Section section = Section.builder()
                .id(1L)
                .maximalTemperature(0.0)
                .minimalTemperature(-1.0)
                .size(500)
                .warehouse(warehouse)
                .build();
        warehouse.setSections(Arrays.asList(section));
        return warehouse;
    }

    private Warehouse createWarehouseWith2Section(Long warehouseId) {
        Warehouse warehouse = Warehouse.builder()
                .id(warehouseId)
                .build();
        List<Section> sections = Arrays.asList(
                Section.builder()
                        .id(1L)
                        .maximalTemperature(0.0)
                        .minimalTemperature(-1.0)
                        .size(500)
                        .warehouse(warehouse)
                        .build(),
                Section.builder()
                        .id(2L)
                        .maximalTemperature(-2.0)
                        .minimalTemperature(-5.0)
                        .size(500)
                        .warehouse(warehouse)
                        .build()
        );
        warehouse.setSections(sections);
        return warehouse;
    }

    private InboundOrder createInboundWith1BatchStockSameProduct(Long inboundOrderId, Section section){
        InboundOrder inboundOrder = InboundOrder.builder()
                .id(inboundOrderId)
                .section(section)
                .build();
        BatchStock batchStock = BatchStock.builder()
                .productId(1L)
                .batchNumber(123)
                .dueDate(LocalDate.of(2023, 12, 2))
                .currentQuantity(100)
                .category(CategoryENUM.FF)
                .inboundOrder(inboundOrder)
                .build();
        inboundOrder.setBatchStocks(Arrays.asList(batchStock));
        return inboundOrder;
    }

    private InboundOrder createInboundWith2BatchStockSameProduct(Long inboundOrderId, Section section){
        InboundOrder inboundOrder = InboundOrder.builder()
                .id(inboundOrderId)
                .section(section)
                .build();
        BatchStock batchStock1 = BatchStock.builder()
                .productId(1L)
                .batchNumber(124)
                .dueDate(LocalDate.of(2023, 8, 2))
                .currentQuantity(13)
                .category(CategoryENUM.FF)
                .inboundOrder(inboundOrder)
                .build();
        BatchStock batchStock2 = BatchStock.builder()
                .productId(1L)
                .batchNumber(125)
                .dueDate(LocalDate.of(2023, 5, 2))
                .currentQuantity(59)
                .category(CategoryENUM.FF)
                .inboundOrder(inboundOrder)
                .build();
        inboundOrder.setBatchStocks(Arrays.asList(batchStock1, batchStock2));
        return inboundOrder;
    }

    private InboundOrder createInboundWith2BatchStockDifferentProduct(Long inboundOrderId, Section section){
        InboundOrder inboundOrder = InboundOrder.builder()
                .id(inboundOrderId)
                .section(section)
                .build();
        BatchStock batchStock1 = BatchStock.builder()
                .productId(2L)
                .batchNumber(124)
                .dueDate(LocalDate.of(2023, 8, 2))
                .currentQuantity(13)
                .category(CategoryENUM.FF)
                .inboundOrder(inboundOrder)
                .build();
        BatchStock batchStock2 = BatchStock.builder()
                .productId(2L)
                .batchNumber(125)
                .dueDate(LocalDate.of(2023, 5, 2))
                .currentQuantity(59)
                .category(CategoryENUM.FF)
                .inboundOrder(inboundOrder)
                .build();
        inboundOrder.setBatchStocks(Arrays.asList(batchStock1, batchStock2));
        return inboundOrder;
    }
}
