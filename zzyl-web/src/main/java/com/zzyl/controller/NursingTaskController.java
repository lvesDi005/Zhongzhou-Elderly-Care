package com.zzyl.controller;

import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.entity.NursingTask;
import com.zzyl.service.NursingTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nursingTask")
@Api(tags = "护理任务")
public class NursingTaskController {

    @Autowired
    private NursingTaskService nursingTaskService;

    @GetMapping("/page")
    @ApiOperation("分页查询护理任务")
    public ResponseResult<PageResponse<NursingTask>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String elderName,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer planId) {
        return ResponseResult.success(nursingTaskService.selectByPage(pageNum, pageSize, elderName, status, planId));
    }

    @PutMapping("/do")
    @ApiOperation("执行护理任务")
    public ResponseResult execute(@RequestParam Long taskId,
                                   @RequestParam(required = false) String estimatedServerTime,
                                   @RequestParam(required = false) String mark,
                                   @RequestParam(required = false) String taskImage) {
        nursingTaskService.executeTask(taskId, null, estimatedServerTime, estimatedServerTime, taskImage, mark);
        return ResponseResult.success();
    }

    @PutMapping("/cancel")
    @ApiOperation("取消护理任务")
    public ResponseResult cancel(@RequestParam Long taskId, @RequestParam String reason) {
        nursingTaskService.cancelTask(taskId, reason);
        return ResponseResult.success();
    }

    @PutMapping("/updateTime")
    @ApiOperation("修改护理任务执行时间")
    public ResponseResult updateTime(@RequestParam Long taskId, @RequestParam String estimatedServerTime) {
        nursingTaskService.updateExecuteTime(taskId, estimatedServerTime);
        return ResponseResult.success();
    }

    @GetMapping
    @ApiOperation("查询护理任务详情")
    public ResponseResult<NursingTask> detail(@RequestParam Long id) {
        return ResponseResult.success(nursingTaskService.selectById(id));
    }
}
