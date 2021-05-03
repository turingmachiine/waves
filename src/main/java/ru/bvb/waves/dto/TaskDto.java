package ru.bvb.waves.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bvb.waves.models.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {
    private Long id;
    private TaskType type;
    private State state;
    private String title;
    private Priority priority;
    private String description;
    private UserDto creator;
    private UserDto executor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Long> blockedBy;
    private List<Long> blocks;

    public static TaskDto from(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .type(task.getType())
                .state(task.getState())
                .title(task.getTitle())
                .priority(task.getPriority())
                .description(task.getDescription())
                .creator(UserDto.from(task.getCreator()))
                .executor(task.getExecutor() != null ? UserDto.from(task.getExecutor()) : null)
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .blockedBy(task.getBlockedBy().stream().map(Task::getId).collect(Collectors.toList()))
                .blocks(task.getBlocks().stream().map(Task::getId).collect(Collectors.toList()))
                .build();
    }

    public static List<TaskDto> from (List<Task> tasks) {
        return tasks.stream().map(TaskDto::from).collect(Collectors.toList());
    }
}
