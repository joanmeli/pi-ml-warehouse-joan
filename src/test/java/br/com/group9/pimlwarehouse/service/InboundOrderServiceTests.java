package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.exception.InboundOrderValidationException;
import br.com.group9.pimlwarehouse.repository.InboundOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class InboundOrderServiceTests {
    private InboundOrderService inboundOrderService;
    private WarehouseService warehouseService;
    private InboundOrderRepository inboundOrderRepository;

    @BeforeEach
    public void before() {
        this.warehouseService = Mockito.mock(WarehouseService.class);
        SectionService sectionService = Mockito.mock((SectionService.class));
        this.inboundOrderRepository = Mockito.mock(InboundOrderRepository.class);
        this.inboundOrderService = new InboundOrderService(warehouseService, sectionService, inboundOrderRepository);
    }

    @Test
    public void shouldReturnNullIfInboundOrderNotFound(){
        Mockito.when(inboundOrderRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertNull(inboundOrderService.get(any(Long.class)));
    }

    @Test
    public void shouldReturnInboundOrderIfItExists(){
        Optional<InboundOrder> inboundOrder = Optional.of(new InboundOrder());
        Mockito.when(inboundOrderRepository.findById(any(Long.class))).thenReturn(inboundOrder);
        assertNotNull(inboundOrderService.get(any(Long.class)));
        assertEquals(inboundOrder.get(), inboundOrderService.get(any(Long.class)));
    }

    @Test
    public void shouldThrowInboundOrderValidationExceptionWhenWarehouseDoesNotExists(){
        List<BatchStock> batchStocks = Collections.emptyList();
        Mockito.when(warehouseService.exists(any(Long.class))).thenReturn(false);
        InboundOrderValidationException exception = assertThrows(
                InboundOrderValidationException.class,
                () -> inboundOrderService.validateInboundOrder(1L, 1L, 1L, batchStocks)
        );
        assertEquals("WAREHOUSE_NOT_FOUND", exception.getMessage());
    }

    @Test
    public void shouldThrowInboundOrderValidationExceptionWhenInboundOrderAlreadyExists(){
        Optional<InboundOrder> inboundOrder = Optional.of(new InboundOrder());
        Mockito.when(inboundOrderRepository.findById(any(Long.class))).thenReturn(inboundOrder);
        InboundOrderValidationException exception = assertThrows(
                InboundOrderValidationException.class,
                () -> inboundOrderService.validateExistence(1L)
        );
        assertEquals("INBOUND_ORDER_ALREADY_EXISTS", exception.getMessage());
    }

}
