package ru.practicum.exploreWithMe.stats.users.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreWithMe.stats.users.model.QUser;
import ru.practicum.exploreWithMe.stats.users.model.User;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long>,
        JpaSpecificationExecutor<User>,
        QuerydslPredicateExecutor<User>,
        QuerydslBinderCustomizer<QUser> {

    @Override
    default void customize(final QuerydslBindings bindings, final QUser root) {
    }


    List<User> findAllByIdIn(List<Long> ids, Pageable pageable);
}
