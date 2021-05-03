package ru.bvb.waves.services;

import ru.bvb.waves.dto.UserDto;
import ru.bvb.waves.dto.UserUpdateDto;

import java.util.List;

public interface UsersService {
    List<UserDto> getUsers(Integer page, Integer size);
    UserDto getUser(Long id);
    UserDto updateUser(Long id, UserUpdateDto userDto);
    void deleteUser(Long id);

}
