package com.stars.controller.dao;

import com.stars.controller.entity.PicklesDesk;

import java.util.List;

public interface PicklesDeskMapper {

    List<PicklesDesk> selectByRoomId(int roomId);

    PicklesDesk selectById(int id);
}