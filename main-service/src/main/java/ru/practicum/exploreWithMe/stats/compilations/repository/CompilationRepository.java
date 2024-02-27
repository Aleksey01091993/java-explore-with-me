package ru.practicum.exploreWithMe.stats.compilations.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreWithMe.stats.compilations.model.Compilation;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    public List<Compilation> findAllByPinned(Boolean pined, Pageable pageable);
}
