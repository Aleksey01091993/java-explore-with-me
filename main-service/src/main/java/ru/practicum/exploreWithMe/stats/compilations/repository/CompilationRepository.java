package ru.practicum.exploreWithMe.stats.compilations.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreWithMe.stats.compilations.model.Compilation;
import ru.practicum.exploreWithMe.stats.compilations.model.QCompilation;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long>,
        JpaSpecificationExecutor<Compilation>,
        QuerydslPredicateExecutor<Compilation>,
        QuerydslBinderCustomizer<QCompilation> {
    @Override
    default void customize(final QuerydslBindings bindings, final QCompilation root) {
    }

    List<Compilation> findAllByPinned(Boolean pined, Pageable pageable);
}
