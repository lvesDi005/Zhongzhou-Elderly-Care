package com.zzyl.service;

import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.entity.NursingProject;
import com.zzyl.vo.NursingProjectVo;

import java.util.List;

/**
 * @Description NursingController
 * @Author kight-tom
 * @Date 2026-05-28  10:55
 */
public interface NursingService {

    /**
     * 分页查询
     */
    PageResponse<NursingProject> queryNursingProject(Integer pageNum, Integer pageSize, String name, Integer status);

    ResponseResult<String> addNursingProject(NursingProjectVo nursingProject);

    void isEnable(Integer id, Integer status);

    void removeNursingProject(Long id);

    NursingProject getNursingProjectById(Long id);

    void modifyNursingProject(NursingProjectVo nursingProject);

    List<NursingProject> getAll();
}