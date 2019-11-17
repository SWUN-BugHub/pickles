package com.stars.controller.protocol.inf;


import com.stars.controller.entity.GameUser;

public class PlaceInfo
{
  private GameUser Player;
  private PlaceType state;
  private int place;

  public PlaceInfo(int place)
  {
    this.place = place;
  }

  public GameUser getPlayer()
  {
    return this.Player;
  }

  public void setPlayer(GameUser Player)
  {
    this.Player = Player;
  }

  public PlaceType getState()
  {
    return this.state;
  }

  public void setState(PlaceType state)
  {
    this.state = state;
  }

  public int getPlace()
  {
    return this.place;
  }

  public void setPlace(int place)
  {
    this.place = place;
  }
}