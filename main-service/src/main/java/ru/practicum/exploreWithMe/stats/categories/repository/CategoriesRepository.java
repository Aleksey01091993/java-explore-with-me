package ru.practicum.exploreWithMe.stats.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreWithMe.stats.categories.model.Categories;
import ru.practicum.exploreWithMe.stats.categories.model.QCategories;


@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long>,
        JpaSpecificationExecutor<Categories>,
        QuerydslPredicateExecutor<Categories>,
        QuerydslBinderCustomizer<QCategories> {

    @Override
    default void customize(final QuerydslBindings bindings, final QCategories root) {
    }

}
