package ru.bvb.waves.services;

import org.checkerframework.checker.nullness.Opt;
import org.hibernate.sql.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.bvb.waves.dto.StateUpdateDto;
import ru.bvb.waves.dto.TaskDto;
import ru.bvb.waves.models.Role;
import ru.bvb.waves.models.State;
import ru.bvb.waves.models.Task;
import ru.bvb.waves.models.User;
import ru.bvb.waves.repositories.TasksRepository;
import ru.bvb.waves.repositories.UsersRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class TaskServiceImplTest {

    @Autowired
    private TaskService taskService;

    @MockBean
    private TasksRepository tasksRepository;

    @MockBean
    private UsersRepository usersRepository;

    @BeforeEach
    void setUp() {
        User user1 = User.builder().id(1L).role(Role.MANAGER).build();

        doReturn(Optional.of(user1)).when(usersRepository).findById(1L);
        User user2 = User.builder().id(2L).role(Role.DEV).build();
        doReturn(Optional.of(user2)).when(usersRepository).findById(2L);
        User user3 = User.builder().id(3L).role(Role.LEAD).build();
        doReturn(Optional.of(user3)).when(usersRepository).findById(3L);
        User user4 = User.builder().id(4L).role(Role.TESTER).build();
        doReturn(Optional.of(user4)).when(usersRepository).findById(4L);

        Task task1 = Task.builder().id(1L).state(State.TO_DO).blocks(new ArrayList<>())
                .blockedBy(new ArrayList<>()).build();
        Task task2 = Task.builder().id(2L).state(State.IN_PROGRESS).blocks(new ArrayList<>())
                .blockedBy(new ArrayList<>()).build();
        Task task3 = Task.builder().id(3L).state(State.CODE_REVIEW).blocks(new ArrayList<>())
                .blockedBy(new ArrayList<>()).build();
        Task task4 = Task.builder().id(4L).state(State.DEV_TEST).blocks(new ArrayList<>())
                .blockedBy(new ArrayList<>()).build();
        Task task5 = Task.builder().id(5L).state(State.TESTING).blocks(new ArrayList<>())
                .blockedBy(new ArrayList<>()).build();
        Task task6 = Task.builder().id(6L).state(State.DONE).blocks(new ArrayList<>())
                .blockedBy(new ArrayList<>()).build();
        Task task7 = Task.builder().id(7L).state(State.WONTFIX).blocks(new ArrayList<>())
                .blockedBy(new ArrayList<>()).build();
        doReturn(Optional.of(task1)).when(tasksRepository).findById(1L);
        doReturn(Optional.of(task2)).when(tasksRepository).findById(2L);
        doReturn(Optional.of(task3)).when(tasksRepository).findById(3L);
        doReturn(Optional.of(task4)).when(tasksRepository).findById(4L);
        doReturn(Optional.of(task5)).when(tasksRepository).findById(5L);
        doReturn(Optional.of(task6)).when(tasksRepository).findById(6L);
        doReturn(Optional.of(task7)).when(tasksRepository).findById(7L);
    }

    @Test
    void setManager() {
        StateUpdateDto dto = StateUpdateDto.builder().userId(1L).state(State.IN_PROGRESS).build();
        assertThrows(IllegalArgumentException.class,() -> taskService.updateState(1L,dto));
    }

    @Test
    void setDev() {
        StateUpdateDto dto = StateUpdateDto.builder().userId(2L).state(State.IN_PROGRESS).build();
        TaskDto taskDto = taskService.updateState(1L, dto);
        assertEquals(2L, taskDto.getExecutor().getId());
        assertEquals(State.IN_PROGRESS, taskDto.getState());
    }

    @Test
    void setDevOnTest() {
        StateUpdateDto dto = StateUpdateDto.builder().userId(2L).state(State.TESTING).build();
        assertThrows(IllegalArgumentException.class,() -> taskService.updateState(4L,dto));
    }

    @Test
    void setTesterOnDev() {
        StateUpdateDto dto = StateUpdateDto.builder().userId(4L).state(State.CODE_REVIEW).build();
        assertThrows(IllegalArgumentException.class,() -> taskService.updateState(2L,dto));
    }

    @Test
    void setLeadOnTest() {
        StateUpdateDto dto = StateUpdateDto.builder().userId(3L).state(State.TESTING).build();
        TaskDto taskDto = taskService.updateState(4L, dto);
        assertEquals(3L, taskDto.getExecutor().getId());
        assertEquals(State.TESTING, taskDto.getState());
    }

    @Test
    void setInProgressWithoutExecutor() {
        StateUpdateDto dto = StateUpdateDto.builder().state(State.IN_PROGRESS).build();
        assertThrows(IllegalArgumentException.class,() -> taskService.updateState(1L,dto));
    }

    @Test
    void setDevTestWithoutExecutor() {
        StateUpdateDto dto = StateUpdateDto.builder().state(State.DEV_TEST).build();
        TaskDto taskDto = taskService.updateState(3L, dto);
        assertEquals(State.DEV_TEST, taskDto.getState());

    }

    @Test
    void setWrongState() {
        StateUpdateDto dto = StateUpdateDto.builder().state(State.DEV_TEST).build();
        assertThrows(IllegalArgumentException.class,() -> taskService.updateState(1L,dto));
    }

    @Test
    void setToDo() {
        StateUpdateDto dto = StateUpdateDto.builder().state(State.TO_DO).build();
        TaskDto taskDto = taskService.updateState(5L, dto);
        assertEquals(State.TO_DO, taskDto.getState());
    }
}