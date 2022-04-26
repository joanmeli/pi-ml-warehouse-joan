package br.com.group9.pimlwarehouse.entity;

import java.time.LocalDateTime;

public class InboundOrder {
    Long id;
    Long sectionId;
    Long batchStockId;
    LocalDateTime orderDate;
}
