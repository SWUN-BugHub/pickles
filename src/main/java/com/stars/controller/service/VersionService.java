
package com.stars.controller.service;

import com.stars.controller.dao.VersionMapper;
import com.stars.controller.entity.Version;
import com.styf.dao.AppVersionMapper;
import com.styf.pojo.AppVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**   
 * 此类描述的是：   
 * @author: 版本查询service
 * @version:
 */
@Service
public class VersionService
{
	@Autowired
	private VersionMapper versionMapper;
	/**
	 * 根据ID查询版本
	 * @param  id	版本id
	 * @return
	 */
	public Version selectById(Integer id)
	{
		return versionMapper.selectById(id);
	}

}
