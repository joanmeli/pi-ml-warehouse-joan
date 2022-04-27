package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.entity.Section;
import br.com.group9.pimlwarehouse.entity.SectionProduct;
import br.com.group9.pimlwarehouse.repository.SectionRepository;
import org.springframework.stereotype.Service;

@Service
public class SectionService {
    private SectionRepository sectionRepository;
    private SectionProductService sectionProductService;

    public SectionService(SectionRepository sectionRepository, SectionProductService sectionProductService) {
        this.sectionRepository = sectionRepository;
        this.sectionProductService = sectionProductService;
    }

    public Section associateProductToSectionByIds(Long sectionId, Long productId) {
        Section foundSection = this.sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Setor não encontrado!"));

        // TODO: 26/04/22 Include call with Product API to validate if the given Product exists by productID.

        SectionProduct newSectionProduct = SectionProduct.builder()
                .section(foundSection)
                .productId(productId)
                .build();
        if(this.sectionProductService.exists(newSectionProduct))
            throw new RuntimeException("Produto já associado ao setor!");
        foundSection.addSectionProduct(newSectionProduct);

        return this.sectionRepository.save(foundSection);
    }

}
