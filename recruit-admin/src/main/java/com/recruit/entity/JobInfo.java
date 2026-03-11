package com.recruit.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class JobInfo {
    private Long id;
    private String title;
    private String companyName;
    private String salary;
    private String workType;
    private String workTime;
    private String welfare;
    private String description;
    private String requirements;
    private String collectionTime;
    private String address;
    private Integer status;
    private Integer viewCount;
    private Integer isTop;
    private String contactType;
    private String contactValue;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
