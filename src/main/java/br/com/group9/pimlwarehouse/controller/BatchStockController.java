package br.com.group9.pimlwarehouse.controller;

import br.com.group9.pimlwarehouse.dto.BatchStockByDuaDateDTO;
import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.enums.CategoryENUM;
import br.com.group9.pimlwarehouse.service.BatchStockService;
import br.com.group9.pimlwarehouse.service.SectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BatchStockController  extends APIController{
    BatchStockService batchStockService;
    SectionService sectionService;

    public BatchStockController(
            BatchStockService batchStockService,
            SectionService sectionService
    ){
        this.batchStockService = batchStockService;
        this.sectionService = sectionService;
    }

    @GetMapping("/fresh-products/due-date")
    public ResponseEntity<List<BatchStockByDuaDateDTO>> findProductsByDueDate(
            @RequestParam (required = false) Long sectionId,
            @RequestParam Long days,
            @RequestParam(required = false) CategoryENUM category
    ) {

        List<BatchStock> product = batchStockService.getAllBatchesByDueDate(sectionId, days, category);
        return ResponseEntity.ok(BatchStockByDuaDateDTO.convert(product));
    }
}
