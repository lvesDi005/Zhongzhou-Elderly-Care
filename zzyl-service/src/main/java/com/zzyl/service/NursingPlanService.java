package com.zzyl.service;

import com.zzyl.base.PageResponse;
import com.zzyl.entity.NursingPlan;

import java.util.List;

public interface NursingPlanService {
    List<NursingPlan> selectAll();
    PageResponse<NursingPlan> selectByPage(Integer pageNum, Integer pageSize, String planName, Integer status);
    NursingPlan selectById(Long id);
    int insert(NursingPlan nursingPlan);
    int update(NursingPlan nursingPlan);
    int deleteById(Long id);
    void updateStatus(Long id, Integer status);
}
