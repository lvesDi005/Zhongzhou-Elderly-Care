package com.zzyl.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageException;
import com.github.pagehelper.PageHelper;
import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.entity.NursingProject;
import com.zzyl.entity.User;
import com.zzyl.mapper.NursingMapper;
import com.zzyl.mapper.UserMapper;
import com.zzyl.service.NursingService;
import com.zzyl.vo.NursingProjectVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description NursingController
 * @Author kight-tom
 * @Date 2026-05-28  10:55
 */
@Service
public class NursingServiceImpl implements NursingService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NursingMapper nursingMapper;

    @Override
    public PageResponse<NursingProject> queryNursingProject(Integer pageNum, Integer pageSize, String name, Integer status) {
        PageHelper.startPage(pageNum, pageSize);
        Page<NursingProject> page = (Page<NursingProject>) nursingMapper.queryNursingProject(name, status);
        for (NursingProject nursingProject : page.getResult()) {
            if (nursingProject.getCreateBy() != null) {
                User user = userMapper.selectByPrimaryKey(nursingProject.getCreateBy());
                if (user != null) {
                    nursingProject.setCreator(user.getRealName());
                }
            }
        }
        return PageResponse.of(page, NursingProject.class);
    }

    @Override
    public ResponseResult<String> addNursingProject(NursingProjectVo nursingProjectvo) {
        NursingProject nursingProject = new NursingProject();
        BeanUtils.copyProperties(nursingProjectvo, nursingProject);
        return nursingMapper.addNursingProject(nursingProject) > 0 ? ResponseResult.success("添加成功") : ResponseResult.error("添加失败");
    }

    @Override
    public void isEnable(Integer id, Integer status) {
        nursingMapper.isEnable(id, status);
    }

    @Override
    public void removeNursingProject(Long id) {
        nursingMapper.removeNursingProject(id);
    }

    @Override
    public NursingProject getNursingProjectById(Long id) {
        NursingProject nursingProject = nursingMapper.selectById(id);
        if (nursingProject != null && nursingProject.getCreateBy() != null) {
            User user = userMapper.selectByPrimaryKey(nursingProject.getCreateBy());
            if (user != null) {
                nursingProject.setCreator(user.getRealName());
            }
        }
        return nursingProject;
    }

    @Override
    public void modifyNursingProject(NursingProjectVo nursingProjectVo) {
        NursingProject nursingProject = new NursingProject();
        BeanUtils.copyProperties(nursingProjectVo, nursingProject);
        nursingMapper.modifyNursingProject(nursingProject);
    }

    @Override
    public List<NursingProject> getAll() {
        return nursingMapper.selectAll();
    }

}
