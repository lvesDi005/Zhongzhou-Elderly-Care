package com.zzyl.mapper;

import com.zzyl.entity.NursingLevel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NursingLevelMapper {

    List<NursingLevel> selectByPage(@Param("name") String name, @Param("status") Integer status);

    @Select("select * from nursing_level")
    List<NursingLevel> selectAll();

    NursingLevel selectById(Long id);

    int insert(NursingLevel nursingLevel);

    int update(NursingLevel nursingLevel);

    @Delete("delete from nursing_level where id = #{id}")
    int deleteById(Long id);

    @Update("update nursing_level set status = #{status} where id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
