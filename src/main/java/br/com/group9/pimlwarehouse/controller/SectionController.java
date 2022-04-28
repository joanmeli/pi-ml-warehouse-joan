package br.com.group9.pimlwarehouse.controller;

import br.com.group9.pimlwarehouse.dto.SectionDTO;
import br.com.group9.pimlwarehouse.dto.SectionProductDTO;
import br.com.group9.pimlwarehouse.dto.WarehouseDTO;
import br.com.group9.pimlwarehouse.entity.Section;
import br.com.group9.pimlwarehouse.entity.Warehouse;
import br.com.group9.pimlwarehouse.service.SectionService;
import br.com.group9.pimlwarehouse.service.WarehouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
            @Valid @RequestBody SectionProductDTO sectionProductDTO,
            UriComponentsBuilder uriBuilder) {
        Section newSection = sectionService.associateProductToSectionByIds(sectionId, sectionProductDTO.getProductId());
        SectionDTO resultSection = SectionDTO.map(newSection);
        URI uri = uriBuilder
                .path(BASE_PATH.concat("/{id}"))
                .buildAndExpand(resultSection.getId())
                .toUri();
        return ResponseEntity.created(uri).body(resultSection);
    }

    @GetMapping(BASE_PATH + "/{sectionId}")
    public ResponseEntity<SectionDTO> findSection(@PathVariable(name = "sectionId") Long sectionId) {
        Section foundSection = this.sectionService.findById(sectionId);
        return ResponseEntity.ok(SectionDTO.map(foundSection));
    }
}
