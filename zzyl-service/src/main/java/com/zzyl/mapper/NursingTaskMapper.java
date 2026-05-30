package com.zzyl.mapper;

import com.zzyl.entity.NursingTask;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NursingTaskMapper {

    List<NursingTask> selectByPage(@Param("elderName") String elderName,
                                   @Param("status") Integer status,
                                   @Param("planId") Integer planId,
                                   @Param("nurseId") Long nurseId,
                                   @Param("projectId") Long projectId,
                                   @Param("startTime") String startTime,
                                   @Param("endTime") String endTime);

    @Select("select * from nursing_task where id = #{id}")
    NursingTask selectById(Long id);

    @Update("update nursing_task set status = 2, task_image = #{taskImage}, mark = #{mark}, estimated_server_time = #{estimatedServerTime}, update_time = NOW() where id = #{id}")
    void executeTask(NursingTask task);

    @Update("update nursing_task set status = 3, cancel_reason = #{cancelReason} where id = #{taskId}")
    void cancelTask(@Param("taskId") Long taskId, @Param("cancelReason") String cancelReason);

    @Update("update nursing_task set estimated_server_time = #{estimatedServerTime} where id = #{taskId}")
    void updateExecuteTime(@Param("taskId") Long taskId, @Param("estimatedServerTime") String estimatedServerTime);
}
