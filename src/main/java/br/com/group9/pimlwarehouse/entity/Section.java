package br.com.group9.pimlwarehouse.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
@Entity
@Table(name = "section")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long id;

    private Double minimalTemperature;

    private Double maximalTemperature;

    private Integer size;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<SectionProduct> sectionProducts;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<InboundOrder> inboundOrders;

    public void addSectionProduct(SectionProduct sectionProduct) {
        this.sectionProducts.add(sectionProduct);
    }
}
