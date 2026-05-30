package com.zzyl.service;

import com.zzyl.base.PageResponse;
import com.zzyl.entity.NursingTask;

public interface NursingTaskService {
    PageResponse<NursingTask> selectByPage(Integer pageNum, Integer pageSize, String elderName, Integer status, Integer planId, Long nurseId, Long projectId, String startTime, String endTime);
    NursingTask selectById(Long id);
    void executeTask(Long taskId, String estimatedServerTime, String mark, String taskImage);
    void cancelTask(Long taskId, String reason);
    void updateExecuteTime(Long taskId, String estimatedServerTime);
}
