package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.entity.Section;
import br.com.group9.pimlwarehouse.repository.SectionRepository;
import org.springframework.stereotype.Service;

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
}
