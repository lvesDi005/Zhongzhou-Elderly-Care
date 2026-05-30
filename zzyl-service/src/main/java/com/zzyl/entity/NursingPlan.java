package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import lombok.Data;

@Data
public class NursingPlan extends BaseEntity {
    private Integer sortNo;
    private String planName;
    private Integer status;
}
