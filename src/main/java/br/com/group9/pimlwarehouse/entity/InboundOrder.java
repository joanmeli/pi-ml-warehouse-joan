package br.com.group9.pimlwarehouse.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    private Section section;
    private LocalDateTime orderDate;
    @OneToMany(mappedBy = "inboundOrder", cascade = CascadeType.ALL)
    private List<BatchStock> batchStocks;

    public InboundOrder(Section section, LocalDateTime orderDate) {
        this.section = section;
        this.orderDate = orderDate;
    }
}
