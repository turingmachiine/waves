package ru.bvb.waves.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TaskType type;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private State state;

    @Column(nullable = false)
    private String title;

    @Enumerated(value = EnumType.STRING)
    private Priority priority;

    private String description;

    @ManyToOne
    private User creator;

    @ManyToOne
    private User executor;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "block_table",
            joinColumns = @JoinColumn(name = "block_task"),
            inverseJoinColumns = @JoinColumn(name = "blocked_task"))
    private List<Task> blockedBy;

    @ManyToMany(mappedBy = "blockedBy")
    private List<Task> blocks;

    @PreUpdate
    public void setUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
