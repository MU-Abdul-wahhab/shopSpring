package com.app.shopspring.service.impl;

import com.app.shopspring.exceptions.APIException;
import com.app.shopspring.exceptions.ResourceNotFoundException;
import com.app.shopspring.model.Category;
import com.app.shopspring.repository.CategoryRepository;
import com.app.shopspring.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repository) {
        this.categoryRepository = repository;
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()) {
            throw new APIException("No Category found");
        }
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory != null) {
            throw new APIException("Category already exists " + category.getCategoryName());
        }
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long id) {
        Category existingCategory = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category" , "categoryId", id)
        );

        categoryRepository.delete(existingCategory);
        return "Category with categoryId - " + id + " was deleted";
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category" , "categoryId", id));

        existingCategory.setCategoryName(category.getCategoryName());
        return categoryRepository.save(existingCategory);
    }
}
