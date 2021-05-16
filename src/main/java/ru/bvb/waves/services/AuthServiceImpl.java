package ru.bvb.waves.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.bvb.waves.dto.PasswordDto;
import ru.bvb.waves.dto.SignInDto;
import ru.bvb.waves.dto.SignUpDto;
import ru.bvb.waves.dto.TokenDto;
import ru.bvb.waves.models.Role;
import ru.bvb.waves.models.User;
import ru.bvb.waves.repositories.UsersRepository;
import ru.bvb.waves.security.details.UserDetailsImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;



    @Override
    public TokenDto signUp(SignUpDto signUpDto) {
        User user = User.builder()
                .login(signUpDto.getLogin())
                .hashPassword(passwordEncoder.encode(signUpDto.getPassword()))
                .tokens(new ArrayList<>())
                .build();
        usersRepository.save(user);
        TokenDto token = getToken(user);
        user.getTokens().add(token.getToken());
        usersRepository.save(user);
        return getToken(user);
    }

    @Override
    public TokenDto signIn(SignInDto signInDto) {
        Optional<User> userOptional = usersRepository.findByLogin(signInDto.getLogin());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(signInDto.getPassword(), user.getHashPassword())) {
                TokenDto token = getToken(user);
                List<String> tokens = user.getTokens();
                if (!tokens.contains(token.getToken())) {
                    tokens.add(token.getToken());
                }
                usersRepository.save(user);
                return token;
            } else throw new IllegalArgumentException("Wrong email/password");
        } else throw new IllegalArgumentException("User not found");

    }

    @Override
    public void reset(PasswordDto passwordDto) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getDetails();
        User user = usersRepository.findByLogin(userDetails.getUsername()).orElseThrow(() -> new IllegalArgumentException("UserNotFound"));
        if (passwordEncoder.matches(passwordDto.getOld(), user.getHashPassword())) {
            user.setHashPassword(passwordEncoder.encode(passwordDto.getNewPass()));
            user.setTokens(new ArrayList<>());
            usersRepository.save(user);
        }
    }

    private TokenDto getToken(User user) {
        String token = Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("login", user.getLogin())
                .claim("role", user.getRole())
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        return new TokenDto(token);
    }
}
