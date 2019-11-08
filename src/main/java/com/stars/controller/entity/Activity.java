package com.stars.controller.entity;

public class Activity
{

  private int id;
  private int type;
  private String content = "";
  private int awardGold;
  private String datetime = "";
  private int condition1;
  private int condition2;
  private int condition3;

  public String getContent()
  {
    return this.content;
  }
  
  public void setContent(String content)
  {
    this.content = content;
  }
  
  public int getAwardGold()
  {
    return this.awardGold;
  }
  
  public void setAwardGold(int awardGold)
  {
    this.awardGold = awardGold;
  }
  
  public int getType()
  {
    return this.type;
  }
  
  public void setType(int type)
  {
    this.type = type;
  }
  
  public String getDatetime()
  {
    return this.datetime;
  }
  
  public void setDatetime(String datetime)
  {
    this.datetime = datetime;
  }
  
  public int getCondition1()
  {
    return this.condition1;
  }
  
  public void setCondition1(int condition1)
  {
    this.condition1 = condition1;
  }
  
  public int getCondition2()
  {
    return this.condition2;
  }
  
  public void setCondition2(int condition2)
  {
    this.condition2 = condition2;
  }
  
  public int getCondition3()
  {
    return this.condition3;
  }
  
  public void setCondition3(int condition3)
  {
    this.condition3 = condition3;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String toString()
  {
    return "Activity [type=" + this.type + ", content=" + this.content + ", awardGold=" + this.awardGold + ", datetime=" + this.datetime + ", condition1=" + this.condition1 + ", condition2=" + this.condition2 + ", condition3=" + this.condition3 + "]";
  }
}
