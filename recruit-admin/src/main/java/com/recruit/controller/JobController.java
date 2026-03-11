package com.recruit.controller;

import com.recruit.dto.Result;
import com.recruit.entity.JobInfo;
import com.recruit.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/job")
@CrossOrigin
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping("/list")
    public Result<?> list(@RequestParam(required = false) Integer status,
                          @RequestParam(required = false) String keyword) {
        return Result.success(jobService.getList(status, keyword));
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Long id) {
        JobInfo job = jobService.getById(id);
        if (job == null) {
            return Result.error("职位不存在");
        }
        return Result.success(job);
    }

    @PostMapping("/{id}/view")
    public Result<?> view(@PathVariable Long id) {
        jobService.updateViewCount(id);
        return Result.success();
    }
}
