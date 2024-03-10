package ru.practicum.exploreWithMe.stats.events.model;

import lombok.*;
import ru.practicum.exploreWithMe.stats.categories.model.Categories;
import ru.practicum.exploreWithMe.stats.coments.model.Comments;
import ru.practicum.exploreWithMe.stats.statuses.StateAction;
import ru.practicum.exploreWithMe.stats.statuses.Status;
import ru.practicum.exploreWithMe.stats.users.model.User;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Builder
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 2000)
    private String annotation;
    @ManyToOne
    private Categories category;
    private Integer confirmedRequest;
    private LocalDateTime createdOn;
    @Column(length = 7000)
    private String description;
    private LocalDateTime eventDate;
    @ManyToOne
    private User initiator;
    @Embedded
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private Status state;
    @Column(length = 120)
    private String title;
    private Integer views;
    @OneToMany(mappedBy = "event")
    private List<Comments> comments = new ArrayList<>();

}
