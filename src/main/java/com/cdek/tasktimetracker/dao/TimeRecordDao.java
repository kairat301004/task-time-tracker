package com.cdek.tasktimetracker.dao;

import com.cdek.tasktimetracker.entity.TimeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TimeRecordDao {

    void insert(TimeRecord timeRecord);

    TimeRecord findById(@Param("id") Long id);

    List<TimeRecord> findByEmployeeAndPeriod(@Param("employeeId") Long employeeId,
                                             @Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);

}
