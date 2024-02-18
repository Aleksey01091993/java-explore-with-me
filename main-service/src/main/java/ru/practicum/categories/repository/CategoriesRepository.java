package ru.practicum.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.categories.model.Categories;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {
}
