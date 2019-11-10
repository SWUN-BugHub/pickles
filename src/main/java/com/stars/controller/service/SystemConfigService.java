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

  private SystemConfig systemConfig=new SystemConfig();
  public SystemConfig getSystemConfig()
  {
    systemConfig=this.systemConfigDao.selectSystemConfigByOne();
    return systemConfig;
  }

    public SystemConfig getSystemConfig2() {
      return systemConfig;
    }
}
