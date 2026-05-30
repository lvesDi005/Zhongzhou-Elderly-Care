package com.zzyl.controller;

import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.entity.NursingProject;
import com.zzyl.entity.User;
import com.zzyl.mapper.UserMapper;
import com.zzyl.service.NursingService;
import com.zzyl.vo.NursingProjectVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description NursingController
 * @Author kight-tom
 * @Date 2026-05-28  10:55
 */
@RestController
@RequestMapping("/nursing_project")
public class NursingController {

    @Autowired
    private NursingService nursingService;

    /**
     * 分页查询
     */
    @GetMapping()
    @ApiOperation("分页查询护理项目")
    public ResponseResult<PageResponse<NursingProject>> queryNursingProject(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer status) {

        PageResponse<NursingProject> page = nursingService.queryNursingProject(pageNum, pageSize, name, status);
        return ResponseResult.success(page);
    }

    /**
     * 添加
     */
    @PostMapping()
    @ApiOperation("添加护理项目")
    public ResponseResult<String> addNursingProject(@RequestBody NursingProjectVo nursingProject) {
        nursingService.addNursingProject(nursingProject);
        return ResponseResult.success();
    }

    /**
     * 启用禁用
     */
    @PutMapping("/{id}/status/{status}")
    @ApiOperation("启用禁用护理项目")
    public ResponseResult isEnable(@PathVariable Integer id, @PathVariable Integer status) {
        nursingService.isEnable(id, status);
        return ResponseResult.success();
    }
    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除护理项目")
    public ResponseResult removeNursingProject(@PathVariable Long id) {
        nursingService.removeNursingProject(id);
        return ResponseResult.success();
    }
    /**
     * 修改
     */
    @PutMapping()
    public ResponseResult modifyNursingProject(@RequestBody NursingProjectVo nursingProject) {
        nursingService.modifyNursingProject(nursingProject);
        return ResponseResult.success();
    }
    @GetMapping("/{id}")
    @ApiOperation("查询护理项目")
    public ResponseResult<NursingProject> getNursingProjectById(@PathVariable Long id) {
        NursingProject nursingProject = nursingService.getNursingProjectById(id);
        return ResponseResult.success(nursingProject);
    }

    @GetMapping("/all")
    @ApiOperation("查询所有护理项目")
    public ResponseResult<List<NursingProject>> getAll() {
        return ResponseResult.success(nursingService.getAll());
    }

}
