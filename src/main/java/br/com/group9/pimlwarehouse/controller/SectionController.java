package br.com.group9.pimlwarehouse.controller;

import br.com.group9.pimlwarehouse.dto.SectionDTO;
import br.com.group9.pimlwarehouse.dto.SectionProductDTO;
import br.com.group9.pimlwarehouse.dto.WarehouseDTO;
import br.com.group9.pimlwarehouse.entity.Section;
import br.com.group9.pimlwarehouse.entity.Warehouse;
import br.com.group9.pimlwarehouse.service.SectionService;
import br.com.group9.pimlwarehouse.service.WarehouseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class SectionController extends APIController{
    private static final String BASE_PATH = "/section";
    private SectionService sectionService;
    private WarehouseService warehouseService;

    public SectionController(SectionService sectionService, WarehouseService warehouseService) {
        this.sectionService = sectionService;
        this.warehouseService = warehouseService;
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
        var sectionDTO = SectionDTO.map(foundSection);
        sectionDTO.setAvailableSpace(sectionService.getAvailableSpace(foundSection));
        return ResponseEntity.ok(sectionDTO);
    }

    /**
     * POST method to create a section in a warehouse.
     * @param warehouseId Id of Warehouse where section will belong to.
     * @param uriBuilder Injection used by Spring to send the location.
     * @return URI of Section on header location, the entity response with status code "201-Created" and create a section in a Warehouse.
     */
    @PostMapping(BASE_PATH + "/{warehouseId}")
    public ResponseEntity<SectionDTO> associateProductToSection(
            @PathVariable Long warehouseId,
            @Valid @RequestBody SectionDTO sectionDTO,
            UriComponentsBuilder uriBuilder
    ) {
        Section section = sectionDTO.map();
        Warehouse warehouse = warehouseService.findById(warehouseId);
        Section savedSection = sectionService.save(section, warehouse);
        SectionDTO resultSection = SectionDTO.simpleMap(savedSection);
        resultSection.setAvailableSpace(Double.valueOf(sectionDTO.getSize()));
        URI uri = uriBuilder
                .path(BASE_PATH.concat("/{id}"))
                .buildAndExpand(section.getId())
                .toUri();
        return ResponseEntity.created(uri).body(resultSection);
    }

    /**
     * DELETE method to delete a section.
     * @param sectionId Id of Section that will be deleted.
     */
    @DeleteMapping(BASE_PATH + "/{sectionId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteSections(@PathVariable Long sectionId) {
        Section section = sectionService.findById(sectionId);
        sectionService.delete(section);
    }
}
