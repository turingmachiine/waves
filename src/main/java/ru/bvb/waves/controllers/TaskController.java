package ru.bvb.waves.controllers;

import io.swagger.annotations.ApiImplicitParam;
import org.hibernate.PropertyValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.bvb.waves.dto.StateUpdateDto;
import ru.bvb.waves.dto.TaskCreationDto;
import ru.bvb.waves.dto.TaskDto;
import ru.bvb.waves.dto.TaskEditionDto;
import ru.bvb.waves.models.State;
import ru.bvb.waves.security.details.UserDetailsImpl;
import ru.bvb.waves.services.TaskService;

import java.util.List;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<TaskDto> getTasks(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                  @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return taskService.getTasks(page, size);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody TaskCreationDto taskDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl details = (UserDetailsImpl) authentication.getDetails();
        return ResponseEntity.ok(taskService.addTask(details.getId(), taskDto));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public TaskDto getTask(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public TaskDto updateTask(@PathVariable Long id, @RequestBody TaskEditionDto taskDto) {
        return taskService.updateTask(id, taskDto);
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(ResponseEntity.noContent(), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/state")
    public ResponseEntity<?> updateState(@PathVariable Long id, StateUpdateDto state) {
        return ResponseEntity.ok(taskService.updateState(id, state));
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/search")
    public List<TaskDto> searchTasks(String query, @RequestParam(name = "page", defaultValue = "0") Integer page,
                                     @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return taskService.searchTask(query, page, size);
    }


}
