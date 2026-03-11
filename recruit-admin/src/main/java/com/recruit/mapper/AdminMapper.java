package com.recruit.mapper;

import com.recruit.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminMapper {
    Admin selectByUsername(@Param("username") String username);

    int insert(Admin admin);

    int updatePassword(@Param("id") Long id, @Param("password") String password);
}
