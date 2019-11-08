package com.stars.controller.entity;


public class SystemConfig
{
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  private int id;
  private int userCheck = 1;
  private int payCheckMoney = 1000;
  private int expiryCheckMoney = 1000;
  private int leaseCheck = 0;
  private int authorize = 1;
  private int sumPayMoney = 100000;
  private int tempSumPayMoney = 100000;
  private int sumExpiryMoney = 100000;
  private int tempSumExpiryMoney = 100000;
  private int userSumMoney = 20000;
  private int tempUserSumMoney = 20000;
  private int promoterSumMoney = 20000;
  private int tempPromoterSumMoney;
  private int moneyOverrun = 100000;
  private int notActive = 180;
  private int chat = 1;
  private int payScale = 100;
  private int promoterPayScale = 20;
  private int gameStatus = 0;
  private int weihuTime = -1;
  private String content;
  private int switchType = -1;
  private int interactPassword = 0;
  private int openLuckGame = 1;
  private int openFishGame = 0;
  private int openCardGame = 0;
  private int operationStatus = 0;
  private String operationDate;
  private long operationStopDate;
  private int baodanStatus = 1;
  private int lackBaodanStatus = 1;
  private int expiry;
  private String baodanPwd;
  
  public int getLackBaodanStatus()
  {
    return this.lackBaodanStatus;
  }
  
  public void setLackBaodanStatus(int lackBaodanStatus)
  {
    this.lackBaodanStatus = lackBaodanStatus;
  }
  
  public int getUserCheck()
  {
    return this.userCheck;
  }
  
  public void setUserCheck(int userCheck)
  {
    this.userCheck = userCheck;
  }
  
  public int getPayCheckMoney()
  {
    return this.payCheckMoney;
  }
  
  public void setPayCheckMoney(int payCheckMoney)
  {
    this.payCheckMoney = payCheckMoney;
  }
  
  public int getExpiryCheckMoney()
  {
    return this.expiryCheckMoney;
  }
  
  public void setExpiryCheckMoney(int expiryCheckMoney)
  {
    this.expiryCheckMoney = expiryCheckMoney;
  }
  
  public int getLeaseCheck()
  {
    return this.leaseCheck;
  }
  
  public void setLeaseCheck(int leaseCheck)
  {
    this.leaseCheck = leaseCheck;
  }
  
  public int getAuthorize()
  {
    return this.authorize;
  }
  
  public void setAuthorize(int authorize)
  {
    this.authorize = authorize;
  }
  
  public int getSumPayMoney()
  {
    return this.sumPayMoney;
  }
  
  public void setSumPayMoney(int sumPayMoney)
  {
    this.sumPayMoney = sumPayMoney;
  }
  
  public int getTempSumPayMoney()
  {
    return this.tempSumPayMoney;
  }
  
  public void setTempSumPayMoney(int tempSumPayMoney)
  {
    this.tempSumPayMoney = tempSumPayMoney;
  }
  
  public int getSumExpiryMoney()
  {
    return this.sumExpiryMoney;
  }
  
  public void setSumExpiryMoney(int sumExpiryMoney)
  {
    this.sumExpiryMoney = sumExpiryMoney;
  }
  
  public int getTempSumExpiryMoney()
  {
    return this.tempSumExpiryMoney;
  }
  
  public void setTempSumExpiryMoney(int tempSumExpiryMoney)
  {
    this.tempSumExpiryMoney = tempSumExpiryMoney;
  }
  
  public int getUserSumMoney()
  {
    return this.userSumMoney;
  }
  
  public void setUserSumMoney(int userSumMoney)
  {
    this.userSumMoney = userSumMoney;
  }
  
  public int getTempUserSumMoney()
  {
    return this.tempUserSumMoney;
  }
  
  public void setTempUserSumMoney(int tempUserSumMoney)
  {
    this.tempUserSumMoney = tempUserSumMoney;
  }
  
