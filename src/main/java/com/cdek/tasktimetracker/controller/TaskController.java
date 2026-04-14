package com.cdek.tasktimetracker.controller;

import com.cdek.tasktimetracker.dto.mapper.TaskMapper;
import com.cdek.tasktimetracker.dto.request.TaskCreateRequest;
import com.cdek.tasktimetracker.dto.response.TaskResponse;
import com.cdek.tasktimetracker.entity.TaskStatus;
import com.cdek.tasktimetracker.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Управление задачами", description = "Методы для управления рабочими задачами")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать новую задачу", description = "Создает задачу со статусом NEW")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Задача успешно создана"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    public TaskResponse createTask(@Valid @RequestBody TaskCreateRequest request) {
        return taskMapper.toResponse(taskService.createTask(taskMapper.toEntity(request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить задачу по ID", description = "Возвращает данные одной задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача найдена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    public TaskResponse getTask(@Parameter(description = "ID задачи", required = true)
                                @PathVariable Long id) {
        return taskMapper.toResponse(taskService.getTaskById(id));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Обновить статус задачи", description = "Изменяет статус задачи (NEW/IN_PROGRESS/DONE)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Недопустимый переход между статусами"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    public TaskResponse updateStatus(@PathVariable Long id, @RequestParam TaskStatus status) {
        return taskMapper.toResponse(taskService.updateTaskStatus(id, status));
    }
}
