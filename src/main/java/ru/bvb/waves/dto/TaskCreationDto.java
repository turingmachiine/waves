package ru.bvb.waves.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bvb.waves.models.Priority;
import ru.bvb.waves.models.State;
import ru.bvb.waves.models.TaskType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskCreationDto {
    private TaskType type;
    private State state;
    private String title;
    private Priority priority;
    private String description;
    private Long executorId;
    private List<Long> blockedBy;
    private List<Long> blocks;
}
