package com.zzyl.mapper;

import com.zzyl.entity.NursingTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface NursingTaskMapper {

    List<NursingTask> selectByPage(@Param("elderName") String elderName,
                                   @Param("status") Integer status,
                                   @Param("planId") Integer planId);

    @Select("select * from nursing_task where id = #{id}")
    NursingTask selectById(Long id);

    @Update("update nursing_task set status = 1, execute_by = #{executeBy}, execute_time = NOW(), complete_time = NOW(), task_image = #{taskImage}, remark = #{remark} where id = #{id}")
    void executeTask(NursingTask task);

    @Update("update nursing_task set status = 2, cancel_reason = #{cancelReason} where id = #{taskId}")
    void cancelTask(@Param("taskId") Long taskId, @Param("cancelReason") String cancelReason);

    @Update("update nursing_task set execute_time = #{executeTime} where id = #{taskId}")
    void updateExecuteTime(@Param("taskId") Long taskId, @Param("executeTime") String executeTime);
}
