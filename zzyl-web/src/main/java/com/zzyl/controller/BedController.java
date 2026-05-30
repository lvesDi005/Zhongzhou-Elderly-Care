package com.zzyl.controller;

import com.zzyl.base.ResponseResult;
import com.zzyl.dto.BedDto;
import com.zzyl.service.BedService;
import com.zzyl.vo.BedVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bed")
@Api(tags = "床位管理相关接口")
public class BedController extends BaseController {

    @Autowired
    private BedService bedService;

    @GetMapping("/read/room/{roomId}")
    @ApiOperation(value = "根据房间id查询床位", notes = "传入房间id")
    public ResponseResult<List<BedVo>> readBedByRoomId(
            @ApiParam(value = "房间ID", required = true) @PathVariable("roomId") Long roomId) {
        List<BedVo> beds = bedService.getBedsByRoomId(roomId);
        return success(beds);
    }
    @GetMapping("/read/{id}")
    @ApiOperation(value = "根据id查询床位", notes = "传入床位id")
    public ResponseResult<BedVo> getBedById(
            @ApiParam(value = "床位ID", required = true) @PathVariable("id") Long id) {
        return success(bedService.getBedById(id));
    }

    /**
     * 新增床位
     *
     */
    @ApiOperation(value = "新增床位", notes = "传入床位信息")
    @PostMapping("/create")
    public ResponseResult addBed(@RequestBody BedVo bed) {
        return success(bedService.addBed(bed));
    }

    /**
     * 删除床位
     *
     */
    @ApiOperation(value = "删除床位", notes = "传入床位信息")
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteBed(@PathVariable  Integer  id) {
        return success(bedService.deleteBed(id));
    }

    /**
     * 修改床位
     */
    @ApiOperation(value = "修改床位", notes = "传入床位信息")
    @PutMapping("/update")
    public ResponseResult updateBed(@RequestBody BedVo bed) {
        return success(bedService.updateBed(bed));
    }


}
