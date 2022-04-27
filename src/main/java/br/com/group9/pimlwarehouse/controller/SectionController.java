package br.com.group9.pimlwarehouse.controller;

import br.com.group9.pimlwarehouse.dto.SectionDTO;
import br.com.group9.pimlwarehouse.dto.SectionProductDTO;
import br.com.group9.pimlwarehouse.dto.WarehouseDTO;
import br.com.group9.pimlwarehouse.entity.Section;
import br.com.group9.pimlwarehouse.entity.Warehouse;
import br.com.group9.pimlwarehouse.service.SectionService;
import br.com.group9.pimlwarehouse.service.WarehouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class SectionController extends APIController{
    private static final String BASE_PATH = "/section";
    private SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping(BASE_PATH + "/{sectionId}/product")
    public ResponseEntity<SectionDTO> associateProductToSection(
            @PathVariable(name = "sectionId") Long sectionId,
            @Valid @RequestBody SectionProductDTO sectionProductDTO) {
        Section newSection = sectionService.associateProductToSectionByIds(sectionId, sectionProductDTO.getProductId());
        SectionDTO result = SectionDTO.map(newSection);
        return ResponseEntity.created(null).body(result);
    }
}
