package com.example.trello.service.impl;

import com.example.trello.dto.CategoryDTO;
import com.example.trello.entity.Category;
import com.example.trello.exception.ResourceNotFoundException;
import com.example.trello.repository.CategoryRepository;
import com.example.trello.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryDTO> retrieveCategories() {
        List<Category> categories = categoryRepository.findAll();
        return new CategoryDTO().toListDto(categories);
    }

    @Override
    public Optional<CategoryDTO> getCate(Long cateId) throws ResourceNotFoundException {
        Category category = categoryRepository.findById(cateId).orElseThrow(() -> new ResourceNotFoundException("Id not found"));
        return Optional.of(new CategoryDTO().convertToDto(category));
    }

    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        Category category = new CategoryDTO().convertToEti(categoryDTO);

        return new CategoryDTO().convertToDto(categoryRepository.save(category));
    }

    @Override
    public Boolean deleteCategory(Long categoryId) throws ResourceNotFoundException {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Id not found"));
        this.categoryRepository.delete(category);
        return true;
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) throws ResourceNotFoundException {
        Category cateExist = categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Id not found"));
        cateExist.setCategoryName(categoryDTO.getCategoryName());

        Category category = new Category();
        category = categoryRepository.save(cateExist);
        return new CategoryDTO().convertToDto(category);
    }



}
