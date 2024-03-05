package ru.practicum.exploreWithMe.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import ru.practicum.exploreWithMe.stats.model.QStatsUnique;
import ru.practicum.exploreWithMe.stats.model.StatsUnique;

import java.util.Optional;

public interface StatsUniqueRepository extends JpaRepository<StatsUnique, Long>,
        JpaSpecificationExecutor<StatsUnique>,
        QuerydslPredicateExecutor<StatsUnique>,
        QuerydslBinderCustomizer<QStatsUnique> {
    @Override
    default void customize(final QuerydslBindings bindings, final QStatsUnique root) {

    }

    Optional<StatsUnique> findByUriIp(String uriIp);
}
