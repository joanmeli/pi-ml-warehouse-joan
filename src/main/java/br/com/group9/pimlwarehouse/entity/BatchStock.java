package br.com.group9.pimlwarehouse.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BatchStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;
    @ManyToOne()
    @JoinColumn(name="inboundOrder_id")
    InboundOrder inboundOrder;

    Long productid;
    Integer batchNumber;
    LocalDate duedate;
    LocalDateTime manufacturingDate;
    Integer initialQuantity;
    Integer currentQuantity;
}
