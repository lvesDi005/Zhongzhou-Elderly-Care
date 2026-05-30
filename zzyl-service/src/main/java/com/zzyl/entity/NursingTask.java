package com.zzyl.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.base.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NursingTask extends BaseEntity {
    private Long nursingId;
    private Long projectId;
    private Long elderId;
    private String bedNumber;
    private Integer taskType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime estimatedServerTime;
    private String mark;
    private String cancelReason;
    private Integer status;
    private String relNo;
    private String taskImage;

    private String elderName;
    private String projectName;
    private String nursingName;
}
