package com.cdek.tasktimetracker.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TimeRecordRequest {

    @NotNull(message = "id сотрудника обязателен")
    @Positive(message = "id сотрудника должен быть положительным")
    private Long employeeId;

    @NotNull(message = "id задачи обязателен")
    @Positive(message = "id задачи должен быть положительным")
    private Long taskId;

    @NotNull(message = "Время старта обязателен")
    private LocalDateTime startTime;

    @NotNull(message = "Время окончания обязателен")
    private LocalDateTime endTime;

    @Size(max = 500, message = "Описание не должно превышать 500 символов")
    private String description;
}
