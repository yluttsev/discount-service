package ru.example.discount.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.example.discount.entity.enums.DiscountType;

@Entity
@Table(name = "discount")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_category_id", referencedColumnName = "id")
    private ProductCategory productCategory;

    @ManyToOne
    @JoinColumn(name = "client_category_id", referencedColumnName = "id")
    private ClientCategory clientCategory;

    @Column(name = "min_value")
    private Short minValue;

    @Column(name = "max_value")
    private Short maxValue;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
}
