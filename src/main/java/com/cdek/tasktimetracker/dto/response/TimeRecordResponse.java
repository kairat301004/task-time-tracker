package com.cdek.tasktimetracker.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TimeRecordResponse {
    private Long id;
    private Long employeeId;
    private Long taskId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private Long durationMinutes;
    private String durationFormatted;
}
