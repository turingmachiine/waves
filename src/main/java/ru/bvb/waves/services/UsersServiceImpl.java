package ru.bvb.waves.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.bvb.waves.dto.UserDto;
import ru.bvb.waves.dto.UserUpdateDto;
import ru.bvb.waves.models.Role;
import ru.bvb.waves.models.User;
import ru.bvb.waves.repositories.UsersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.bvb.waves.dto.UserDto.from;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public List<UserDto> getUsers(Integer page, Integer size) {
        return from(usersRepository.findAll(PageRequest.of(page, size)).getContent());
    }

    @Override
    public UserDto getUser(Long id) {
        return from(usersRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found")));
    }

    @Override
    public UserDto updateUser(Long userId, UserUpdateDto userDto) {
        Optional<User> userOptional = usersRepository.findById(userId);
        User user = userOptional.orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (userDto.getLogin() != null) {
            user.setLogin(userDto.getLogin());
        }
        if (userDto.getRole() != null) {
            user.setRole(Role.valueOf(userDto.getRole()));
            user.setTokens(new ArrayList<>());
        }
        usersRepository.save(user);
        return from(user);
    }

    @Override
    public void deleteUser(Long id) {
        usersRepository.deleteById(id);
    }
}
