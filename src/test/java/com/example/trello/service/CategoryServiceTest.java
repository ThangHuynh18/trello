package com.example.trello.service;

import com.example.trello.dto.CategoryDTO;
import com.example.trello.entity.Category;
import com.example.trello.exception.ResourceNotFoundException;
import com.example.trello.repository.CategoryRepository;
import com.example.trello.service.impl.CategoryServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
//@AutoConfigureMockMvc(addFilters = false)
public class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryServiceImpl categoryService;


    @Test
    public void whenGetAllCateThenReturnList(){

        List<Category> categories1 = new ArrayList<>();
        categories1.add(new Category(1,"cate1"));
        categories1.add(new Category(2,"cate2"));
       // categories1.add(new Category(3,"cate3"));

        when(categoryRepository.findAll()).thenReturn(categories1);

        List<CategoryDTO> categoryDtos1 = categoryService.retrieveCategories();

        assertEquals("cate1", categoryDtos1.get(0).getCategoryName());
        assertEquals(2, categoryService.retrieveCategories().size());     //khi goi ham retrieve thi auto goi findAll

        verify(categoryRepository, atLeastOnce()).findAll();
    }

    @Test
    public void testRetrieveCategories_ReturnsNoItems() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        final List<CategoryDTO> result = categoryService.retrieveCategories();

        // Verify the results
        Assertions.assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    public void testGetCateByIdThenReturnCateOfThisId() throws ResourceNotFoundException {
        // Setup
        // Configure CategoryRepository.findById(...).
        final Category category = (new Category(1L, "categoryName"));

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        CategoryDTO categoryDTO = new CategoryDTO().convertToDto(category);

        // Run the test
        final Optional<CategoryDTO> result = categoryService.getCate(1L);

        // Verify the results
        assertEquals("categoryName", result.get().getCategoryName());
        verify(categoryRepository, atLeastOnce()).findById(1L);
    }

    @Test
    public void whenGivenCateShouldAddCate() throws ResourceNotFoundException{
        CategoryDTO categoryDTO = new CategoryDTO(1,"category1");
        Category category1 = new Category(1, "category1");

        when(categoryRepository.save(any())).thenReturn(category1);
        CategoryDTO dto = categoryService.saveCategory(categoryDTO);

        Assert.assertEquals("category1", dto.getCategoryName());
        verify(categoryRepository, atLeastOnce()).save(any());

    }

    @Test
    public void testGivenIdThenDeleteCategoryOfId() throws ResourceNotFoundException {
        final Optional<Category> category = Optional.of(new Category(0L, "categoryName"));
        when(categoryRepository.findById(0L)).thenReturn(category);

        // Run the test
        final Boolean result = categoryService.deleteCategory(0L);

        // Verify the results
        Assertions.assertThat(result).isTrue();
        verify(categoryRepository).delete(any(Category.class));
    }

    @Test
    public void testUpdateCategory() throws Exception {
        final CategoryDTO categoryDTO = new CategoryDTO(0L, "categoryName");

        final Optional<Category> category = Optional.of(new Category(0L, "categoryName"));
        when(categoryRepository.findById(0L)).thenReturn(category);

        when(categoryRepository.save(any(Category.class))).thenReturn(new Category(0L, "categoryName"));

        // Run the test
        final CategoryDTO result = categoryService.updateCategory(0L, categoryDTO);

        // Verify the results
        assertEquals("categoryName", result.getCategoryName());
        verify(categoryRepository, atLeastOnce()).findById(0L);
        verify(categoryRepository, atLeastOnce()).save(any(Category.class));
    }


    //    @Test
//    public void shouldReturnAllCategoryDto() {
//
//        when(categoryRepository.findAll()).thenReturn(categories);
//
//        List<CategoryDTO> categoryDtos1 = categoryService.retrieveCategories();
//
////        assertEquals(categoryDtos1.size(),categoryDtos.size());
//
//        assertThat(categoryDtos).usingRecursiveComparison().isEqualTo(categoryDtos1);
//
//        verify(categoryRepository, times(1)).findAll();
//    }


//    @Test
//    public void testDeleteCategory_CategoryRepositoryFindByIdReturnsAbsent() throws ResourceNotFoundException {
//        // Setup
//        when(categoryRepository.findById(0L)).thenReturn(Optional.ofNullable(null));
//
//        // Run the test
////        final Boolean result = categoryService.deleteCategory(0L);
//        assertThatThrownBy(() -> categoryService.deleteCategory(0L)).isInstanceOf(ResourceNotFoundException.class);
//        // Verify the results
//        //Assertions.assertThat(result).isFalse();
//        verify(categoryRepository).delete(any());
//    }
}
