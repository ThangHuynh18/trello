package com.example.trello.repository;

import com.example.trello.dto.Statistic;
import com.example.trello.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends BaseRepository<Category, Long> {
    public Optional<Category> findCategoryByCategoryName(String name);

    public List<Category> getAllFetchJoin();

    public List<Statistic> getStatistic();
}
