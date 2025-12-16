package com.app.shopspring.service;

import com.app.shopspring.model.Category;
import com.app.shopspring.payLoad.CategoryDTO;
import com.app.shopspring.payLoad.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    CategoryDTO createCategory(CategoryDTO categoryDto);
    CategoryDTO deleteCategory(Long id);
    CategoryDTO updateCategory(CategoryDTO categoryDto , Long id);
}
