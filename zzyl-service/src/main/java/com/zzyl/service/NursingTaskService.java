package com.zzyl.service;

import com.zzyl.base.PageResponse;
import com.zzyl.entity.NursingTask;

public interface NursingTaskService {
    PageResponse<NursingTask> selectByPage(Integer pageNum, Integer pageSize, String elderName, Integer status, Integer planId);
    NursingTask selectById(Long id);
    void executeTask(Long taskId, String executeBy, String executeTime, String completeTime, String taskImage, String mark);
    void cancelTask(Long taskId, String reason);
    void updateExecuteTime(Long taskId, String estimatedServerTime);
}
