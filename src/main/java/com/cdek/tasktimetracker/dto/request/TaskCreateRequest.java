package com.cdek.tasktimetracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskCreateRequest {
    @NotBlank(message = "Заголовок задачи обязателен")
    @Size(min = 3, max = 255, message = "Заголовок задачи должен содержать от 3 до 255 символов")
    private String title;

    @Size(max = 1000, message = "Описание задачи не должно превышать 1000 символов")
    private String description;
}
