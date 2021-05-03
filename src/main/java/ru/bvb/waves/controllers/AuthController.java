package ru.bvb.waves.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.bvb.waves.dto.PasswordDto;
import ru.bvb.waves.dto.SignInDto;
import ru.bvb.waves.dto.SignUpDto;
import ru.bvb.waves.dto.TokenDto;
import ru.bvb.waves.services.AuthService;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signUp")
    public TokenDto signUp(@RequestBody SignUpDto signUpDto) {
        return authService.signUp(signUpDto);
    }

    @PostMapping("/signIn")
    public TokenDto signIn(@RequestBody SignInDto signInDto) {
        return authService.signIn(signInDto);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordDto passwordDto) {
        authService.reset(passwordDto);
        return ResponseEntity.noContent().build();
    }
}
