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

    /**
     * Search if the productId already associated in Section informed.
     * @param sectionProduct receives a newSectionProduct where it has a productId and Section.
     * @return a boolean if this association exists or not exists.
     */
    public boolean exists(SectionProduct sectionProduct) {
        return this.sectionProductRepository.existsBySectionIdAndProductId(
                sectionProduct.getSection().getId(), sectionProduct.getProductId()
        );
    }
}
