package com.stars.controller.entity;


public class Version
{


  private Integer id;
  private String versionCode;
  private String datetime;
  private String downloadWindows;
  private String downloadAndroid;
  private int type;
  private String remark;
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
  public String getVersionCode()
  {
    return this.versionCode;
  }
  
  public void setVersionCode(String versionCode)
  {
    this.versionCode = versionCode;
  }
  
  public String getDatetime()
  {
    return this.datetime;
  }
  
  public void setDatetime(String datetime)
  {
    this.datetime = datetime;
  }
  
  public String getDownloadWindows()
  {
    return this.downloadWindows;
  }
  
  public void setDownloadWindows(String downloadWindows)
  {
    this.downloadWindows = downloadWindows;
  }
  
  public String getDownloadAndroid()
  {
    return this.downloadAndroid;
  }
  
  public void setDownloadAndroid(String downloadAndroid)
  {
    this.downloadAndroid = downloadAndroid;
  }
  
  public String getRemark()
  {
    return this.remark;
  }
  
  public void setRemark(String remark)
  {
    this.remark = remark;
  }
  
  public int getType()
  {
    return this.type;
  }
  
  public void setType(int type)
  {
    this.type = type;
  }
}
