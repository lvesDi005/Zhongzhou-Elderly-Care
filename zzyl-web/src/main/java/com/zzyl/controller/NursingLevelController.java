package com.zzyl.controller;

import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.entity.NursingLevel;
import com.zzyl.service.NursingLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nursingLevel")
@Api(tags = "护理等级")
public class NursingLevelController {

    @Autowired
    private NursingLevelService nursingLevelService;

    @GetMapping("/listAll")
    @ApiOperation("查询所有护理等级")
    public ResponseResult<List<NursingLevel>> listAll() {
        return ResponseResult.success(nursingLevelService.selectAll());
    }

    @GetMapping("/listByPage")
    @ApiOperation("分页查询护理等级")
    public ResponseResult<PageResponse<NursingLevel>> listByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer status) {
        return ResponseResult.success(nursingLevelService.selectByPage(pageNum, pageSize, name, status));
    }

    @GetMapping("/{id}")
    @ApiOperation("查询护理等级详情")
    public ResponseResult<NursingLevel> selectById(@PathVariable Long id) {
        return ResponseResult.success(nursingLevelService.selectById(id));
    }

    @PostMapping("/insert")
    @ApiOperation("添加护理等级")
    public ResponseResult insert(@RequestBody NursingLevel nursingLevel) {
        return nursingLevelService.insert(nursingLevel) > 0 ? ResponseResult.success() : ResponseResult.error();
    }

    @PutMapping("/update")
    @ApiOperation("修改护理等级")
    public ResponseResult update(@RequestBody NursingLevel nursingLevel) {
        return nursingLevelService.update(nursingLevel) > 0 ? ResponseResult.success() : ResponseResult.error();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除护理等级")
    public ResponseResult delete(@PathVariable Long id) {
        return nursingLevelService.deleteById(id) > 0 ? ResponseResult.success() : ResponseResult.error();
    }

    @PutMapping("/{id}/status/{status}")
    @ApiOperation("启用禁用护理等级")
    public ResponseResult updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        nursingLevelService.updateStatus(id, status);
        return ResponseResult.success();
    }
}
