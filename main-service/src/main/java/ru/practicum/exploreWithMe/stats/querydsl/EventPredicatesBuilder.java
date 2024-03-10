package ru.practicum.exploreWithMe.stats.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EventPredicatesBuilder {

    private final List<SearchCriteria> params;

    public EventPredicatesBuilder() {
        params = new ArrayList<>();
    }

    @SuppressWarnings("UnusedReturnValue")
    public EventPredicatesBuilder with(String key, Object value) {
        params.add(new SearchCriteria(key, value));
        return this;
    }

    public BooleanExpression build() {
        if (params.isEmpty()) {
            return null;
        }

        List<BooleanExpression> predicates = params.stream()
                .map(param -> {
                    EventPredicate predicate = new EventPredicate(param);
                    return predicate.getPredicate();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        BooleanExpression result = Expressions.asBoolean(true).isTrue();
        for (BooleanExpression predicate : predicates) {
            result = result.and(predicate);
        }
        return result;
    }
}
