package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.dto.ProductDTO;
import br.com.group9.pimlwarehouse.entity.*;
import br.com.group9.pimlwarehouse.repository.SectionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;
import java.util.stream.Collectors;

public class SectionServiceTests {
    private SectionService sectionService;
    private SectionRepository sectionRepositoryMock;
    private SectionProductService sectionProductServiceMock;
    private ProductAPIService productAPIServiceMock;

    @BeforeEach
    public void setupService() {
        sectionRepositoryMock = Mockito.mock(SectionRepository.class);
        sectionProductServiceMock = Mockito.mock(SectionProductService.class);
        productAPIServiceMock = Mockito.mock(ProductAPIService.class);
        sectionService = new SectionService(sectionRepositoryMock, sectionProductServiceMock, productAPIServiceMock);
    }


    /**
     * Métodos de SectionService:
     *  - public Section findById(Long id)
     *  - public void validateBatchStocksBySection(Long sectorId, Long warehouseId, List<BatchStock> batchStocks)
     *  - public Section associateProductToSectionByIds(Long sectionId, Long productId)
     */

    /**
     *  - public Section findById(Long id)
     *      Cenários:
     *          -Section encontrada com sucesso.
     *          -Section não é encontrada (lança exceção)
     */

    @Test
    public void shouldFindWhenSectionIsFound() {
        Section section = Section.builder().id(1L).build();

        Mockito.when(sectionRepositoryMock.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(section));

        Assertions.assertDoesNotThrow(() -> {
            Section foundSection = sectionService.findById(1L);
            Assertions.assertEquals(section, foundSection);
        });
    }

    @Test
    public void shouldThrowExceptionWhenSectionNotFound() {
        Mockito.when(sectionRepositoryMock.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () -> {
            sectionService.findById(1L);
        });

        Assertions.assertEquals("SECTION_NOT_FOUND", runtimeException.getMessage());
    }

    /**
     *  - public void validateBatchStocksBySection(Long sectorId, Long warehouseId, List<BatchStock> batchStocks)
     *      Cenários:
     *          -Valida Section com sucesso.
     *          -Section não está associado a Warehouse (lança exceção).
     *          -Produto do BatchStock não está associado a Section (lança exceção).
     *          -Section não possui espaço o suficiente (lança exceção).
     */

    @Test
    public void shouldSuccessfullyValidateWhenValidSection() {
        Section section = createValidSection();
        List<BatchStock> validBatchStocks = createValidBatchStocks();

        Mockito.when(sectionRepositoryMock.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(section));

        Assertions.assertDoesNotThrow(() -> {
            sectionService.validateBatchStocksBySection(1L, 1L, validBatchStocks);
        });
    }

    @Test
    public void shouldThrowExceptionWhenWarehouseDoesNotMatchSection() {
        Section section = createValidSection();

        Mockito.when(sectionRepositoryMock.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(section));

        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () -> {
            sectionService.validateBatchStocksBySection(1L, 2L, Arrays.asList());
        });

