package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NursingLevel extends BaseEntity {
    private String name;
    private Integer lplanId;
    private BigDecimal fee;
    private Integer status;
    private String description;

    private String planName;
}
