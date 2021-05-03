package ru.bvb.waves.controllers;

import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.bvb.waves.dto.UserDto;
import ru.bvb.waves.dto.UserUpdateDto;
import ru.bvb.waves.services.UsersService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping
    public List<UserDto> getUsers(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                  @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return usersService.getUsers(page, size);
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return usersService.getUser(id);
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        usersService.deleteUser(id);
        return ResponseEntity.ok(ResponseEntity.noContent());
    }



    @PreAuthorize("hasAuthority('MANAGER')")
    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserUpdateDto userDto) {
        return usersService.updateUser(id, userDto);
    }

}
