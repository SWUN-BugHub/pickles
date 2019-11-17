package com.stars.controller.protocol.inf;

import com.stars.controller.entity.GameUser;
import com.stars.controller.entity.PicklesDesk;
import com.stars.controller.entity.User;

import java.util.List;
import java.util.Set;

public abstract interface IBaseRoom
{
  public abstract PicklesDesk getRoomInfo();

  public abstract Set<GameUser> getWatchers();

  public abstract void updatePlaceState(GameUser paramIGamePlayer);

  public abstract boolean removePlayer(GameUser paramIGamePlayer, int paramInt);

  public abstract List<Integer> getXiaZhuBets();

  public abstract float getPlayerCoin(User paramIGamePlayer);

  public abstract  BaseGame getGame();

  public abstract void setGame(BaseGame game);
}