package ru.practicum.exploreWithMe.stats.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@Table(name = "stats_unique")
@NoArgsConstructor
@AllArgsConstructor
public class StatsUnique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "app")
    private String app;
    @Column(name = "uri_ip", unique = true)
    private String uriIp;
    @Column(name = "uri")
    private String uri;
    @Column(name = "time_tamp")
    private LocalDateTime timesTamp;
}