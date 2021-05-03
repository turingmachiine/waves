package ru.bvb.waves.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.bvb.waves.models.User;
import ru.bvb.waves.repositories.UsersRepository;
import ru.bvb.waves.security.authentication.TokenAuthentication;
import ru.bvb.waves.security.details.UserDetailsImpl;

import java.util.Optional;

@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UsersRepository usersRepository;

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = authentication.getName();
        Optional<User> userOptional =  usersRepository.findByToken(token);
        User user = userOptional.orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Bad token"));
        UserDetails userDetails = UserDetailsImpl.builder()
                .userId(user.getId())
                .role(user.getRole().toString())
                .name(user.getLogin())
                .build();
        authentication.setAuthenticated(true);
        ((TokenAuthentication)authentication).setUserDetails(userDetails);
        return authentication;

    }

    @Override
    public boolean supports(Class<?> aClass) {
        return TokenAuthentication.class.equals(aClass);
    }
}
