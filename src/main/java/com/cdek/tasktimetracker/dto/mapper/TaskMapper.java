package com.cdek.tasktimetracker.dto.mapper;

import com.cdek.tasktimetracker.dto.request.TaskCreateRequest;
import com.cdek.tasktimetracker.dto.response.TaskResponse;
import com.cdek.tasktimetracker.entity.Task;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(TaskCreateRequest request);

    @Mapping(target = "status", expression = "java(task.getStatus().name())")
    TaskResponse toResponse(Task task);
}
