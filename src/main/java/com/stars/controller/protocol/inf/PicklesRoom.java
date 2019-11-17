package com.stars.controller.protocol.inf;

import com.stars.controller.entity.GameUser;
import com.stars.controller.entity.PicklesDesk;
import com.stars.controller.entity.User;
import com.stars.controller.protocol.BaseRoom;

import java.util.List;

public class PicklesRoom extends BaseRoom {
    public  PicklesRoom(PicklesDesk roomInfo)
    {
        this.roomInfo=roomInfo;
    }
    @Override
    public PicklesDesk getRoomInfo() {
        return this.roomInfo;
    }


    @Override
    public void updatePlaceState(GameUser paramIGamePlayer) {

    }

    @Override
    public boolean removePlayer(GameUser paramIGamePlayer, int paramInt) {
        return false;
    }

    @Override
    public List<Integer> getXiaZhuBets() {
        return null;
    }

    @Override
    public float getPlayerCoin(User paramIGamePlayer) {
        return 0;
    }

    @Override
    public void setGame(BaseGame game) {
        this.game=game;
    }
}
