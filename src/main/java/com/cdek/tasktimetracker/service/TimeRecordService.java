package com.cdek.tasktimetracker.service;

import com.cdek.tasktimetracker.dao.TimeRecordDao;
import com.cdek.tasktimetracker.entity.TimeRecord;
import com.cdek.tasktimetracker.exception.InvalidTimeRangeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimeRecordService {
    private final TimeRecordDao timeRecordDao;
    private final TaskService taskService;

    @Transactional
    public TimeRecord createTimeRecord(TimeRecord timeRecord) {
        log.info("Создание записи времени для сотрудника {} по задаче {}",
                timeRecord.getEmployeeId(), timeRecord.getTaskId());

        taskService.getTaskById(timeRecord.getTaskId());

        validateTimeRange(timeRecord.getStartTime(), timeRecord.getEndTime());

        timeRecordDao.insert(timeRecord);

        // Перезагружаем запись, чтобы получить created_at
        TimeRecord createdRecord = timeRecordDao.findById(timeRecord.getId());
        if (createdRecord != null) {
            timeRecord.setCreatedAt(createdRecord.getCreatedAt());
        }

        log.info("Запись времени с id: {} создана", timeRecord.getId());
        return timeRecord;
    }

    public List<TimeRecord> getTimeRecordsByEmployeeAndPeriod(Long employeeId, LocalDateTime start, LocalDateTime end) {
        log.info("Получение записей времени для сотрудника {} с {} по {}", employeeId, start, end);

        if (start.isAfter(end)) {
            throw new InvalidTimeRangeException("Дата начала должна быть раньше даты окончания");
        }

        return timeRecordDao.findByEmployeeAndPeriod(employeeId, start, end);
    }

    private void validateTimeRange(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new InvalidTimeRangeException("Дата начала и дата окончания обязательны для заполнения");
        }
        if (end.isBefore(start)) {
            throw new InvalidTimeRangeException("Дата окончания должна быть позже даты начала");
        }
        if (start.isAfter(LocalDateTime.now())) {
            throw new InvalidTimeRangeException("Дата начала не может быть в будущем");
        }
        if (java.time.Duration.between(start, end).toHours() > 24) {
            throw new InvalidTimeRangeException("Длительность записи не может превышать 24 часа");
        }
    }
}
