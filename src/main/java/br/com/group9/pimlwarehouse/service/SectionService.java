package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.dto.ProductDTO;
import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.entity.Section;
import br.com.group9.pimlwarehouse.entity.SectionProduct;
import br.com.group9.pimlwarehouse.exception.ProductDoesNotMatchSectionException;
import br.com.group9.pimlwarehouse.exception.SectionNotFoundException;
import br.com.group9.pimlwarehouse.exception.SectionProductNotFoundException;
import br.com.group9.pimlwarehouse.exception.InboundOrderValidationException;
import br.com.group9.pimlwarehouse.repository.SectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Section findById(Long id) {
        return this.sectionRepository.findById(id)
                .orElseThrow(() -> new SectionNotFoundException("SECTION_NOT_FOUND"));
    }

    private Double getTotalBatchSize(List<BatchStock> batchStocks){
        return batchStocks.stream().map(
                e -> e.getProductSize() * e.getCurrentQuantity()
        ).mapToDouble(Double::doubleValue).sum();
    }


    public Double getAvailableSpace(Section section){
        List<InboundOrder> inboundOrders = section.getInboundOrders();
        Double occupiedSpace = inboundOrders.stream().map(
                order -> getTotalBatchSize(order.getBatchStocks())
        ).mapToDouble(Double::doubleValue).sum();

        return section.getSize()-occupiedSpace;

    }

    public void validateBatchStocksBySection(Long sectorId, Long warehouseId, List<BatchStock> batchStocks) {
        Section section = findById(sectorId);

        if(section.getWarehouse().getId() != warehouseId)
            throw new InboundOrderValidationException("SECTION_WAREHOUSE_DOES_NOT_MATCH");

        boolean productMatchesSection = batchStocks.stream()
                .map(b -> b.getProductId())
                .anyMatch(prodId ->
                        section.getSectionProducts().stream()
                                .filter(sectionProduct -> sectionProduct.getProductId() == prodId)
                                .findFirst().orElse(null) == null
                );
        if(productMatchesSection)
            throw new InboundOrderValidationException("PRODUCT_SECTION_DOES_NOT_MATCH");

        Double availableSpace = getAvailableSpace(section);
        Double requiredSpace = getTotalBatchSize(batchStocks);
        if (requiredSpace > availableSpace){
            throw new InboundOrderValidationException("SECTION_SPACE_NOT_ENOUGH");
        }
    }

    public void validateProductToSectionAssociation(ProductDTO productDTO, Section section) {
        if(productDTO.getMinimumTemperature() < section.getMinimalTemperature())
            throw new ProductDoesNotMatchSectionException("EXCEEDING_SECTION_MINIMUM_TEMPERATURE");
        if(productDTO.getMinimumTemperature() > section.getMaximalTemperature())
            throw new ProductDoesNotMatchSectionException("INFERIOR_SECTION_MAXIMUM_TEMPERATURE");
    }

    public Section associateProductToSectionByIds(Long sectionId, Long productId) {
        Section foundSection = this.sectionRepository.findById(sectionId)
                .orElseThrow(() -> new SectionNotFoundException("SECTION_NOT_FOUND"));

        ProductDTO foundProductDTO = this.productAPIService.fetchProductById(productId);

        validateProductToSectionAssociation(foundProductDTO, foundSection);

        SectionProduct newSectionProduct = SectionProduct.builder()
                .section(foundSection)
                .productId(productId)
                .build();
        if(this.sectionProductService.exists(newSectionProduct))
            throw new SectionProductNotFoundException("SECTION_PRODUCT_ALREADY_ASSOCIATED");
        foundSection.addSectionProduct(newSectionProduct);

        return this.sectionRepository.save(foundSection);

    }
}
