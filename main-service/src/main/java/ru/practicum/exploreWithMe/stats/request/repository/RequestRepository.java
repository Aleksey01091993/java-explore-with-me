package ru.practicum.exploreWithMe.stats.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import ru.practicum.exploreWithMe.stats.request.model.QRequest;
import ru.practicum.exploreWithMe.stats.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long>,
        JpaSpecificationExecutor<Request>,
        QuerydslPredicateExecutor<Request>,
        QuerydslBinderCustomizer<QRequest> {
    @Override
    default void customize(final QuerydslBindings bindings, final QRequest root) {
    }
    Optional<Request> findFirstByRequester_IdAndEvent_Id(Long userId, Long eventId);
    List<Request> findAllByRequester_Id(Long userId);
    List<Request> findAllByRequester_IdAndEvent_Id(Long userId, Long eventId);
    List<Request> findAllByIdIn(List<Long> ids);
}
