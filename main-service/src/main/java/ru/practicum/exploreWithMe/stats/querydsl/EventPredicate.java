package ru.practicum.exploreWithMe.stats.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import ru.practicum.exploreWithMe.stats.events.model.QEvent;
import ru.practicum.exploreWithMe.stats.statuses.Status;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class EventPredicate {

    private SearchCriteria criteria;

    public BooleanExpression getPredicate() {
        if (criteria.getKey().equals("users")) {
            return QEvent.event.initiator.id.in((List<Long>) criteria.getValue());
        } else if (criteria.getKey().equals("states")) {
            return QEvent.event.state.in((List<Status>) criteria.getValue());
        } else if (criteria.getKey().equals("category")) {
            return QEvent.event.category.id.in((List<Long>) criteria.getValue());
        } else if (criteria.getKey().equals("text")) {
            return QEvent.event.annotation.containsIgnoreCase((String) criteria.getValue())
                    .or(QEvent.event.description.containsIgnoreCase((String) criteria.getValue()));
        } else if (criteria.getKey().equals("paid")) {
            return QEvent.event.paid.eq((Boolean) criteria.getValue());
        } else if (criteria.getKey().equals("eventStart")) {
            return QEvent.event.eventDate.before((LocalDateTime) criteria.getValue());
        } else if (criteria.getKey().equals("eventEnd")) {
            return QEvent.event.eventDate.after((LocalDateTime) criteria.getValue());
        } else if (criteria.getKey().equals("available") && (Boolean) criteria.getValue()) {
            return QEvent.event.confirmedRequest.loe(QEvent.event.participantLimit);
        }
        return null;
    }
}
