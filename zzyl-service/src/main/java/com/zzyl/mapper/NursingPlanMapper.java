package com.zzyl.mapper;

import com.zzyl.entity.NursingPlan;
import com.zzyl.entity.NursingProjectPlan;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NursingPlanMapper {

    List<NursingPlan> selectByPage(@Param("planName") String planName, @Param("status") Integer status);

    @Select("select * from nursing_plan")
    List<NursingPlan> selectAll();

    NursingPlan selectById(Long id);

    int insert(NursingPlan nursingPlan);

    int update(NursingPlan nursingPlan);

    @Delete("delete from nursing_plan where id = #{id}")
    int deleteById(Long id);

    @Update("update nursing_plan set status = #{status} where id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Select("select npp.*, np.name as project_name from nursing_project_plan npp left join nursing_project np on npp.project_id = np.id where npp.plan_id = #{planId}")
    List<NursingProjectPlan> selectProjectPlansByPlanId(@Param("planId") Integer planId);
}
