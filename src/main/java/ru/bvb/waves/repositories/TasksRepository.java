package ru.bvb.waves.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.bvb.waves.models.Task;

import java.util.List;

public interface TasksRepository extends JpaRepository<Task, Long> {

    @Query("from Task task where cast(task.id AS string) = :query or upper(task.title) like concat('%', upper(:query),'%') " +
            "or upper(task.description) like concat('%', upper(:query),'%')")
    List<Task> search(String query, Pageable pageable);
}
