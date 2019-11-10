package com.stars.controller.service;

import com.stars.controller.dao.PicklesDeskMapper;
import com.stars.controller.entity.PicklesDesk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PicklesDeskService {
    @Autowired
    private PicklesDeskMapper picklesDeskMapper;
    public List<PicklesDesk> getPicklesDesks(int roomId) {
        return picklesDeskMapper.selectByRoomId(roomId);
    }

    public PicklesDesk getPicklesDesksById(int deskId) {
        return picklesDeskMapper.selectById(deskId);
    }
}
