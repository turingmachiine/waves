package ru.bvb.waves.services;

import ru.bvb.waves.dto.StateUpdateDto;
import ru.bvb.waves.dto.TaskCreationDto;
import ru.bvb.waves.dto.TaskDto;
import ru.bvb.waves.dto.TaskEditionDto;
import ru.bvb.waves.models.State;

import java.util.List;

public interface TaskService {

    List<TaskDto> getTasks(Integer page, Integer size);

    TaskDto getTask(Long taskId);
    TaskDto addTask(Long creatorId, TaskCreationDto taskDto);
    TaskDto updateTask(Long taskId, TaskEditionDto taskDto);
    void deleteTask(Long taskId);

    TaskDto updateState(Long taskId, StateUpdateDto state);

    List<TaskDto> searchTask(String query, Integer page, Integer size);
}
