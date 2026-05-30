package com.zzyl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.entity.NursingProject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 护理项目 VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NursingProjectVo extends NursingProject {

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 创建人名称
     */
    private String creator;

    /**
     * 更新人名称
     */
    private String updater;
}
