package com.example.trello.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product", indexes = @Index(name = "multiIndex", columnList = "product_id, product_name, category_id"))
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long product_id;

    @Column(name = "product_name")
    @NotBlank
    private String productName;

    @Column(name = "product_price")
    @Min(value = 1)
    private float productPrice;

    @Column(name = "product_description")
    @NotBlank
    private String productDescription;

    @Column(name = "product_qty")
    @Min(value = 0)
    private long productQty;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;



    public Product(String productName, float productPrice, String productDescription, long productQty, Category category) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.productQty = productQty;
        this.category = category;
    }
}
