package com.cdek.tasktimetracker.service;

import com.cdek.tasktimetracker.dao.TimeRecordDao;
import com.cdek.tasktimetracker.entity.Task;
import com.cdek.tasktimetracker.entity.TimeRecord;
import com.cdek.tasktimetracker.exception.InvalidTimeRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeRecordServiceTest {

    @Mock
    private TimeRecordDao timeRecordDao;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TimeRecordService timeRecordService;

    private TimeRecord timeRecord;
    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);

        timeRecord = new TimeRecord();
        timeRecord.setEmployeeId(100L);
        timeRecord.setTaskId(1L);
        timeRecord.setStartTime(LocalDateTime.now().minusHours(2));
        timeRecord.setEndTime(LocalDateTime.now());
    }

    @Test
    void createTimeRecord_Success() {
        when(taskService.getTaskById(1L)).thenReturn(task);

        doAnswer(invocation -> {
            TimeRecord tr = invocation.getArgument(0);
            tr.setId(1L);
            return null;
        }).when(timeRecordDao).insert(any(TimeRecord.class));

        when(timeRecordDao.findById(1L)).thenReturn(timeRecord);

        TimeRecord result = timeRecordService.createTimeRecord(timeRecord);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(timeRecordDao, times(1)).insert(timeRecord);
        verify(timeRecordDao, times(1)).findById(1L);
    }

    @Test
    void createTimeRecord_InvalidTimeRange_ThrowsException() {
        timeRecord.setEndTime(timeRecord.getStartTime().minusHours(1));

        assertThatThrownBy(() -> timeRecordService.createTimeRecord(timeRecord))
                .isInstanceOf(InvalidTimeRangeException.class)
                .hasMessageContaining("Дата окончания должна быть позже даты начала");

        verify(timeRecordDao, never()).insert(any());
    }

    @Test
    void createTimeRecord_TaskNotFound_ThrowsException() {
        when(taskService.getTaskById(999L)).thenThrow(new RuntimeException("Задача не найдена"));
        timeRecord.setTaskId(999L);

        assertThatThrownBy(() -> timeRecordService.createTimeRecord(timeRecord))
                .isInstanceOf(RuntimeException.class);

        verify(timeRecordDao, never()).insert(any());
    }

    @Test
    void getTimeRecordsByEmployeeAndPeriod_Success() {
        List<TimeRecord> expectedRecords = List.of(timeRecord);
        when(timeRecordDao.findByEmployeeAndPeriod(eq(100L), any(), any())).thenReturn(expectedRecords);

        List<TimeRecord> result = timeRecordService.getTimeRecordsByEmployeeAndPeriod(
                100L,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now()
        );

        assertThat(result).hasSize(1);
        verify(timeRecordDao, times(1)).findByEmployeeAndPeriod(anyLong(), any(), any());
    }

    @Test
    void getTimeRecordsByEmployeeAndPeriod_InvalidDateRange_ThrowsException() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.minusDays(1);

        assertThatThrownBy(() -> timeRecordService.getTimeRecordsByEmployeeAndPeriod(100L, start, end))
                .isInstanceOf(InvalidTimeRangeException.class)
                .hasMessageContaining("Дата начала должна быть раньше даты окончания");

        verify(timeRecordDao, never()).findByEmployeeAndPeriod(anyLong(), any(), any());
    }
}