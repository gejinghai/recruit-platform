package com.recruit.mapper;

import com.recruit.entity.AdPosition;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AdPositionMapper {
    List<AdPosition> selectAll();

    AdPosition selectById(Long id);
}
