package ru.practicum.exploreWithMe.stats.querydsl;

import com.querydsl.core.types.dsl.*;
import lombok.AllArgsConstructor;
import ru.practicum.exploreWithMe.stats.categories.model.Categories;
import ru.practicum.exploreWithMe.stats.events.model.Event;
import ru.practicum.exploreWithMe.stats.events.model.Status;
import ru.practicum.exploreWithMe.stats.users.model.User;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class EventPredicate {

    private SearchCriteria criteria;

    public BooleanExpression getPredicate() {

        PathBuilder<Event> entityPath = new PathBuilder<>(Event.class, "event");
        if (criteria.getKey().equals("users")) {
            PathBuilder<User> path = entityPath.get("initiator", User.class);
            return path.get("id", Long.class).in((List<Long>) criteria.getValue());
        } else if (criteria.getKey().equals("states")) {
            EnumPath<Status> path = entityPath.getEnum("state", Status.class);
            return path.in((List<Status>) criteria.getValue());
        } else if (criteria.getKey().equals("category")) {
            PathBuilder<Categories> path = entityPath.get(criteria.getKey(), Categories.class);
                    return path.get("id", Long.class).in((List<Long>) criteria.getValue());
        } else if (criteria.getKey().equals("text")) {
            StringPath path = entityPath.getString("annotation");
            StringPath pathTwo = entityPath.getString("description");
            return path.likeIgnoreCase('%' + criteria.getValue().toString().trim() + '%')
                            .or(pathTwo.likeIgnoreCase('%' + criteria.getValue().toString().trim() + '%'));
        } else if (criteria.getKey().equals("paid")) {
            BooleanPath path = entityPath.getBoolean(criteria.getKey());
            return path.eq((Boolean) criteria.getValue());
        } else if (criteria.getKey().equals("eventStart")) {
            DateTimePath<LocalDateTime> path = entityPath.getDateTime("eventDate", LocalDateTime.class);
            return path.before((LocalDateTime) criteria.getValue());
        } else if (criteria.getKey().equals("eventEnd")) {
            DateTimePath<LocalDateTime> path = entityPath.getDateTime("eventDate", LocalDateTime.class);
            return path.after((LocalDateTime) criteria.getValue());
        } else if (criteria.getKey().equals("available") && (Boolean) criteria.getValue()) {
            NumberPath<Integer> path = entityPath.getNumber("confirmedRequest", Integer.class);
            NumberPath<Integer> path2 = entityPath.getNumber("participantLimit", Integer.class);
            return path.loe(path2);
        }
        return null;
    }
}
