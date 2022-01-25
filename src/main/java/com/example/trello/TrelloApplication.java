package com.example.trello;

import com.example.trello.entity.Category;
import com.example.trello.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TrelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrelloApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(CategoryRepository categoryRepository){
        return (args -> {
           categoryRepository.save(new Category(1L, "Category 1"));
           categoryRepository.save(new Category(2L, "Category 2"));

        });
    }
}
