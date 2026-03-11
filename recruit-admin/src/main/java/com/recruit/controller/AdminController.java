package com.recruit.controller;

import com.recruit.dto.Result;
import com.recruit.entity.Admin;
import com.recruit.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private AdminMapper adminMapper;

    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");

        Admin admin = adminMapper.selectByUsername(username);
        if (admin == null) {
            return Result.error("用户名或密码错误");
        }

        // 简单密码验证（生产环境应使用BCrypt）
        if (!password.equals("admin123")) {
            return Result.error("用户名或密码错误");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("id", admin.getId());
        data.put("username", admin.getUsername());
        data.put("role", admin.getRole());
        return Result.success(data);
    }

    @PostMapping("/logout")
    public Result<?> logout() {
        return Result.success();
    }
}
