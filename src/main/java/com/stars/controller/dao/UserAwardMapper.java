package com.stars.controller.dao;

import com.stars.controller.entity.UserAward;

import java.util.List;

public interface UserAwardMapper {
    List<UserAward> selectByUserId(Integer id);
}
