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

    /**
     * POST method to associate a product to a section.
     * @param sectionId get section Id.
     * @param sectionProductDTO associate the product with a section according to its Id.
     * @param uriBuilder Injection used by Spring to send the location.
     * @return URI of InboundOrder on header location, the entity response with status code "201-Created" and associate the product with a section.
     */
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

    /**
     * GET method to found a section according to Id
     * @param sectionId get section Id
     * @return section of the informed ID
     */
    @GetMapping(BASE_PATH + "/{sectionId}")
    public ResponseEntity<SectionDTO> findSection(@PathVariable(name = "sectionId") Long sectionId) {
        Section foundSection = this.sectionService.findById(sectionId);
        return ResponseEntity.ok(SectionDTO.map(foundSection));
    }
}
