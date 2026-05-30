package com.zzyl.service.impl;

import com.zzyl.base.ResponseResult;
import com.zzyl.dto.BedDto;
import com.zzyl.enums.BasicEnum;
import com.zzyl.exception.BaseException;
import com.zzyl.mapper.BedMapper;
import com.zzyl.service.BedService;
import com.zzyl.utils.UserThreadLocal;
import com.zzyl.vo.BedVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BedServiceImpl implements BedService {

    @Autowired
    private BedMapper bedMapper;

    @Override
    public List<BedVo> getBedsByRoomId(Long roomId) {
        return bedMapper.getBedsByRoomId(roomId);
    }

    @Override
    public String addBed(BedVo bed) {
        // 判断床位编号是否重复
        if (bed.getBedNumber() != null) {
            int count = bedMapper.countByBedNumber(bed.getBedNumber());
            if (count > 0) {
                throw new BaseException(BasicEnum.BED_NUMBER_EXIST);
            }
        }
        bed.setDataState("1");
        if (bed.getBedStatus() == null) {
            bed.setBedStatus(0);
        }
        return bedMapper.addBed(bed) > 0 ? "添加成功" : "添加失败";
    }

    @Override
    public String deleteBed(Integer  id) {
        return bedMapper.deleteBed(id) > 0 ? "删除成功" : "删除失败";
    }

    @Override
    public ResponseResult updateBed(BedVo bed) {
        bed.setUpdateTime(LocalDateTime.now());
        bed.setUpdateBy(UserThreadLocal.getMgtUserId());
        // 判断修改名称是否重复
        if (bed.getName() != null) {
            List<BedDto> list = bedMapper.getBedByName(bed.getName());
            if (list != null && list.size() > 0) {
                throw new BaseException(BasicEnum.BED_NAME_EXIST);
            }
        }
        // 判断修改床位编号是否重复（排除自身）
        if (bed.getBedNumber() != null && bed.getId() != null) {
            int count = bedMapper.countByBedNumberExcludingId(bed.getBedNumber(), bed.getId());
            if (count > 0) {
                throw new BaseException(BasicEnum.BED_NUMBER_EXIST);
            }
        }
        return bedMapper.updateBed(bed) > 0 ? ResponseResult.success("修改成功") : ResponseResult.success("修改失败");
    }

    @Override
    public BedVo getBedById(Long id) {
        return bedMapper.getBedById(id);
    }

}

