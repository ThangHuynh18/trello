package com.example.trello.repository.impl;

import com.example.trello.dto.Statistic;
import com.example.trello.entity.Category;
import com.example.trello.repository.CategoryRepository;
import com.querydsl.core.types.Projections;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class CategoryRepositoryImpl extends BaseRepositoryImpl<Category, Long> implements CategoryRepository {
    public CategoryRepositoryImpl(EntityManager em) {
        super(Category.class, em);
    }

    @Override
    public Optional<Category> findCategoryByCategoryName(String name) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(category)
                        .from(category)
                        .where(category.categoryName.equalsIgnoreCase(name))
                        .fetchFirst()
        );
    }

    @Override
    public List<Category> getAllFetchJoin() {
        return jpaQueryFactory
                .select(category)
                .distinct()
                .from(category)
                .innerJoin(category.products,product)
                .fetchJoin().fetch();
    }

    @Override
    public List<Statistic> getStatistic() {
        return jpaQueryFactory
                .from(category)
                .innerJoin(category.products, product)
                .groupBy(category.categoryName)
                .select(Projections.constructor(Statistic.class,
                        category.categoryName, product.count())).fetch();
    }
}
