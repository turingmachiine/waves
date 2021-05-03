package ru.bvb.waves.controllers;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GetTasksTests {

    String token;

    @Autowired
    private MockMvc mockMvc;

    @SneakyThrows
    @BeforeEach
    public void setUp() {
        MvcResult result = mockMvc.perform(post("/signIn").contentType("application/json").content("{\n" +
                "  \"login\": \"test4\",\n" +
                "  \"password\": \"qwerty007\"\n" +
                "}")).andExpect(status().isOk()).andReturn();
        token = result.getResponse().getContentAsString();
        token = token.substring(10, token.length() - 2);
    }

    @Test
    void getTasksWithoutToken() throws Exception {
        mockMvc.perform(get("/tasks")).andExpect(status().isForbidden());
    }


    @Test
    void getTasksWithToken() throws Exception {
        mockMvc.perform(get("/tasks").header("Authorization",
                token)).andExpect(status().isOk());
    }

    @Test
    void getTasksWithWrongToken() throws Exception {
        mockMvc.perform(get("/tasks").header("Authorization",
                "ADSADAS")).andExpect(status().isForbidden());
    }

    @Test
    void getTask() throws Exception {
        mockMvc.perform(get("/tasks/1").header("Authorization",
                token)).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getWrongTask() throws Exception {
        mockMvc.perform(get("/tasks/9").header("Authorization",
                token)).andExpect(status().isBadRequest());
    }
}
