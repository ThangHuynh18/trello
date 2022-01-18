package com.example.trello.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "category", indexes = @Index(name = "firstIndex", columnList = "category_name"))
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long category_id;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    private Set<Product> products;

    @Column(name = "category_name")
    @NotBlank
    private String categoryName;

//    public Category(long category_id, String categoryName) {
//        this.category_id = category_id;
////        this.products = products;
//        this.categoryName = categoryName;
//    }
}
