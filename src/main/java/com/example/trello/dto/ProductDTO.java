package com.example.trello.dto;

import com.example.trello.entity.Product;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDTO {
    private long id;
    private String name;
    private float price;

    public ProductDTO convertToDto(Product entity) {
        ProductDTO dto = new ProductDTO();
        dto.setId(entity.getProduct_id());
        dto.setName(entity.getProductName());
        dto.setPrice(entity.getProductPrice());

        return dto;
    }

    public List<ProductDTO> toListDto(List<Product> listEntity) {
        List<ProductDTO> listDto = new ArrayList<>();

        listEntity.forEach(e->{
            listDto.add(this.convertToDto(e));
        });
        return listDto;
    }
}
