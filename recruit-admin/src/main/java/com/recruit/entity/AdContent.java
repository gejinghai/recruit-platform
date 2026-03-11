package com.recruit.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AdContent {
    private Long id;
    private Long positionId;
    private String title;
    private String imageUrl;
    private String linkUrl;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createTime;
}
