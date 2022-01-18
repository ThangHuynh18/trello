package com.example.trello.service;

import com.example.trello.dto.CategoryDTO;
import com.example.trello.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    public List<CategoryDTO> retrieveCategories();

    public Optional<CategoryDTO> getCate(Long categoryId) throws ResourceNotFoundException;

    public CategoryDTO saveCategory(CategoryDTO categoryDTO);

    public Boolean deleteCategory(Long categoryId) throws ResourceNotFoundException;

    public CategoryDTO updateCategory(Long categoryId,CategoryDTO categoryDTO) throws ResourceNotFoundException;
}
