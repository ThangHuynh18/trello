package com.example.trello.controller;

import com.example.trello.dto.CategoryDTO;
import com.example.trello.dto.ErrorCode;
import com.example.trello.dto.ResponseDTO;
import com.example.trello.dto.SuccessCode;
import com.example.trello.exception.ResourceNotFoundException;
import com.example.trello.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/categories")
public class CategoryController {

    Logger logger= LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<ResponseDTO> getAllCategory() throws Exception {
        ResponseDTO response = new ResponseDTO();
        List<ResponseDTO> responseDTO = new ArrayList<>();

        try {
            List<CategoryDTO> categoryDTOS = categoryService.retrieveCategories();
            List list = Collections.synchronizedList(new ArrayList(categoryDTOS));

            //response.setData(responseDTO.addAll(list));
            if (responseDTO.addAll(list) == true) {
                response.setData(categoryDTOS);
            }
            response.setSuccessCode(SuccessCode.GET_ALL_CATEGORY_SUCCESS);
        } catch (Exception e){
            throw new Exception(ErrorCode.GET_CATEGORY_ERROR + ". " +e.getMessage());
        }
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{category_id}")
    public ResponseEntity<ResponseDTO> getCate(@PathVariable("category_id") Long id) throws ResourceNotFoundException {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Optional<CategoryDTO> categoryDTO = categoryService.getCate(id);
            if(categoryDTO.isPresent()){
                responseDTO.setData(categoryDTO);
                responseDTO.setSuccessCode(SuccessCode.FIND_CATEGORY_SUCCESS);
                logger.info("category found : {}",categoryDTO.get().getCategoryName());
            } else {
                try {
                    throw new Exception();
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Category Not Found with ID : {}",id);
                }

            }


        } catch (Exception e) {
            throw new ResourceNotFoundException(""+ErrorCode.FIND_CATEGORY_ERROR);
        }
        return ResponseEntity.ok(responseDTO);
    }


    @GetMapping("/category/{category_id}")
    public ResponseEntity<Optional<CategoryDTO>> getCateById(@PathVariable("category_id") Long id) throws ResourceNotFoundException {
        ResponseDTO responseDTO = new ResponseDTO();
        Optional<CategoryDTO> categoryDTO;
        try {
            categoryDTO = categoryService.getCate(id);
            if (categoryDTO.isPresent()) {
                responseDTO.setData(categoryDTO);
                responseDTO.setSuccessCode(SuccessCode.FIND_CATEGORY_SUCCESS);
                logger.info("category found : {}", categoryDTO.get().getCategoryName());
            } else {
                try {
                    throw new Exception();
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Category Not Found with ID : {}", id);
                }

            }


        } catch (Exception e) {
            throw new ResourceNotFoundException("" + ErrorCode.FIND_CATEGORY_ERROR);
        }
        return ResponseEntity.ok(categoryDTO);
    }



    //insert

    @PostMapping("/category")
    public ResponseEntity<ResponseDTO> createCate(@Valid @RequestBody CategoryDTO categoryDTO) throws Exception{
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            CategoryDTO dto = categoryService.saveCategory(categoryDTO);
            responseDTO.setData(dto);
            responseDTO.setSuccessCode(SuccessCode.ADD_CATEGORY_SUCCESS);
        } catch (Exception e){
            throw new Exception(ErrorCode.ADD_CATEGORY_ERROR + ". "+e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }


    //update
    @PutMapping("/category/{category_id}")
    public ResponseEntity<ResponseDTO> updateCate(@PathVariable(value = "category_id") Long categoryId, @Valid @RequestBody CategoryDTO categoryDTO) throws Exception {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            CategoryDTO updateCate = categoryService.updateCategory(categoryId, categoryDTO);

            responseDTO.setData(updateCate);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_CATEGORY_SUCCESS);
        } catch (Exception e){
            throw new Exception(ErrorCode.UPDATE_CATEGORY_ERROR + ". "+ e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    //    //delete
    @DeleteMapping("/category/{category_id}")
    public ResponseEntity<ResponseDTO> deleteCate(@PathVariable(value = "category_id") Long categoryId) throws Exception {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Boolean isDel = categoryService.deleteCategory(categoryId);
            responseDTO.setData(isDel);
            responseDTO.setSuccessCode(SuccessCode.DELETE_CATEGORY_SUCCESS);
        } catch (Exception e){
            throw new Exception(ErrorCode.DELETE_CATEGORY_ERROR +". "+ e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

}
