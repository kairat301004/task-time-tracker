package com.cdek.tasktimetracker.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Task {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
