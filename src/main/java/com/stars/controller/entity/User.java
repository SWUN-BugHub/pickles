package com.stars.controller.entity;

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
  private char sex;
  private String password;
  private String phone;
  private String card;
  private String question;
  private String answer;
  private String registDate;
  private String loginDate;
  private int status = 0;
  private int overflow = 0;
  private int gameGold = 0;
  private int expeGold = 0;
  private double levelScore = 0.0D;
  private int gameScore = 0;
  private int expeScore = 0;
  private int level = 1;
  private int photoId = 1;
  private int lastDeskId;
  private int shutupStatus = 0;
  private int lastGame;
  private int type;
  private int expiryNum = 0;
  private int payMoney;
  private int promoterId;
  private String promoterName;
  private int borrow;
  private int displayStatus;
  private int currentGameScore;
  
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
  
  public int getBorrow()
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
  
  public int getDisplayStatus()
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
  
  public int getCurrentGameScore()
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
