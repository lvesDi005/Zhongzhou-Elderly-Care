package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NursingTask extends BaseEntity {
    private String taskNo;
    private Long elderId;
    private Long projectId;
    private Integer planId;
    private Integer status;
    private LocalDateTime executeTime;
    private String executeBy;
    private LocalDateTime completeTime;
    private String cancelReason;
    private String taskImage;
}
