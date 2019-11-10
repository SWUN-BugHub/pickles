package com.stars.controller.entity;


public class UserAward
{
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  private int id;
  private int userId;
  private int gold;
  private String datetime;
  private int status;
  private String admin;
  private String username;
  private int authority;
  
  public int getUserId()
  {
    return this.userId;
  }
  
  public void setUserId(int userId)
  {
    this.userId = userId;
  }
  
  public int getGold()
  {
    return this.gold;
  }
  
  public void setGold(int gold)
  {
    this.gold = gold;
  }
  
  public String getDatetime()
  {
    return this.datetime;
  }
  
  public void setDatetime(String datetime)
  {
    this.datetime = datetime;
  }
  
  public int getStatus()
  {
    return this.status;
  }
  
  public void setStatus(int status)
  {
    this.status = status;
  }
  
  public String getAdmin()
  {
    return this.admin;
  }
  
  public void setAdmin(String admin)
  {
    this.admin = admin;
  }
  
  public String getUsername()
  {
    return this.username;
  }
  
  public void setUsername(String username)
  {
    this.username = username;
  }
  
  public int getAuthority()
  {
    return this.authority;
  }
  
  public void setAuthority(int authority)
  {
    this.authority = authority;
  }
}
