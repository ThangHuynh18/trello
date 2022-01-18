package com.example.trello.dto;

import com.example.trello.entity.Category;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private long category_id;

    @NotBlank
    private String categoryName;



    public CategoryDTO convertToDto(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategory_id(category.getCategory_id());
        categoryDTO.setCategoryName(category.getCategoryName());

        return categoryDTO;
    }

    public Category convertToEti(CategoryDTO categoryDTO) {
        Category category = new Category();

        category.setCategory_id(categoryDTO.getCategory_id());
        category.setCategoryName(categoryDTO.getCategoryName());

        return category;
    }


    public List<CategoryDTO> toListDto(List<Category> listEntity) {
        List<CategoryDTO> listDto = new ArrayList<>();

        listEntity.forEach(e->{
            listDto.add(this.convertToDto(e));
        });
        return listDto;
    }

}
