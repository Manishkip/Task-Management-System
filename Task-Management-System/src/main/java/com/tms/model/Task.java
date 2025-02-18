package com.tms.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "task")
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    //title, description, status, assigned user

    @Column(name = "task_title")
    private String title;

    @Column(name = "task_description")
    private String description;

    @Column(name = "task_status")  //To-Do, In Progress, Done
    private String status;

    @Column(name = "assigned_user")
    private String assigned_user;

    //For timestamps
    @Column(name = "created_at") // updatable = false
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now(); // Set the creation timestamp
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDate = createdAt.format(formatter);
        updatedAt = createdAt; // Initially set updatedAt to the same as createdAt
    }

    // @PreUpdate is called before the entity is updated
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now(); // Update the updatedAt timestamp
    }
}
