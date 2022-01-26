package com.example.trello.repository.impl;

import com.example.trello.entity.QCategory;
import com.example.trello.entity.QProduct;
import com.example.trello.repository.BaseRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;

public abstract class BaseRepositoryImpl<T,ID> extends SimpleJpaRepository<T,ID> implements BaseRepository<T,ID> {

    EntityManager em;
    JPAQueryFactory jpaQueryFactory;

    protected  final QCategory category = QCategory.category;
    protected  final QProduct product = QProduct.product;

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.em = em;
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public T findByIdMandatory(ID id) throws IllegalArgumentException {
        return findById(id)
                .orElseThrow(()->new IllegalArgumentException("entity not found with id "+id));
    }
}
