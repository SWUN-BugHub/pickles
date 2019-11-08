package com.stars.controller.service;


import com.stars.controller.dao.SystemConfigMapper;
import com.stars.controller.entity.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("systemConfigService")
public class SystemConfigService
{
  @Autowired
  private SystemConfigMapper systemConfigDao;
  
  public SystemConfig getSystemConfig()
  {
    return this.systemConfigDao.getSystemConfig();
  }
}
