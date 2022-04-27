package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.entity.Section;
import br.com.group9.pimlwarehouse.entity.SectionProduct;
import br.com.group9.pimlwarehouse.exception.SectionNotFoundException;
import br.com.group9.pimlwarehouse.exception.SectionProductNotFoundException;
import br.com.group9.pimlwarehouse.repository.SectionRepository;
import org.springframework.stereotype.Service;

@Service
public class SectionService {
    private SectionRepository sectionRepository;
    private SectionProductService sectionProductService;
    private ProductAPIService productAPIService;

    public SectionService(SectionRepository sectionRepository, SectionProductService sectionProductService, ProductAPIService productAPIService) {
        this.sectionRepository = sectionRepository;
        this.sectionProductService = sectionProductService;
        this.productAPIService = productAPIService;
    }

    public Section associateProductToSectionByIds(Long sectionId, Long productId) {
        Section foundSection = this.sectionRepository.findById(sectionId)
                .orElseThrow(() -> new SectionNotFoundException("SECTION_NOT_FOUND"));

        this.productAPIService.fetchProductById(productId);

        SectionProduct newSectionProduct = SectionProduct.builder()
                .section(foundSection)
                .productId(productId)
                .build();
        if(this.sectionProductService.exists(newSectionProduct))
            throw new SectionProductNotFoundException("SECTION_PRODUCT_NOT_FOUND");
        foundSection.addSectionProduct(newSectionProduct);

        return this.sectionRepository.save(foundSection);
    }

}
