package com.stars.controller.entity;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import io.netty.channel.ChannelHandlerContext;
import jdk.nashorn.internal.ir.annotations.Ignore;

import java.lang.annotation.Annotation;
import java.util.Collection;

public class User
{
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  private Integer id;
  private String username;
  private String nickname;
  private String name;
  private Character sex;
  @Ignore
  private String password;
  @Ignore
  private String phone;
  @Ignore
  private String card;
  private String question;
  private String answer;
  @Ignore
  private String registDate;
  @Ignore
  private String loginDate;
  @Ignore
  private Integer status = 0;
  @Ignore
  private Integer overflow = 0;

  private Integer gameGold = 0;
  private Integer expeGold = 0;
  @Ignore
  private Double levelScore = 0.0D;

  private Integer gameScore = 0;
  private Integer expeScore = 0;
  @Ignore
  private Integer level = 1;
  @Ignore
  private Integer photoId = 1;
  @Ignore
  private Integer lastDeskId;
  @Ignore
  private Integer shutupStatus = 0;
  @Ignore
  private Integer lastGame;
  @Ignore
  private Integer type;
  @Ignore
  private Integer expiryNum = 0;
  @Ignore
  private Integer payMoney;
  @Ignore
  private Integer promoterId;
  @Ignore
  private String promoterName;
  @Ignore
  private Integer borrow;
  @Ignore
  private Integer displayStatus;
  @Ignore
  private Integer currentGameScore;


  public Integer getRoomId() {
    return roomId;
  }

  public void setRoomId(Integer roomId) {
    this.roomId = roomId;
  }

  private Integer roomId;

  public ChannelHandlerContext getCtx() {
    return ctx;
  }

  public void setCtx(ChannelHandlerContext ctx) {
    this.ctx = ctx;
  }

  @Ignore
  private ChannelHandlerContext ctx;


  public String getUsername()
  {
    return this.username;
  }
  
  public void setUsername(String username)
  {
    this.username = username;
  }
  
  public String getNickname()
  {
    return this.nickname;
  }
  
  public void setNickname(String nickname)
  {
    this.nickname = nickname;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public char getSex()
  {
    return this.sex;
  }
  
  public void setSex(char sex)
  {
    this.sex = sex;
  }
  
  public String getPassword()
  {
    return this.password;
  }
  
  public void setPassword(String password)
  {
    this.password = password;
  }
  
  public String getPhone()
  {
    return this.phone;
  }
  
  public void setPhone(String phone)
  {
    this.phone = phone;
  }
  
  public String getCard()
  {
    return this.card;
  }
  
  public void setCard(String card)
  {
    this.card = card;
  }
  
  public String getQuestion()
  {
    return this.question;
  }
  
  public void setQuestion(String question)
  {
    this.question = question;
  }
  
  public String getAnswer()
  {
    return this.answer;
  }
  
  public void setAnswer(String answer)
  {
    this.answer = answer;
  }
  
  public String getRegistDate()
  {
    return this.registDate;
  }
  
  public void setRegistDate(String registDate)
  {
    this.registDate = registDate;
  }
  
  public String getLoginDate()
  {
    return this.loginDate;
  }
  
  public void setLoginDate(String loginDate)
  {
    this.loginDate = loginDate;
  }
  
  public int getStatus()
  {
    return this.status;
  }
  
  public void setStatus(int status)
  {
    this.status = status;
  }
  
  public int getGameGold()
  {
    return this.gameGold;
  }
  
  public void setGameGold(int gameGold)
  {
    this.gameGold = gameGold;
  }
  
  public int getExpeGold()
  {
    return this.expeGold;
  }
  
  public void setExpeGold(int expeGold)
  {
    this.expeGold = expeGold;
  }
  
  public double getLevelScore()
  {
    return this.levelScore;
  }
  
  public void setLevelScore(double levelScore)
  {
    this.levelScore = levelScore;
  }
  
  public int getLevel()
  {
    return this.level;
  }
  
  public void setLevel(int level)
  {
    this.level = level;
  }
  
  public int getOverflow()
  {
    return this.overflow;
  }
  
  public void setOverflow(int overflow)
  {
    this.overflow = overflow;
  }
  
  public int getGameScore()
  {
    return this.gameScore;
  }
  
  public void setGameScore(int gameScore)
  {
    this.gameScore = gameScore;
  }
  
  public int getPhotoId()
  {
    return this.photoId;
  }
  
  public void setPhotoId(int photoId)
  {
    this.photoId = photoId;
  }
  
  public Integer getBorrow()
  {
    return this.borrow;
  }
  
  public void setBorrow(int borrow)
  {
    this.borrow = borrow;
  }
  
  public int getExpeScore()
  {
    return this.expeScore;
  }
  
  public void setExpeScore(int expeScore)
  {
    this.expeScore = expeScore;
  }
  
  public int getLastDeskId()
  {
    return this.lastDeskId;
  }
  
  public void setLastDeskId(int lastDeskId)
  {
    this.lastDeskId = lastDeskId;
  }
  
  public int getShutupStatus()
  {
    return this.shutupStatus;
  }
  
  public void setShutupStatus(int shutupStatus)
  {
    this.shutupStatus = shutupStatus;
  }
  
  public Integer getDisplayStatus()
  {
    return this.displayStatus;
  }
  
  public void setDisplayStatus(int displayStatus)
  {
    this.displayStatus = displayStatus;
  }
  
  public int getLastGame()
  {
    return this.lastGame;
  }
  
  public void setLastGame(int lastGame)
  {
    this.lastGame = lastGame;
  }
  
  public Integer getCurrentGameScore()
  {
    return this.currentGameScore;
  }
  
  public void setCurrentGameScore(int currentGameScore)
  {
    this.currentGameScore = currentGameScore;
  }
  
  public int getType()
  {
    return this.type;
  }
  
  public void setType(int type)
  {
    this.type = type;
  }
  
  public int getExpiryNum()
  {
    return this.expiryNum;
  }
  
  public void setExpiryNum(int expiryNum)
  {
    this.expiryNum = expiryNum;
  }
  
  public int getPayMoney()
  {
    return this.payMoney;
  }
  
  public void setPayMoney(int payMoney)
  {
    this.payMoney = payMoney;
  }
  
  public int getPromoterId()
  {
    return this.promoterId;
  }
  
  public void setPromoterId(int promoterId)
  {
    this.promoterId = promoterId;
  }
  
  public String getPromoterName()
  {
    return this.promoterName;
  }
  
  public void setPromoterName(String promoterName)
  {
    this.promoterName = promoterName;
  }

}
