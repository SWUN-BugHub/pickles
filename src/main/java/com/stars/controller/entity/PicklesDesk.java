package com.stars.controller.entity;

import java.util.Date;

public class PicklesDesk
{
    private Integer id;
  private Integer roomId;
  private String name;
  private Integer playerCount;
  private Integer baseCoin;
  private Integer limitCoin;
  private Integer autoStart;
  private Integer totalGame;
  private Boolean isCut;
  private Boolean isStart;
  private Date createTime;
  private Boolean isDeleted;
  private Integer exchange;
  public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public int getPlayerCount() {
	return playerCount;
}

public void setPlayerCount(int playerCount) {
	this.playerCount = playerCount;
}

public int getBaseCoin() {
	return baseCoin;
}

public void setBaseCoin(int baseCoin) {
	this.baseCoin = baseCoin;
}

public int getLimitCoin() {
	return limitCoin;
}

public void setLimitCoin(int limitCoin) {
	this.limitCoin = limitCoin;
}

public int getAutoStart() {
	return autoStart;
}

public void setAutoStart(int autoStart) {
	this.autoStart = autoStart;
}

public int getTotalGame() {
	return totalGame;
}

public void setTotalGame(int totalGame) {
	this.totalGame = totalGame;
}

public boolean isCut() {
	return isCut;
}

public void setCut(boolean isCut) {
	this.isCut = isCut;
}

public boolean isStart() {
	return isStart;
}

public void setStart(boolean isStart) {
	this.isStart = isStart;
}

public Date getCreateTime() {
	return createTime;
}

public void setCreateTime(Date createTime) {
	this.createTime = createTime;
}

public boolean isDeleted() {
	return isDeleted;
}

public void setDeleted(boolean isDeleted) {
	this.isDeleted = isDeleted;
}

public int getRoomId()
  {
    return this.roomId;
  }
  
  public void setRoomId(int roomId)
  {
    this.roomId = roomId;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }

public Integer getExchange() {
	return exchange;
}

public void setExchange(Integer exchange) {
	this.exchange = exchange;
}
}
