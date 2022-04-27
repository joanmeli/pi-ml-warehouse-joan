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
@Entity
@Table(name = "batchStock")
public class BatchStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @ManyToOne()
    @JoinColumn(name="inboundOrder_id")
    private InboundOrder inboundOrder;
    private Long productSize;
    private Long productId;
    private Integer batchNumber;
    private LocalDate dueDate;
    private LocalDateTime manufacturingDate;
    private Integer initialQuantity;
    private Integer currentQuantity;
}
