package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.entity.Section;
import br.com.group9.pimlwarehouse.enums.CategoryENUM;
import br.com.group9.pimlwarehouse.exception.BatchStockWithdrawException;
import br.com.group9.pimlwarehouse.exception.InboundOrderValidationException;
import br.com.group9.pimlwarehouse.repository.BatchStockRepository;
import br.com.group9.pimlwarehouse.service.BatchStockService;
import br.com.group9.pimlwarehouse.service.SectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class BatchStockServiceTest {
    private BatchStockRepository batchStockRepository;
    private BatchStockService batchStockService;
    private SectionService sectionService;

    @BeforeEach
    public void before() {
        this.batchStockRepository = Mockito.mock(BatchStockRepository.class);
        this.sectionService = Mockito.mock(SectionService.class);
        this.batchStockService = new BatchStockService(batchStockRepository, sectionService);
    }

    @Test
    public void shouldSaveAListOfBatchStocks(){
        List<BatchStock> batchStockList = new ArrayList<>();
        batchStockList.add(new BatchStock());
        batchStockList.add(new BatchStock());
        batchStockList.add(new BatchStock());

        Mockito.when(batchStockRepository.save(any(BatchStock.class))).thenReturn(new BatchStock());

        List<BatchStock> savedBatchStockList = batchStockService.save(batchStockList);
        assertEquals(3, savedBatchStockList.size());
    }
    @Test
    public void shouldReturnBatchStocksByProductId(){
        List<BatchStock> batchStockList = new ArrayList<>();
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
        newBatchStocks.add(BatchStock.builder().batchNumber(1).currentQuantity(121).build());
        newBatchStocks.add(BatchStock.builder().batchNumber(2).currentQuantity(122).build());
        newBatchStocks.add(BatchStock.builder().batchNumber(3).currentQuantity(123).build());
        List<BatchStock> oldBatchStocks = new ArrayList<>();
        oldBatchStocks.add(BatchStock.builder().batchNumber(1).currentQuantity(121).build());
        oldBatchStocks.add(BatchStock.builder().batchNumber(2).currentQuantity(221).build());
        oldBatchStocks.add(BatchStock.builder().batchNumber(3).currentQuantity(321).build());

        InboundOrder order = InboundOrder.builder().batchStocks(oldBatchStocks).build();

        assertThrows(InboundOrderValidationException.class, () -> batchStockService.update(newBatchStocks, null));
        assertDoesNotThrow(() -> batchStockService.update(newBatchStocks, order));

        order.getBatchStocks().remove(1);
        assertThrows(InboundOrderValidationException.class, () -> batchStockService.update(newBatchStocks, order));

    }

    @Test
    public void shouldReturnProductByDueDate() {

        BatchStock batchStock1 = BatchStock.builder().batchNumber(1).dueDate(LocalDate.now().plusMonths(2)).build();
        BatchStock batchStock2 = BatchStock.builder().batchNumber(2).dueDate(LocalDate.now().plusMonths(3)).build();
        BatchStock batchStock3 = BatchStock.builder().batchNumber(3).dueDate(LocalDate.now().plusMonths(4)).build();

        List<BatchStock> orderedBatchStocks = new ArrayList<>();
        orderedBatchStocks.add(batchStock1);
        orderedBatchStocks.add(batchStock2);
        orderedBatchStocks.add(batchStock3);

        List<BatchStock> unorderedBatchStocks = new ArrayList<>();
        unorderedBatchStocks.add(batchStock3);
        unorderedBatchStocks.add(batchStock1);
        unorderedBatchStocks.add(batchStock2);

        Mockito.when(batchStockRepository.findByDueDateBetweenAndCategory(
                any(LocalDate.class), any(LocalDate.class), any(CategoryENUM.class))
        ).thenReturn(unorderedBatchStocks);

        List<BatchStock> batchStockList = batchStockService.getAllBatchesByDueDate(null, 30L, CategoryENUM.FF);
        assertEquals(orderedBatchStocks, batchStockList);


        List<InboundOrder> inboundOrders = Collections.singletonList(
                InboundOrder.builder().batchStocks(unorderedBatchStocks).build()
        );
        Section section = Section.builder().inboundOrders(inboundOrders).build();
        Mockito.when(sectionService.findById(any(Long.class))).thenReturn(section);

        Mockito.when(batchStockRepository.findByDueDateBetweenAndInboundOrder(
                any(LocalDate.class), any(LocalDate.class), any(InboundOrder.class))
        ).thenReturn(unorderedBatchStocks);

        batchStockList = batchStockService.getAllBatchesByDueDate(1L, 30L, null);
        assertEquals(orderedBatchStocks, batchStockList);
    }

    @Test
    public void shouldWithdrawStockByProductId() {

        List<BatchStock> batchStock1 = Collections.singletonList(BatchStock.builder().currentQuantity(9).productId(1L).build());
        List<BatchStock> batchStock2 = Collections.singletonList(BatchStock.builder().currentQuantity(10).productId(1L).build());
        List<BatchStock> batchStock3 = Collections.singletonList(BatchStock.builder().currentQuantity(10).productId(1L).build());

        Map<Long, Integer> quantityByProductMap = new HashMap<>();
        quantityByProductMap.put(1L, 10);
        quantityByProductMap.put(2L, 10);
        quantityByProductMap.put(3L, 10);

        Mockito.when(batchStockService.findByProductIdWithValidShelfLife(1L)).thenReturn(batchStock1);
        Mockito.when(batchStockService.findByProductIdWithValidShelfLife(2L)).thenReturn(batchStock2);
        Mockito.when(batchStockService.findByProductIdWithValidShelfLife(3L)).thenReturn(batchStock3);

        assertThrows(BatchStockWithdrawException.class, () -> batchStockService.withdrawStockByProductId(quantityByProductMap));

        quantityByProductMap.remove(1L);
        List<BatchStock> batchStockList = List.of(batchStock2.get(0), batchStock3.get(0));
        Mockito.when(batchStockRepository.saveAll(batchStock2)).thenReturn(batchStock2);
        Mockito.when(batchStockRepository.saveAll(batchStock3)).thenReturn(batchStock3);

        List<BatchStock> batchStocks = batchStockService.withdrawStockByProductId(quantityByProductMap);
        assertEquals(batchStocks, batchStockList);
        assertEquals(batchStocks.get(0).getCurrentQuantity(), 0);
        assertEquals(batchStocks.get(1).getCurrentQuantity(), 0);




    }
}
