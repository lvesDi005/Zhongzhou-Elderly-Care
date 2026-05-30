package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import lombok.Data;

@Data
public class NursingProjectPlan extends BaseEntity {
    private Integer planId;
    private Integer projectId;
    private String executeTime;
    private Integer executeCycle;
    private Integer executeFrequency;
}
