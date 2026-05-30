package com.zzyl.mapper;

import com.zzyl.entity.NursingPlan;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface NursingPlanMapper {

    List<NursingPlan> selectByPage(@Param("planName") String planName, @Param("status") Integer status);

    @Select("select * from nursing_plan")
    List<NursingPlan> selectAll();

    @Select("select * from nursing_plan where id = #{id}")
    NursingPlan selectById(Long id);

    int insert(NursingPlan nursingPlan);

    int update(NursingPlan nursingPlan);

    @Delete("delete from nursing_plan where id = #{id}")
    int deleteById(Long id);

    @Update("update nursing_plan set status = #{status} where id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
