package ru.bvb.waves.services;

import ru.bvb.waves.dto.PasswordDto;
import ru.bvb.waves.dto.SignInDto;
import ru.bvb.waves.dto.SignUpDto;
import ru.bvb.waves.dto.TokenDto;

public interface AuthService {
    TokenDto signUp(SignUpDto signUpDto);
    TokenDto signIn(SignInDto signInDto);

    void reset(PasswordDto passwordDto);
}
