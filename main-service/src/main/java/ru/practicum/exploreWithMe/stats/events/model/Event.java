package ru.practicum.exploreWithMe.stats.events.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.exploreWithMe.stats.categories.model.Categories;
import ru.practicum.exploreWithMe.stats.coments.model.Comments;
import ru.practicum.exploreWithMe.stats.compilations.model.Compilation;
import ru.practicum.exploreWithMe.stats.statuses.Status;
import ru.practicum.exploreWithMe.stats.users.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(length = 2000, name = "annotation")
    private String annotation;
    @ManyToMany(mappedBy = "events")
    private List<Compilation> compilations;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Categories category;
    @Column(name = "confirmed_request")
    private Integer confirmedRequest;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(length = 7000, name = "description")
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    private User initiator;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "lat")),
            @AttributeOverride(name = "lon", column = @Column(name = "lon"))
    })
    private Location location;
    @Column(name = "paid")
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private Status state;
    @Column(length = 120, name = "title")
    private String title;
    @Column(name = "views")
    private Integer views;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Comments> comments;

}
