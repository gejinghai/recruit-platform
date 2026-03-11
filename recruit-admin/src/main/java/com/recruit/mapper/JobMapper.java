package com.recruit.mapper;

import com.recruit.entity.JobInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface JobMapper {
    List<JobInfo> selectList(@Param("status") Integer status, @Param("keyword") String keyword);

    JobInfo selectById(@Param("id") Long id);

    int insert(JobInfo job);

    int update(JobInfo job);

    int delete(@Param("id") Long id);

    int updateViewCount(@Param("id") Long id);

    int updateTop(@Param("id") Long id, @Param("isTop") Integer isTop);
}
