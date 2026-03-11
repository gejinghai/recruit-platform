package com.recruit.entity;

import lombok.Data;

@Data
public class AdPosition {
    private Long id;
    private String positionKey;
    private String positionName;
    private Integer width;
    private Integer height;
    private Integer status;
}
