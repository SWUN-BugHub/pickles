package com.stars.controller.dao;


import com.stars.controller.entity.Activity;

public interface ActivityMapper {
    Activity selectById(int id);
}