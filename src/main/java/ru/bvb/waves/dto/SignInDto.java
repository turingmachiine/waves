package ru.bvb.waves.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInDto {
    String login;
    String password;
}
