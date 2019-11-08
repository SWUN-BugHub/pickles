/**
 * 
 */
package com.stars.controller.service;

import com.stars.controller.dao.ActivityMapper;
import com.styf.dao.AppVersionMapper;
import com.styf.pojo.AppVersion;
import com.styf.pojo.AppVersionExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**   
 * 此类描述的是：   
 * @author: styf   
 * @version: 2016年4月15日 上午11:44:53    
 */
@Service
public class ActivityService
{
	@Autowired
	private ActivityMapper activityService;
	
	

}
