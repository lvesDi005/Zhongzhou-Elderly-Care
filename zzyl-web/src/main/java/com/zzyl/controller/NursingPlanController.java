package com.zzyl.controller;

import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.entity.NursingPlan;
import com.zzyl.service.NursingPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nursing")
@Api(tags = "护理计划")
public class NursingPlanController {

    @Autowired
    private NursingPlanService nursingPlanService;

    @GetMapping("/plan")
    @ApiOperation("查询所有护理计划")
    public ResponseResult<List<NursingPlan>> selectAll() {
        return ResponseResult.success(nursingPlanService.selectAll());
    }

    @GetMapping("/plan/search")
    @ApiOperation("分页查询护理计划")
    public ResponseResult<PageResponse<NursingPlan>> selectByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String planName,
            @RequestParam(required = false) Integer status) {
        return ResponseResult.success(nursingPlanService.selectByPage(pageNum, pageSize, planName, status));
    }

    @GetMapping("/plan/{id}")
    @ApiOperation("查询护理计划详情")
    public ResponseResult<NursingPlan> selectById(@PathVariable Long id) {
        return ResponseResult.success(nursingPlanService.selectById(id));
    }

    @PostMapping("/plan")
    @ApiOperation("添加护理计划")
    public ResponseResult add(@RequestBody NursingPlan nursingPlan) {
        return nursingPlanService.insert(nursingPlan) > 0 ? ResponseResult.success() : ResponseResult.error();
    }

    @PutMapping("/plan/{id}")
    @ApiOperation("修改护理计划")
    public ResponseResult update(@PathVariable Long id, @RequestBody NursingPlan nursingPlan) {
        nursingPlan.setId(id);
        return nursingPlanService.update(nursingPlan) > 0 ? ResponseResult.success() : ResponseResult.error();
    }

    @DeleteMapping("/plan/{id}")
    @ApiOperation("删除护理计划")
    public ResponseResult delete(@PathVariable Long id) {
        return nursingPlanService.deleteById(id) > 0 ? ResponseResult.success() : ResponseResult.error();
    }

    @PutMapping("/{id}/status/{status}")
    @ApiOperation("启用禁用护理计划")
    public ResponseResult updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        nursingPlanService.updateStatus(id, status);
        return ResponseResult.success();
    }
}
