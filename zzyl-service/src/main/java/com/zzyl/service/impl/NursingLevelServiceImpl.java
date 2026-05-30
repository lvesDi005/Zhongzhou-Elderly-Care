package com.zzyl.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zzyl.base.PageResponse;
import com.zzyl.entity.NursingLevel;
import com.zzyl.mapper.NursingLevelMapper;
import com.zzyl.service.NursingLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NursingLevelServiceImpl implements NursingLevelService {

    @Autowired
    private NursingLevelMapper nursingLevelMapper;

    @Override
    public List<NursingLevel> selectAll() {
        return nursingLevelMapper.selectAll();
    }

    @Override
    public PageResponse<NursingLevel> selectByPage(Integer pageNum, Integer pageSize, String name, Integer status) {
        PageHelper.startPage(pageNum, pageSize);
        Page<NursingLevel> page = (Page<NursingLevel>) nursingLevelMapper.selectByPage(name, status);
        return PageResponse.of(page, NursingLevel.class);
    }

    @Override
    public NursingLevel selectById(Long id) {
        return nursingLevelMapper.selectById(id);
    }

    @Override
    public int insert(NursingLevel nursingLevel) {
        nursingLevel.setCreateTime(LocalDateTime.now());
        return nursingLevelMapper.insert(nursingLevel);
    }

    @Override
    public int update(NursingLevel nursingLevel) {
        nursingLevel.setUpdateTime(LocalDateTime.now());
        return nursingLevelMapper.update(nursingLevel);
    }

    @Override
    public int deleteById(Long id) {
        return nursingLevelMapper.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        nursingLevelMapper.updateStatus(id, status);
    }
}
