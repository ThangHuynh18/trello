package com.example.trello.controller;

import com.example.trello.dto.CategoryDTO;
import com.example.trello.entity.Category;
import com.example.trello.exception.ResourceNotFoundException;
import com.example.trello.service.CategoryService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private CategoryService categoryService;

    private final List<CategoryDTO> categories = new ArrayList<>();

    private String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
    CategoryDTO RECORD_1 = new CategoryDTO(1l, "Rayven Yor");


    @BeforeEach
    void setUp(){
        this.categories.add(new CategoryDTO(1L,"cate1"));
        this.categories.add(new CategoryDTO(2L,"cate2"));
        this.categories.add(new CategoryDTO(3L,"cate3"));
    }

    @Test
    public void getAllTest() throws Exception {
        when(categoryService.retrieveCategories()).thenReturn(this.categories);
        this.mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(categories.size())));

    }

    @Test
    public void givenCategoryId_whenGetCategory()
            throws Exception {

        CategoryDTO category = new CategoryDTO(1,"cate1");

        given(categoryService.getCate(category.getCategory_id())).willReturn(java.util.Optional.of(category));

        mockMvc.perform(get("/api/categories/category/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("categoryName", is(category.getCategoryName())));
    }

    @Test
    public void createCate() throws Exception {
        CategoryDTO category = new CategoryDTO(1,"category1");
    //solution1
//        String exampleJson = "{\"id\":\"1\",\"categoryName\":\"category1\"}";
//
//        when(categoryService.saveCategory(category)).thenReturn(category);
//
//        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .post("/api/categories/category")
//                .accept(MediaType.APPLICATION_JSON).content(exampleJson)
//                .contentType(MediaType.APPLICATION_JSON);

    //solution 2
        when(categoryService.saveCategory(category)).thenReturn(category);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/categories/category")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(category));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
                //.andExpect(jsonPath("$.['data.categoryName']", is("category1")));
    }

    @Test
    public void updateCate() throws Exception {
        String uri = "/api/categories/category/2";
        CategoryDTO categoryUpdated = new CategoryDTO();
        categoryUpdated.setCategoryName("cate2");

        String inputJson = mapToJson(categoryUpdated);
        when(categoryService.getCate(RECORD_1.getCategory_id())).thenReturn(Optional.of(RECORD_1));
        when(categoryService.saveCategory(categoryUpdated)).thenReturn(categoryUpdated);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
//        String content = mvcResult.getResponse().getContentAsString();
//        assertEquals(content, "Category is updated successsfully");
    }


    @Test
    public void deleteProduct() throws Exception {
        String uri = "/api/categories/category/2";

        when(categoryService.getCate(RECORD_1.getCategory_id())).thenReturn(Optional.of(RECORD_1));
        when(categoryService.deleteCategory(RECORD_1.getCategory_id())).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);
//        String content = mvcResult.getResponse().getContentAsString();
//        assertEquals(content, "Category is deleted successsfully");
    }

//    @Test
//    void testDeleteCate_CategoryServiceThrowsResourceNotFoundException() throws Exception {
//        // Setup
//        when(categoryService.deleteCategory(0L)).thenThrow(ResourceNotFoundException.class);
//
//        // Run the test
//        final MockHttpServletResponse response = mockMvc.perform(delete("/api/categories/category/{category_id}", 0)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andReturn().getResponse();
//
//        // Verify the results
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
//    }
}
