package com.stars.controller.service;

import com.stars.controller.dao.UserAwardMapper;
import com.stars.controller.entity.UserAward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAwardService {
    @Autowired
    private UserAwardMapper userAwardMapper;
    public UserAward[] selectByUserId(Integer id) {
        List<UserAward> list=userAwardMapper.selectByUserId(id);
        return list.toArray(new UserAward[list.size()]);
    }
}
