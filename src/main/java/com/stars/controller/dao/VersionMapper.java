package com.stars.controller.dao;

import com.stars.controller.entity.Version;

public interface VersionMapper {

    Version selectById(int id);
}