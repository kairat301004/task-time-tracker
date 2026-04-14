package com.cdek.tasktimetracker.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TimeRecord {
    private Long id;
    private Long employeeId;
    private Long taskId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private LocalDateTime createdAt;
}
