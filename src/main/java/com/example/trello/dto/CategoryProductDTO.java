package com.example.trello.dto;

import com.example.trello.entity.Category;
import com.example.trello.entity.Product;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class CategoryProductDTO {
    private long category_id;

    @NotBlank
    private String categoryName;


    private List<ProductDTO> productList;


    public CategoryProductDTO convertToDto(Category category) {
        CategoryProductDTO categoryDTO = new CategoryProductDTO();
        categoryDTO.setCategory_id(category.getCategory_id());
        categoryDTO.setCategoryName(category.getCategoryName());

        List<ProductDTO> listDto = new ArrayList<>();

        if(category.getProducts() != null){
            category.getProducts().forEach(e -> {
                listDto.add(new ProductDTO().convertToDto(e));
            });
        }
        categoryDTO.setProductList(listDto);

        return categoryDTO;
    }

//    public Category convertToEti(CategoryDTO categoryDTO) {
//        Category category = new Category();
//
//        category.setCategory_id(categoryDTO.getCategory_id());
//        category.setCategoryName(categoryDTO.getCategoryName());
//
//        return category;
//    }


    public List<CategoryProductDTO> toListDto(List<Category> listEntity) {
        List<CategoryProductDTO> listDto = new ArrayList<>();

        listEntity.forEach(e->{
            listDto.add(this.convertToDto(e));
        });
        return listDto;
    }

}
