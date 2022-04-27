package br.com.group9.pimlwarehouse.entity;


import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(
    name = "sectionProduct",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"section_id", "product_id"})
    }
)
//@IdClass(SectionProductId.class)
public class SectionProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_product_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "section_id")
//    @Id
    private Section section;

//    @Id
    @Column(name = "product_id")
    private Long productId;
}
