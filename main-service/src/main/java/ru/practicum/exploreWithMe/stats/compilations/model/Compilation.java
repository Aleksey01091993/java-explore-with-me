package ru.practicum.exploreWithMe.stats.compilations.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.exploreWithMe.stats.events.model.Event;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "compilation")
@NoArgsConstructor
@AllArgsConstructor
public class Compilation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToMany
    @JoinTable(
            name = "compilation_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "events_id")
    )
    private List<Event> events;
    @Column(name = "pinned")
    private Boolean pinned;
    @Column(name = "title", length = 50)
    private String title;
}
