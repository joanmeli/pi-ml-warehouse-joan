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

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "sectionProduct",
            joinColumns = {
                    @JoinColumn(name = "section_id", nullable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "product_id", nullable = false)
            }
    )
    private List<Product> products;

    @OneToMany(mappedBy = "sectionId", cascade = CascadeType.ALL)
    private List<SectionProduct> sectionProducts;

    public void addSectionProduct(Long productId) {
        SectionProduct newSectionProduct = SectionProduct.builder()
                .sectionId(this)
                .productId(Product.builder().id(productId).build())
                .build();
        this.sectionProducts.add(newSectionProduct);
    }
}
