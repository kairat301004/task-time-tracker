package com.cdek.tasktimetracker.dao;

import com.cdek.tasktimetracker.entity.Task;
import com.cdek.tasktimetracker.entity.TaskStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TaskDao {

    void insert(Task task);

    Task findById(@Param("id") Long id);

    void updateStatus(@Param("id") Long id, @Param("status") TaskStatus status);
}
