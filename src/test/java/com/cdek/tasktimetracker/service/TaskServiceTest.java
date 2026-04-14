package com.cdek.tasktimetracker.service;

import com.cdek.tasktimetracker.dao.TaskDao;
import com.cdek.tasktimetracker.entity.Task;
import com.cdek.tasktimetracker.entity.TaskStatus;
import com.cdek.tasktimetracker.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskDao taskDao;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setStatus(TaskStatus.NEW);
    }

    @Test
    void createTask_Success() {
        Task newTask = new Task();
        newTask.setTitle("New Task");

        doAnswer(invocation -> {
            Task t = invocation.getArgument(0);
            t.setId(1L);
            return null;
        }).when(taskDao).insert(any(Task.class));

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("New Task");
        savedTask.setStatus(TaskStatus.NEW);
        when(taskDao.findById(1L)).thenReturn(savedTask);

        Task result = taskService.createTask(newTask);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(taskDao, times(1)).insert(newTask);
        verify(taskDao, times(1)).findById(1L);
    }

    @Test
    void getTaskById_Success() {
        when(taskDao.findById(1L)).thenReturn(task);

        Task result = taskService.getTaskById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(taskDao, times(1)).findById(1L);
    }

    @Test
    void getTaskById_NotFound_ThrowsException() {
        when(taskDao.findById(999L)).thenReturn(null);

        assertThatThrownBy(() -> taskService.getTaskById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Задача не найдена с id: 999");
    }

    @Test
    void updateTaskStatus_Success() {
        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setStatus(TaskStatus.IN_PROGRESS);

        when(taskDao.findById(1L)).thenReturn(task).thenReturn(updatedTask);
        doNothing().when(taskDao).updateStatus(1L, TaskStatus.IN_PROGRESS);

        Task result = taskService.updateTaskStatus(1L, TaskStatus.IN_PROGRESS);

        assertThat(result.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        verify(taskDao, times(1)).updateStatus(1L, TaskStatus.IN_PROGRESS);
    }

    @Test
    void updateTaskStatus_InvalidTransition_ThrowsException() {
        when(taskDao.findById(1L)).thenReturn(task);

        assertThatThrownBy(() -> taskService.updateTaskStatus(1L, TaskStatus.DONE))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Нельзя изменить статус с NEW на DONE напрямую");

        verify(taskDao, never()).updateStatus(anyLong(), any());
    }
}