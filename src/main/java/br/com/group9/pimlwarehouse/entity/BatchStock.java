package br.com.group9.pimlwarehouse.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BatchStock {
    Long Id;
    Long inboundOrderId;
    Long productid;
    Integer batchNumber;
    LocalDate duedate;
    LocalDateTime manufacturingDate;
    Integer initialQuantity;
    Integer currentQunatity;
}
