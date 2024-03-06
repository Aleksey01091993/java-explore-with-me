package ru.practicum.exploreWithMe.stats.repository;


import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreWithMe.stats.model.QStats;
import ru.practicum.exploreWithMe.stats.model.Stats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long>,
        JpaSpecificationExecutor<Stats>,
        QuerydslPredicateExecutor<Stats>,
        QuerydslBinderCustomizer<QStats> {
    @Override
    default void customize(final QuerydslBindings bindings, final QStats root) {

    }

}
