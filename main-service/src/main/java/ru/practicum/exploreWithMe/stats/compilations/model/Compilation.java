package ru.practicum.exploreWithMe.stats.compilations.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.exploreWithMe.stats.events.model.Event;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "compilation")
@NoArgsConstructor
@AllArgsConstructor
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany
    private List<Event> events;
    private Boolean pinned;
    private String title;
}
