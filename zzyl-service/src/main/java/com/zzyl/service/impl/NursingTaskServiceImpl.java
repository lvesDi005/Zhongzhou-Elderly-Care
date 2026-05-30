package com.zzyl.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zzyl.base.PageResponse;
import com.zzyl.entity.NursingTask;
import com.zzyl.mapper.NursingTaskMapper;
import com.zzyl.service.NursingTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NursingTaskServiceImpl implements NursingTaskService {

    @Autowired
    private NursingTaskMapper nursingTaskMapper;

    @Override
    public PageResponse<NursingTask> selectByPage(Integer pageNum, Integer pageSize, String elderName, Integer status, Integer planId, Long nurseId, Long projectId, String startTime, String endTime) {
        PageHelper.startPage(pageNum, pageSize);
        Page<NursingTask> page = (Page<NursingTask>) nursingTaskMapper.selectByPage(elderName, status, planId, nurseId, projectId, startTime, endTime);
        return PageResponse.of(page, NursingTask.class);
    }

    @Override
    public NursingTask selectById(Long id) {
        return nursingTaskMapper.selectById(id);
    }

    @Override
    public void executeTask(Long taskId, String estimatedServerTime, String mark, String taskImage) {
        NursingTask task = new NursingTask();
        task.setId(taskId);
        task.setTaskImage(taskImage);
        task.setMark(mark);
        if (estimatedServerTime != null) {
            task.setEstimatedServerTime(java.time.LocalDateTime.parse(estimatedServerTime.replace(" ", "T")));
        }
        nursingTaskMapper.executeTask(task);
    }

    @Override
    public void cancelTask(Long taskId, String reason) {
        nursingTaskMapper.cancelTask(taskId, reason);
    }

    @Override
    public void updateExecuteTime(Long taskId, String estimatedServerTime) {
        if (estimatedServerTime != null && !estimatedServerTime.isEmpty()) {
            try {
                java.time.LocalDateTime.parse(estimatedServerTime.replace(" ", "T"));
            } catch (Exception e) {
                // already in correct format
            }
        }
        nursingTaskMapper.updateExecuteTime(taskId, estimatedServerTime);
    }
}
