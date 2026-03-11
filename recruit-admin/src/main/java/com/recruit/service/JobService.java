package com.recruit.service;

import com.recruit.entity.JobInfo;
import java.util.List;

public interface JobService {
    List<JobInfo> getList(Integer status, String keyword);

    JobInfo getById(Long id);

    boolean add(JobInfo job);

    boolean update(JobInfo job);

    boolean delete(Long id);

    boolean updateViewCount(Long id);

    boolean updateTop(Long id, Integer isTop);
}
