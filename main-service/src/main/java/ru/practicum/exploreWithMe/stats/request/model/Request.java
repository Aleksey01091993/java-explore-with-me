package ru.practicum.exploreWithMe.stats.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.exploreWithMe.stats.events.model.Event;
import ru.practicum.exploreWithMe.stats.statuses.Status;
import ru.practicum.exploreWithMe.stats.users.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@Table(name = "request")
@NoArgsConstructor
@AllArgsConstructor
public class Request implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Event event;
    @ManyToOne
    private User requester;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDateTime created;
}
