package com.recruit.controller;

import com.recruit.dto.Result;
import com.recruit.entity.JobInfo;
import com.recruit.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/job")
@CrossOrigin
public class AdminJobController {

    @Autowired
    private JobService jobService;

    @GetMapping("/list")
    public Result<?> list(@RequestParam(required = false) Integer status,
                          @RequestParam(required = false) String keyword) {
        return Result.success(jobService.getList(status, keyword));
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody JobInfo job) {
        job.setStatus(1);
        job.setViewCount(0);
        job.setIsTop(0);
        boolean success = jobService.add(job);
        return success ? Result.success() : Result.error("添加失败");
    }

    @PostMapping("/update")
    public Result<?> update(@RequestBody JobInfo job) {
        boolean success = jobService.update(job);
        return success ? Result.success() : Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        boolean success = jobService.delete(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    @PostMapping("/top/{id}")
    public Result<?> top(@PathVariable Long id, @RequestParam Integer isTop) {
        boolean success = jobService.updateTop(id, isTop);
        return success ? Result.success() : Result.error("操作失败");
    }
}
