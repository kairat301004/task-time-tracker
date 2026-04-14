package com.cdek.tasktimetracker.controller;

import com.cdek.tasktimetracker.dto.request.TimeRecordRequest;
import com.cdek.tasktimetracker.dto.response.TimeRecordResponse;
import com.cdek.tasktimetracker.entity.TimeRecord;
import com.cdek.tasktimetracker.service.TimeRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/time-records")
@RequiredArgsConstructor
@Tag(name = "Записи времени", description = "Методы для фиксации рабочего времени по задачам")
public class TimeRecordController {
    private final TimeRecordService timeRecordService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать запись времени", description = "Фиксирует время, затраченное сотрудником на задачу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Запись времени успешно создана"),
            @ApiResponse(responseCode = "400", description = "Некорректный временной диапазон или входные данные"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    public TimeRecordResponse createTimeRecord(@Valid @RequestBody TimeRecordRequest request) {
        TimeRecord record = new TimeRecord();
        record.setEmployeeId(request.getEmployeeId());
        record.setTaskId(request.getTaskId());
        record.setStartTime(request.getStartTime());
        record.setEndTime(request.getEndTime());
        record.setDescription(request.getDescription());

        TimeRecord saved = timeRecordService.createTimeRecord(record);
        return toResponse(saved);
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get employee time records", description = "Returns time records for an employee within date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Записи найдены"),
            @ApiResponse(responseCode = "400", description = "Некорректный временной диапазон"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    public List<TimeRecordResponse> getEmployeeRecords(
            @Parameter(description = "ID сотрудника", required = true)
            @PathVariable Long employeeId,

            @Parameter(description = "Дата начала (в формате ISO)", example = "2026-04-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,

            @Parameter(description = "Дата окончания (в формате ISO)", example = "2026-04-30T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        return timeRecordService.getTimeRecordsByEmployeeAndPeriod(employeeId, start, end)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private TimeRecordResponse toResponse(TimeRecord record) {
        long durationMinutes = Duration.between(record.getStartTime(), record.getEndTime()).toMinutes();

        long hours = durationMinutes / 60;
        long minutes = durationMinutes % 60;

        String formatted = String.format("%d ч. %d мин.", hours, minutes);

        return TimeRecordResponse.builder()
                .id(record.getId())
                .employeeId(record.getEmployeeId())
                .taskId(record.getTaskId())
                .startTime(record.getStartTime())
                .endTime(record.getEndTime())
                .description(record.getDescription())
                .durationMinutes(durationMinutes)
                .durationFormatted(formatted)
                .build();
    }

}
