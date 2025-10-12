package com.app.shopspring.service.impl;

import com.app.shopspring.model.Category;
import com.app.shopspring.repository.CategoryRepository;
import com.app.shopspring.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repository) {
        this.categoryRepository = repository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long id) {
        Category existingCategory = categoryRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found")
        );

        categoryRepository.delete(existingCategory);
        return "Category with categoryId - " + id + " was deleted";
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found"));

        existingCategory.setCategoryName(category.getCategoryName());
        return categoryRepository.save(existingCategory);
    }
//    @Override
//    public Category updateCategory(Category category, Long id) {
//        List<Category> categories = categoryRepository.findAll();
//        Optional<Category> optionalCategory = categories.stream().filter(
//                c -> c.getCategoryId().equals(id)
//        ).findFirst();
//
//        if (optionalCategory.isPresent()) {
//            Category exisitingCategory = optionalCategory.get();
//            exisitingCategory.setCategoryName(category.getCategoryName());
//            Category savedCategory = categoryRepository.save(exisitingCategory);
//            return savedCategory;
//        } else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found");
//        }
//    }
}
