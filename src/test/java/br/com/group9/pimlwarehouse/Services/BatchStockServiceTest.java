package br.com.group9.pimlwarehouse.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.exception.InboundOrderValidationException;
import br.com.group9.pimlwarehouse.repository.BatchStockRepository;
import br.com.group9.pimlwarehouse.repository.SectionRepository;
import br.com.group9.pimlwarehouse.service.BatchStockService;
import br.com.group9.pimlwarehouse.service.ProductAPIService;
import br.com.group9.pimlwarehouse.service.SectionProductService;
import br.com.group9.pimlwarehouse.service.SectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

public class BatchStockServiceTest {
    private BatchStockRepository batchStockRepository;
    private BatchStockService batchStockService;

    @BeforeEach
    public void before() {
        this.batchStockRepository = Mockito.mock(BatchStockRepository.class);
        SectionService sectionService = new SectionService(
                Mockito.mock(SectionRepository.class),
                Mockito.mock(SectionProductService.class),
                Mockito.mock(ProductAPIService.class)
        );
        this.batchStockService = new BatchStockService(batchStockRepository, sectionService);
    }

    @Test
    public void shouldSaveAListOfBatchStocks(){
        List<BatchStock> batchStockList = new ArrayList<BatchStock>();
        batchStockList.add(new BatchStock());
        batchStockList.add(new BatchStock());
        batchStockList.add(new BatchStock());

        Mockito.when(batchStockRepository.save(any(BatchStock.class))).thenReturn(new BatchStock());

        List<BatchStock> savedBatchStockList = batchStockService.save(batchStockList);
        assertEquals(3, savedBatchStockList.size());
    }
    @Test
    public void shouldReturnBatchStocksByProductId(){
        List<BatchStock> batchStockList = new ArrayList<BatchStock>();
        batchStockList.add(new BatchStock());
        batchStockList.add(new BatchStock());
        batchStockList.add(new BatchStock());

        Mockito.when(batchStockRepository.findByProductId(any(Long.class))).thenReturn(batchStockList);

        List<BatchStock> savedBatchStockList = batchStockService.findByProductId(1L);
        assertEquals(3, savedBatchStockList.size());
    }

    @Test
    public void shouldUpdateBatchStockInitialQuantity(){

        List<BatchStock> newBatchStocks = new ArrayList<>();
        newBatchStocks.add(BatchStock.builder().initialQuantity(121).build());
        newBatchStocks.add(BatchStock.builder().initialQuantity(122).build());
        newBatchStocks.add(BatchStock.builder().initialQuantity(123).build());
        List<BatchStock> oldBatchStocks = new ArrayList<>();
        oldBatchStocks.add(BatchStock.builder().initialQuantity(121).build());
        oldBatchStocks.add(BatchStock.builder().initialQuantity(221).build());
        oldBatchStocks.add(BatchStock.builder().initialQuantity(321).build());

        InboundOrder order = InboundOrder.builder().batchStocks(newBatchStocks).build();

        assertThrows(InboundOrderValidationException.class, () -> batchStockService.update(newBatchStocks, null));
    }
}
