package ru.bvb.waves.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.bvb.waves.models.User;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);

    @Query("select user FROM User user JOIN user.tokens token WHERE token = ?1")
    Optional<User> findByToken(String token);
}
