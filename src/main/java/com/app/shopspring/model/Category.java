package com.app.shopspring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    private Long categoryId;

    @NotBlank(message = "Category Name is Required")
    @Size(min = 5, message = "Category Name must Contain at least 5 Characters")
    private String categoryName;

}
