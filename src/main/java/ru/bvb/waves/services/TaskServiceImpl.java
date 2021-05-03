package ru.bvb.waves.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.bvb.waves.dto.StateUpdateDto;
import ru.bvb.waves.dto.TaskCreationDto;
import ru.bvb.waves.dto.TaskDto;
import ru.bvb.waves.dto.TaskEditionDto;
import ru.bvb.waves.models.Role;
import ru.bvb.waves.models.State;
import ru.bvb.waves.models.Task;
import ru.bvb.waves.models.User;
import ru.bvb.waves.repositories.TasksRepository;
import ru.bvb.waves.repositories.UsersRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.bvb.waves.dto.TaskDto.from;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TasksRepository tasksRepository;

    @Override
    public List<TaskDto> getTasks(Integer page, Integer size) {
        return from(tasksRepository.findAll(PageRequest.of(page, size)).getContent());
    }

    @Override
    public TaskDto getTask(Long taskId) {
        return from(tasksRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found")));
    }

    @Override
    public TaskDto addTask(Long creatorId, TaskCreationDto taskDto) {
        Task task = Task.builder()
                .title(taskDto.getTitle())
                .type(taskDto.getType())
                .creator(usersRepository.findById(creatorId).orElseThrow(
                        () -> new IllegalArgumentException("User not found")))
                .state(taskDto.getState())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .blocks(new ArrayList<>())
                .blockedBy(new ArrayList<>())
                .build();
        if (taskDto.getPriority() != null) {
            task.setPriority(taskDto.getPriority());
        }
        if (taskDto.getDescription() != null) {
            task.setDescription(taskDto.getDescription());
        }
        if (taskDto.getExecutorId() != null) {
            User user = usersRepository.findById(taskDto.getExecutorId()).orElseThrow(
                    () -> new IllegalArgumentException("User not found"));
            if (userStateCheck(user, task.getState())) {
                task.setExecutor(user);
            }
        }
        if (taskDto.getBlockedBy() != null) {
            List<Task> tasks = task.getBlockedBy();
            for (Long id : taskDto.getBlockedBy()) {
                tasks.add(tasksRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found")));
            }
        }
        if (taskDto.getBlocks() != null) {
            List<Task> tasks = task.getBlocks();
            for (Long id : taskDto.getBlocks()) {
                tasks.add(tasksRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found")));
            }
        }
        tasksRepository.save(task);
        return from(task);
    }

    @Override
    public TaskDto updateTask(Long taskId, TaskEditionDto taskDto) {
        Task task = tasksRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        if (taskDto.getTitle() != null) {
            task.setTitle(taskDto.getTitle());
        }
        if (taskDto.getDescription() != null) {
            task.setDescription(taskDto.getDescription());
        }
        if (taskDto.getPriority() != null) {
            task.setPriority(taskDto.getPriority());
        }
        if (taskDto.getType() != null) {
            task.setType(taskDto.getType());
        }
        if (taskDto.getExecutorId() != null) {
            User user = usersRepository.findById(taskDto.getExecutorId()).orElseThrow(
                    () -> new IllegalArgumentException("User not found"));
            if (userStateCheck(user, task.getState())) {
                task.setExecutor(user);
            }
        }
        if (taskDto.getBlockedBy() != null) {
            List<Task> tasks = task.getBlockedBy();
            for (Long id : taskDto.getBlockedBy()) {
                tasks.add(tasksRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found")));
            }
        }
        if (taskDto.getBlocks() != null) {
            List<Task> tasks = task.getBlocks();
            for (Long id : taskDto.getBlocks()) {
                tasks.add(tasksRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found")));
            }
        }
        tasksRepository.save(task);
        return from(task);
    }

    @Override
    public void deleteTask(Long taskId) {
        tasksRepository.deleteById(taskId);
    }

    @Override
    public TaskDto updateState(Long taskId, StateUpdateDto state) {
        Task task = tasksRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        if (state.getUserId() != null) {
            User user = usersRepository.findById(state.getUserId()).orElseThrow(
                    () -> new IllegalArgumentException("User not found"));
            if (userStateCheck(user, state.getState())) {
                task.setExecutor(user);
            } else {
                throw new IllegalArgumentException("This user can't be executor");
            }
        }
        if (state.getState() == State.TO_DO || state.getState() == State.WONTFIX) {
            task.setState(state.getState());
        } else {
            if (task.getState().next().equals(state.getState())) {
                if ((task.getExecutor() == null && state.getState() == State.IN_PROGRESS)
                        || task.getExecutor() != null && !userStateCheck(task.getExecutor(), state.getState())) {
                    throw new IllegalArgumentException("Add right executor to switch to " + state.getState());
                }
                task.setState(state.getState());
            } else {
                throw new IllegalArgumentException();
            }
        }
        tasksRepository.save(task);
        return from(task);
    }

    private boolean userStateCheck(User user, State state) {
        return user.getRole() != Role.MANAGER &&
                !((state == State.IN_PROGRESS ||
                        state == State.CODE_REVIEW ||
                        state == State.DEV_TEST) && user.getRole() == Role.TESTER)
                && !(state == State.TESTING && user.getRole() == Role.DEV);
    }

    @Override
    public List<TaskDto> searchTask(String query, Integer page, Integer size) {
        try {
            return from(tasksRepository.findByTitleContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrId(query, query,
                    Long.parseLong(query), PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"))));
        } catch (NumberFormatException e) {
            return from(tasksRepository.findByTitleContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrId(query, query,
                    0L, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"))));
        }
    }
}
