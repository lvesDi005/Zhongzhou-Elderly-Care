package com.zzyl.service;

import com.zzyl.base.ResponseResult;
import com.zzyl.dto.BedDto;
import com.zzyl.vo.BedVo;

import java.util.List;

/**
 * 服务接口：BedService（床位管理服务）
 */
public interface BedService {


    /**
     * 通过房间ID检索床位
     * @param roomId 房间ID
     * @return 床位视图对象列表
     */
    List<BedVo> getBedsByRoomId(Long roomId);

    String addBed(BedVo bed);

    String deleteBed(Integer bedId);

    ResponseResult updateBed(BedVo bed);

    BedVo getBedById(Long id);
}
