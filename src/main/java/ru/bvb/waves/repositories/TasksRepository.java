package ru.bvb.waves.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.bvb.waves.models.Task;

import java.util.List;

public interface TasksRepository extends JpaRepository<Task, Long> {
    List<Task> findByTitleContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrId(String title, String description, Long id, Pageable pageable);
}
