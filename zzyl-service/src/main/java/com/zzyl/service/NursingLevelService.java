package com.zzyl.service;

import com.zzyl.base.PageResponse;
import com.zzyl.entity.NursingLevel;

import java.util.List;

public interface NursingLevelService {
    List<NursingLevel> selectAll();
    PageResponse<NursingLevel> selectByPage(Integer pageNum, Integer pageSize, String name, Integer status);
    NursingLevel selectById(Long id);
    int insert(NursingLevel nursingLevel);
    int update(NursingLevel nursingLevel);
    int deleteById(Long id);
    void updateStatus(Long id, Integer status);
}
