package ru.bvb.waves.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bvb.waves.models.Role;
import ru.bvb.waves.models.Task;
import ru.bvb.waves.models.User;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String login;
    private Role role;

    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .role(user.getRole())
                .build();
    }

    public static List<UserDto> from (List<User> users) {
        return users.stream().map(UserDto::from).collect(Collectors.toList());
    }
}
