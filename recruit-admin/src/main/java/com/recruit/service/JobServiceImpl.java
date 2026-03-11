package com.recruit.service;

import com.recruit.entity.JobInfo;
import com.recruit.mapper.JobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobMapper jobMapper;

    @Override
    public List<JobInfo> getList(Integer status, String keyword) {
        return jobMapper.selectList(status, keyword);
    }

    @Override
    public JobInfo getById(Long id) {
        return jobMapper.selectById(id);
    }

    @Override
    public boolean add(JobInfo job) {
        return jobMapper.insert(job) > 0;
    }

    @Override
    public boolean update(JobInfo job) {
        return jobMapper.update(job) > 0;
    }

    @Override
    public boolean delete(Long id) {
        return jobMapper.delete(id) > 0;
    }

    @Override
    public boolean updateViewCount(Long id) {
        return jobMapper.updateViewCount(id) > 0;
    }

    @Override
    public boolean updateTop(Long id, Integer isTop) {
        return jobMapper.updateTop(id, isTop) > 0;
    }
}
