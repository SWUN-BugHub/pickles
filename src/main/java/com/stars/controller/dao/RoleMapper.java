package com.stars.controller.dao;

import com.stars.controller.entity.User;

public interface RoleMapper {
    User selectByUserName(String username);

    void updateUserLastGame(User user);
}
