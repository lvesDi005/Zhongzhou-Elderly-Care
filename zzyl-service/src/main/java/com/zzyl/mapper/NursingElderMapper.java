package com.zzyl.mapper;

import com.zzyl.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NursingElderMapper {

    List<UserVo> selectUserByElderId(Long elderId);
}
