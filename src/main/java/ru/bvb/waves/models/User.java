package ru.bvb.waves.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "wave_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String login;

    private String hashPassword;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "creator")
    private List<Task> createdTasks;

    @OneToMany(mappedBy = "executor")
    private List<Task> appointedTasks;

    @ElementCollection
    @CollectionTable(name = "token", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "value")
    private List<String> tokens;
}