  public int getPromoterSumMoney()
  {
    return this.promoterSumMoney;
  }
  
  public void setPromoterSumMoney(int promoterSumMoney)
  {
    this.promoterSumMoney = promoterSumMoney;
  }
  
  public int getTempPromoterSumMoney()
  {
    return this.tempPromoterSumMoney;
  }
  
  public void setTempPromoterSumMoney(int tempPromoterSumMoney)
  {
    this.tempPromoterSumMoney = tempPromoterSumMoney;
  }
  
  public int getMoneyOverrun()
  {
    return this.moneyOverrun;
  }
  
  public void setMoneyOverrun(int moneyOverrun)
  {
    this.moneyOverrun = moneyOverrun;
  }
  
  public int getNotActive()
  {
    return this.notActive;
  }
  
  public void setNotActive(int notActive)
  {
    this.notActive = notActive;
  }
  
  public int getChat()
  {
    return this.chat;
  }
  
  public void setChat(int chat)
  {
    this.chat = chat;
  }
  
  public int getPayScale()
  {
    return this.payScale;
  }
  
  public void setPayScale(int payScale)
  {
    this.payScale = payScale;
  }
  
  public int getPromoterPayScale()
  {
    return this.promoterPayScale;
  }
  
  public void setPromoterPayScale(int promoterPayScale)
  {
    this.promoterPayScale = promoterPayScale;
  }
  
  public int getGameStatus()
  {
    return this.gameStatus;
  }
  
  public void setGameStatus(int gameStatus)
  {
    this.gameStatus = gameStatus;
  }
  
  public int getWeihuTime()
  {
    return this.weihuTime;
  }
  
  public void setWeihuTime(int weihuTime)
  {
    this.weihuTime = weihuTime;
  }
  
  public String getContent()
  {
    return this.content;
  }
  
  public void setContent(String content)
  {
    this.content = content;
  }
  
  public int getSwitchType()
  {
    return this.switchType;
  }
  
  public void setSwitchType(int switchType)
  {
    this.switchType = switchType;
  }
  
  public int getInteractPassword()
  {
    return this.interactPassword;
  }
  
  public void setInteractPassword(int interactPassword)
  {
    this.interactPassword = interactPassword;
  }
  
  public int getOpenLuckGame()
  {
    return this.openLuckGame;
  }
  
  public void setOpenLuckGame(int openLuckGame)
  {
    this.openLuckGame = openLuckGame;
  }
  
  public int getOpenFishGame()
  {
    return this.openFishGame;
  }
  
  public void setOpenFishGame(int openFishGame)
  {
    this.openFishGame = openFishGame;
  }
  
  public int getOpenCardGame()
  {
    return this.openCardGame;
  }
  
  public void setOpenCardGame(int openCardGame)
  {
    this.openCardGame = openCardGame;
  }
  
  public int getOperationStatus()
  {
    return this.operationStatus;
  }
  
  public void setOperationStatus(int operationStatus)
  {
    this.operationStatus = operationStatus;
  }
  
  public String getOperationDate()
  {
    return this.operationDate;
  }
  
  public void setOperationDate(String operationDate)
  {
    this.operationDate = operationDate;
  }
  
  public long getOperationStopDate()
  {
    return this.operationStopDate;
  }
  
  public void setOperationStopDate(long operationStopDate)
  {
    this.operationStopDate = operationStopDate;
  }
  
  public int getBaodanStatus()
  {
    return this.baodanStatus;
  }
  
  public void setBaodanStatus(int baodanStatus)
  {
    this.baodanStatus = baodanStatus;
  }
  
  public int getExpiry()
  {
    return this.expiry;
  }
  
  public void setExpiry(int expiry)
  {
    this.expiry = expiry;
  }
  
  public String getBaodanPwd()
  {
    return this.baodanPwd;
  }
  
  public void setBaodanPwd(String baodanPwd)
  {
    this.baodanPwd = baodanPwd;
  }
}
