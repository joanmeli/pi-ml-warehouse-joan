package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.entity.SectionProduct;
import br.com.group9.pimlwarehouse.repository.SectionProductRepository;
import org.springframework.stereotype.Service;

@Service
public class SectionProductService {
    private SectionProductRepository sectionProductRepository;

    public SectionProductService(SectionProductRepository sectionProductRepository) {
        this.sectionProductRepository = sectionProductRepository;
    }

    public boolean exists(SectionProduct sectionProduct) {
        return this.sectionProductRepository.existsBySectionIdAndProductId(sectionProduct.getSection().getId(), sectionProduct.getProductId());
    }
}
