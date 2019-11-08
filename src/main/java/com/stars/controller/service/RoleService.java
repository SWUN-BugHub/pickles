package com.stars.controller.service;

import com.stars.controller.dao.RoleMapper;
import com.stars.controller.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleMapper mapper;
    public User selectByUserName(String username) {
        return selectByUserName(username);
    }
}
