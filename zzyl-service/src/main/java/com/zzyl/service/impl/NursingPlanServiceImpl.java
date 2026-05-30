package com.zzyl.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zzyl.base.PageResponse;
import com.zzyl.entity.NursingPlan;
import com.zzyl.mapper.NursingPlanMapper;
import com.zzyl.service.NursingPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NursingPlanServiceImpl implements NursingPlanService {

    @Autowired
    private NursingPlanMapper nursingPlanMapper;

    @Override
    public List<NursingPlan> selectAll() {
        return nursingPlanMapper.selectAll();
    }

    @Override
    public PageResponse<NursingPlan> selectByPage(Integer pageNum, Integer pageSize, String planName, Integer status) {
        PageHelper.startPage(pageNum, pageSize);
        Page<NursingPlan> page = (Page<NursingPlan>) nursingPlanMapper.selectByPage(planName, status);
        return PageResponse.of(page, NursingPlan.class);
    }

    @Override
    public NursingPlan selectById(Long id) {
        return nursingPlanMapper.selectById(id);
    }

    @Override
    public int insert(NursingPlan nursingPlan) {
        nursingPlan.setCreateTime(LocalDateTime.now());
        return nursingPlanMapper.insert(nursingPlan);
    }

    @Override
    public int update(NursingPlan nursingPlan) {
        nursingPlan.setUpdateTime(LocalDateTime.now());
        return nursingPlanMapper.update(nursingPlan);
    }

    @Override
    public int deleteById(Long id) {
        return nursingPlanMapper.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        nursingPlanMapper.updateStatus(id, status);
    }
}
