package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.entity.Section;
import br.com.group9.pimlwarehouse.entity.SectionProduct;
import br.com.group9.pimlwarehouse.repository.SectionProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class SectionProductServiceTest {
    private SectionProductService sectionProductService;
    private SectionProductRepository sectionProductRepositoryMock;

    @BeforeEach
    public void before(){
        sectionProductService = Mockito.mock(SectionProductService.class);
        sectionProductRepositoryMock = Mockito.mock(SectionProductRepository.class);
        sectionProductService = new SectionProductService(sectionProductRepositoryMock);

    }

    @Test
    public void shouldCountHowManyTimesCallProductIdIsAssociatedWithASection(){
        Section validSections =
                Section.builder()
                        .id(1L)
                        .maximalTemperature(0.0)
                        .minimalTemperature(-1.0)
                        .size(500)
                        .build();

        SectionProduct validSectionProduct = SectionProduct.builder()
                .section(validSections)
                .productId(1L)
                .build();

        Assertions.assertDoesNotThrow(() ->{
            sectionProductService.exists(validSectionProduct);
        });

        Mockito.verify(sectionProductRepositoryMock, Mockito.times(1))
                .existsBySectionIdAndProductId(
                        validSectionProduct.getSection().getId(), validSectionProduct.getProductId()
                );

    }


}
