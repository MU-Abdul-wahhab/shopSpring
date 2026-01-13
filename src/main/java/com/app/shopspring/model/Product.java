package com.app.shopspring.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "products")
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    @NotBlank
    @Size(min = 3, message = "Product Name Must Contain 3 Characters")
    private String productName;

    @Size(min = 6, message = "Product Description Must Contain 6 Characters")
    private String description;
    private String image;
    private Integer quantity;
    private Double price;
    private Double specialPrice;
    private Double discountPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User user;
}
