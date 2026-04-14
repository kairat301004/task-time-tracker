package com.cdek.tasktimetracker.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private int statusCode;
    private String error;
    private LocalDateTime timestamp;
    private String path;
}
