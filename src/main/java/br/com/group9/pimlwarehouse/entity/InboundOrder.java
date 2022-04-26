package br.com.group9.pimlwarehouse.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "inboundOrder")
public class InboundOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inboundOrder_id")
    private Long id;
    @ManyToOne()
    @JoinColumn(name="section_id")
    private Section sections;
    private LocalDateTime orderDate;

    public InboundOrder(Section sections, LocalDateTime orderDate) {
        this.sections = sections;
        this.orderDate = orderDate;
    }
}
