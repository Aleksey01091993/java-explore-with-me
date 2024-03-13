package ru.practicum.exploreWithMe.stats.coments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreWithMe.stats.coments.model.Comments;
import ru.practicum.exploreWithMe.stats.coments.model.QComments;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long>,
        JpaSpecificationExecutor<Comments>,
        QuerydslPredicateExecutor<Comments>,
        QuerydslBinderCustomizer<QComments> {
    @Override
    default void customize(final QuerydslBindings bindings, final QComments root) {

    }
    Optional<Comments> findFirstByIdAndAuthor_Id(Long id, Long userId);

    List<Comments> findAllByIdIn(List<Long> ids);
}
