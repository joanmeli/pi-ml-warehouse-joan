package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.entity.Section;
import br.com.group9.pimlwarehouse.exceptions.InboundOrderValidationException;
import br.com.group9.pimlwarehouse.repository.SectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SectionService {
    private SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public Optional<Section> get(Long id) {
        return this.sectionRepository.findById(id);
    }

    public Long getAvailableSpace(Section section){
        List<InboundOrder> inboundOrders = section.getInboundOrders();
        Long occupiedSpace = inboundOrders.stream().map(
                order -> order.getBatchStocks().stream().map(
                        e -> e.getProductSize() * e.getCurrentQuantity()
                ).mapToLong(Long::longValue).sum()
        ).mapToLong(Long::longValue).sum();

        return section.getSize()-occupiedSpace;

    }

    public void validateSection(Long sectorId, List<BatchStock> batchStocks) {
        Optional<Section>  sectionOptional = get(sectorId);
        if (sectionOptional.isEmpty()){
            throw new InboundOrderValidationException("SECTION_NOT_FOUND");
        }

        Section section = sectionOptional.get();
        Long availableSpace = getAvailableSpace(section);
        long requiredSpace = batchStocks.stream().map(
                e -> e.getProductSize() * e.getCurrentQuantity()
        ).mapToLong(Long::longValue).sum();
        System.out.println(availableSpace);
        if (requiredSpace > availableSpace){
            throw new InboundOrderValidationException("SECTION_SPACE_NOT_ENOUGH");
        }

    }
}
