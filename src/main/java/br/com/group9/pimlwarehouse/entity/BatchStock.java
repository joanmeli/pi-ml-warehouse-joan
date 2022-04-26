package br.com.group9.pimlwarehouse.entity;

import lombok.*;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BatchStock {
    @Id
    Long Id;
    @ManyToOne()
    @JoinColumn(name="inboundOrder_id")
    InboundOrder inboundOrder;

    Long productid;
    Integer batchNumber;
    LocalDate duedate;
    LocalDateTime manufacturingDate;
    Integer initialQuantity;
    Integer currentQunatity;
}
