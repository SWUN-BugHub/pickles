package com.stars.controller.util;

import com.stars.controller.service.ActivityService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service("myApplicationContext")
public class MyApplicationContextUtil
  implements ApplicationContextAware
{
  private static ApplicationContext cb;
//  @Autowired
//  private WaterMarginDeskService waterDeskService;
  @Autowired
  private ActivityService activityService;
//  @Autowired
//  private SystemConfigService systemConfigService;
//
  public void setApplicationContext(ApplicationContext context)
    throws BeansException
  {
    cb = context;
 //   this.waterDeskService.IllIIIlIllIlIlII();
//    LocalMem.num = this.systemConfigService.IIlllIIIlIllIIIl().getMoneyOverrun();
  }
  
  public static ApplicationContext getContext()
  {
    return cb;
  }
}