        Assertions.assertEquals("SECTION_WAREHOUSE_DOES_NOT_MATCH", runtimeException.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenProductDoesNotMatchSection() {
        Section section = createValidSection();
        List<BatchStock> invalidBatchStocks = createValidBatchStocks().stream()
                .map(b -> {
                    b.setProductId(2L);
                    return b;
                }).collect(Collectors.toList());

        Mockito.when(sectionRepositoryMock.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(section));

        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () -> {
            sectionService.validateBatchStocksBySection(1L, 1L, invalidBatchStocks);
        });

        Assertions.assertEquals("PRODUCT_SECTION_DOES_NOT_MATCH", runtimeException.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenSectionSpaceNotEnough() {
        Section section = createValidSection();
        List<BatchStock> invalidBatchStocks = createValidBatchStocks().stream()
                .map(b -> {
                    b.setProductSize(2.1);
                    return b;
                }).collect(Collectors.toList());

        Mockito.when(sectionRepositoryMock.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(section));

        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () -> {
            sectionService.validateBatchStocksBySection(1L, 1L, invalidBatchStocks);
        });

        Assertions.assertEquals("SECTION_SPACE_NOT_ENOUGH", runtimeException.getMessage());
    }

    /**
     *  - public Section associateProductToSectionByIds(Long sectionId, Long productId)
     *      Cenários:
     *          -Associa produto à Section com sucesso.
     *          -Temperatura mínima do produto é maior que máxima da Section (lança exceção).
     *          -Temperatura mínima do produto é menor que mínima da Section (lança exceção).
     *          -Associação já foi criada anteriormente (lança exceção).
     */

    @Test
    public void shouldSuccessfullyAssociateWhenValidProductAndSection() {
        Section section = createValidSection();
        ProductDTO foundProduct = ProductDTO.builder().id(2L).minimumTemperature(1.0).build();

        Mockito.when(sectionRepositoryMock.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(section));
        Mockito.when(productAPIServiceMock.fetchProductById(foundProduct.getId()))
                .thenReturn(foundProduct);
        Mockito.when(sectionProductServiceMock.exists(Mockito.any()))
                .thenReturn(false);
        Mockito.when(sectionRepositoryMock.save(Mockito.any()))
                .thenReturn(Mockito.any());

        Assertions.assertDoesNotThrow(() -> {
            sectionService.associateProductToSectionByIds(1L, 2L);
        });
    }

    @Test
    public void shouldThrowExceptionWhenMinimumProductTemperatureExceedsSectionMaximum() {
        Section section = createValidSection();
        ProductDTO foundProduct = ProductDTO.builder().id(2L).minimumTemperature(3.0).build();

        Mockito.when(sectionRepositoryMock.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(section));
        Mockito.when(productAPIServiceMock.fetchProductById(foundProduct.getId()))
                .thenReturn(foundProduct);
        Mockito.when(sectionProductServiceMock.exists(Mockito.any()))
                .thenReturn(false);
        Mockito.when(sectionRepositoryMock.save(Mockito.any()))
                .thenReturn(Mockito.any());

        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () -> {
            sectionService.associateProductToSectionByIds(1L, 2L);
        });

        Assertions.assertEquals("INFERIOR_SECTION_MAXIMUM_TEMPERATURE", runtimeException.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenMinimumProductTemperatureIsLowerThanSectionMinimum() {
        Section section = createValidSection();
        ProductDTO foundProduct = ProductDTO.builder().id(2L).minimumTemperature(-0.1).build();

        Mockito.when(sectionRepositoryMock.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(section));
        Mockito.when(productAPIServiceMock.fetchProductById(foundProduct.getId()))
                .thenReturn(foundProduct);
        Mockito.when(sectionProductServiceMock.exists(Mockito.any()))
                .thenReturn(false);
        Mockito.when(sectionRepositoryMock.save(Mockito.any()))
                .thenReturn(Mockito.any());

        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () -> {
            sectionService.associateProductToSectionByIds(1L, 2L);
        });

        Assertions.assertEquals("EXCEEDING_SECTION_MINIMUM_TEMPERATURE", runtimeException.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenAssociationAlreadyExists() {
        Section section = createValidSection();
        ProductDTO foundProduct = ProductDTO.builder().id(2L).minimumTemperature(0.1).build();

        Mockito.when(sectionRepositoryMock.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(section));
        Mockito.when(productAPIServiceMock.fetchProductById(foundProduct.getId()))
                .thenReturn(foundProduct);
        Mockito.when(sectionProductServiceMock.exists(Mockito.any()))
                .thenReturn(true);
        Mockito.when(sectionRepositoryMock.save(Mockito.any()))
                .thenReturn(Mockito.any());

        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () -> {
            sectionService.associateProductToSectionByIds(1L, 2L);
        });

        Assertions.assertEquals("SECTION_PRODUCT_ALREADY_ASSOCIATED", runtimeException.getMessage());
    }

    /**
     * Métodos utilitários
     */

    private Section createValidSection() {
        Warehouse warehouse = Warehouse.builder()
                .id(1L)
                .build();
        SectionProduct sectionProduct = SectionProduct.builder()
                .id(1L)
                .productId(1L)
                .build();
        InboundOrder inboundOrder = InboundOrder.builder()
                .batchStocks(new ArrayList<>(Collections.singletonList(
                        BatchStock.builder().productSize(2.0).currentQuantity(0).build()
                )))
                .build();
        Section section = Section.builder()
                .size(60)
                .minimalTemperature(0.0)
                .maximalTemperature(2.0)
                .warehouse(warehouse)
                .sectionProducts(new ArrayList<>(Collections.singletonList(sectionProduct)))
                .inboundOrders(new ArrayList<>(Collections.singletonList(inboundOrder)))
                .build();
        warehouse.setSections(new ArrayList<>(Collections.singletonList(section)));
        sectionProduct.setSection(section);
        inboundOrder.setSection(section);
        return section;
    }

    private List<BatchStock> createValidBatchStocks() {
        return new ArrayList<>(Arrays.asList(
                BatchStock.builder()
                        .productId(1L)
                        .productSize(2.0)
                        .currentQuantity(10)
                        .build(),
                BatchStock.builder()
                        .productId(1L)
                        .productSize(2.0)
                        .currentQuantity(10)
                        .build(),
                BatchStock.builder()
                        .productId(1L)
                        .productSize(2.0)
                        .currentQuantity(10)
                        .build()
        ));
    }

}
