package com.recruit.mapper;

import com.recruit.entity.AdContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AdContentMapper {
    List<AdContent> selectByPositionId(@Param("positionId") Long positionId);

    List<AdContent> selectByPositionKey(@Param("positionKey") String positionKey);

    List<AdContent> selectAll();

    AdContent selectById(@Param("id") Long id);

    int insert(AdContent adContent);

    int update(AdContent adContent);

    int delete(@Param("id") Long id);
}
