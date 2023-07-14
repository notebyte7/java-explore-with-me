package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.compilation.Compilation;

import java.util.Optional;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    Optional<Compilation> findById(Long compId);

    @Query("SELECT c " +
            "FROM Compilation c " +
            "WHERE c.pinned IN ?1 OR ?1 IS NULL")
    Page<Compilation> findAll(Boolean pinned, Pageable pageable);
}
