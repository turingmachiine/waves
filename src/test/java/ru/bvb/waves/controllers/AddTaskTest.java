package ru.bvb.waves.controllers;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.bvb.waves.dto.TaskCreationDto;
import ru.bvb.waves.dto.TaskDto;
import ru.bvb.waves.models.Priority;
import ru.bvb.waves.models.State;
import ru.bvb.waves.models.TaskType;
import ru.bvb.waves.services.TaskService;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AddTaskTest {

    private String token;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @SneakyThrows
    @BeforeEach
    public void SetUp() {
        when(taskService.addTask(2L, TaskCreationDto.builder().build()))
                .thenThrow(new IllegalArgumentException("Some values can't be null"));
        when(taskService.addTask(2L, makeCreation())).thenReturn(makeTask());
        MvcResult result = mockMvc.perform(post("/signIn").contentType("application/json").content("{\n" +
                "  \"login\": \"test2\",\n" +
                "  \"password\": \"qwerty007\"\n" +
                "}")).andReturn();
        token = result.getResponse().getContentAsString();
        token = token.substring(10, token.length() - 2);
    }

        private TaskDto makeTask() {
        return TaskDto.builder()
                .title("New Task")
                .type(TaskType.BUG)
                .state(State.TESTING)
                .priority(Priority.CRITICAL)
                .blockedBy(new ArrayList<>())
                .blocks(new ArrayList<>())
                .description("string").build();
    }

    private TaskCreationDto makeCreation() {
        return TaskCreationDto.builder()
                .title("New Task")
                .type(TaskType.BUG)
                .state(State.TESTING)
                .priority(Priority.CRITICAL)
                .description("string").build();
    }

    @Test
    void addTask() throws Exception {
        mockMvc.perform(post("/tasks").header("Authorization", token)
                .contentType("application/json").content("{\n" +
                        "  \"description\": \"string\",\n" +
                        "  \"priority\": \"CRITICAL\",\n" +
                        "  \"state\": \"TESTING\",\n" +
                        "  \"title\": \"New Task\",\n" +
                        "  \"type\": \"BUG\"\n" +
                        "}")).andExpect(status().isOk());
    }

    @Test
    void addWrongTask() throws Exception {
        mockMvc.perform(post("/tasks").header("Authorization", token)
                .contentType("application/json").content("{}")).andExpect(status().isBadRequest());
    }


}
