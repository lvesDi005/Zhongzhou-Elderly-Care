package com.zzyl.mapper;

import com.zzyl.base.PageResponse;
import com.zzyl.entity.NursingProject;
import com.zzyl.vo.NursingProjectVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Description NursingController
 * @Author kight-tom
 * @Date 2026-05-28  10:55
 */
@Mapper
public interface NursingMapper {

    List<NursingProject> queryNursingProject(@Param("name") String name, @Param("status") Integer status);

    int addNursingProject(NursingProject nursingProject);

    void isEnable(Integer id, Integer status);

    @Delete("delete from nursing_project where id=#{id}")
    void removeNursingProject(Long id);


    @Select("select * from nursing_project where id = #{id}")
    NursingProject selectById(Long id);

    @Select("select * from nursing_project where status = 1 order by order_no")
    List<NursingProject> selectAll();

    void modifyNursingProject(NursingProject nursingProject);

}