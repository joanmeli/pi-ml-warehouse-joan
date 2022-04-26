package br.com.group9.pimlwarehouse.entity;


import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "sectionProduct")
public class SectionProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_product_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section sectionId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product productId;
}
