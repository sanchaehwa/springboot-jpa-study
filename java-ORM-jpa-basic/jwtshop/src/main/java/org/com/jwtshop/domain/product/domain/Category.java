package org.com.jwtshop.domain.product.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name="category")

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer category_id;

    @Column(nullable = false)
    private String name;

    //상위 카테고리
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private Category parent;

    //하위 카테고리
    @OneToMany(mappedBy ="parent")
    private List<Category>chidren = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    private List<ItemCategory>itemCategories = new ArrayList<>();




}
