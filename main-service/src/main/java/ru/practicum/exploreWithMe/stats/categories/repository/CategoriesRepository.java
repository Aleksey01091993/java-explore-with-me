package ru.practicum.exploreWithMe.stats.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreWithMe.stats.categories.model.Categories;


@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {
}
