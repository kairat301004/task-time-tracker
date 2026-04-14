package com.cdek.tasktimetracker.service;

import com.cdek.tasktimetracker.dao.TaskDao;
import com.cdek.tasktimetracker.entity.Task;
import com.cdek.tasktimetracker.entity.TaskStatus;
import com.cdek.tasktimetracker.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskDao taskDao;

    @Transactional
    public Task createTask(Task task) {
        log.info("Создание новой задачи: {}", task.getTitle());
        task.setStatus(TaskStatus.NEW);
        taskDao.insert(task);

        if (task.getId() != null) {
            Task createdTask = taskDao.findById(task.getId());
            if (createdTask != null) {
                task.setCreatedAt(createdTask.getCreatedAt());
                task.setUpdatedAt(createdTask.getUpdatedAt());
            }
        }

        log.info("Задача создана с id: {}", task.getId());
        return task;
    }

    public Task getTaskById(Long id) {
        log.debug("Получение задачи по id: {}", id);
        Task task = taskDao.findById(id);
        if (task == null) {
            log.error("Задача с id: {} не найдена", id);
            throw new ResourceNotFoundException("Задача не найдена с id: " + id);
        }
        return task;
    }

    @Transactional
    public Task updateTaskStatus(Long id, TaskStatus status) {
        log.info("Обновление статуса задачи [id={}]: новый статус {}", id, status);
        Task task = getTaskById(id);

        validateStatusTransition(task.getStatus(), status);

        taskDao.updateStatus(id, status);

        Task updatedTask = taskDao.findById(id);
        if (updatedTask != null) {
            task.setStatus(updatedTask.getStatus());
            task.setUpdatedAt(updatedTask.getUpdatedAt());
        }

        log.info("Статус задачи {} обновлен успешно", id);
        return task;
    }

    private void validateStatusTransition(TaskStatus current, TaskStatus next) {
        if (current == TaskStatus.DONE && next != TaskStatus.DONE) {
            throw new IllegalStateException("Нельзя изменить статус выполненной задачи");
        }
        if (current == TaskStatus.NEW && next == TaskStatus.DONE) {
            throw new IllegalStateException("Нельзя изменить статус с NEW на DONE напрямую. Используйте сначала IN_PROGRESS");
        }
    }
}
