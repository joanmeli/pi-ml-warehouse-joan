package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.entity.Section;
import br.com.group9.pimlwarehouse.entity.Warehouse;
import br.com.group9.pimlwarehouse.exceptions.InboundOrderValidationException;
import br.com.group9.pimlwarehouse.repository.SectionRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class SectionService {
    private SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public boolean exists(Long id) {
        Optional<Section> op = this.sectionRepository.findById(id);
        return op.isPresent();
    }

    public Optional<Section> get(Long id) {
        return this.sectionRepository.findById(id);
    }

    public Long getAvaliableSpace(Section section){
        List<InboundOrder> inboundOrders = section.getInboundOrders();
        Long currentSize = 0L;
        inboundOrders.stream().map(
                order -> order.getBatchStocks().stream().map(batchStock ->
                        currentSize += batchStock.getProductSize()
                )
        );
    }

    public void validateSection(Long sectorId) {
        Optional<Section>  sectionOptional = get(sectorId);
        if (sectionOptional.isEmpty()){
            throw new InboundOrderValidationException("SECTION_NOT_FOUND");
        }
        Section section = sectionOptional.get();
        Long avaliableSpace = getAvaliableSpace(section);
    }
}
