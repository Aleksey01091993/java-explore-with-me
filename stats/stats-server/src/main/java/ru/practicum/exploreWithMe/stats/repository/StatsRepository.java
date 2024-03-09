package ru.practicum.exploreWithMe.stats.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreWithMe.stats.model.QStats;
import ru.practicum.exploreWithMe.stats.model.Stats;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long>,
        JpaSpecificationExecutor<Stats>,
        QuerydslPredicateExecutor<Stats>,
        QuerydslBinderCustomizer<QStats> {
    @Override
    default void customize(final QuerydslBindings bindings, final QStats root) {

    }

}
