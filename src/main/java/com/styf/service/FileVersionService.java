/**
 * 
 */
package com.styf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.styf.dao.FileVersionMapper;
import com.styf.pojo.AppVersionExample;
import com.styf.pojo.FileVersion;
import com.styf.pojo.FileVersionExample;

/**   
 * 此类描述的是：   
 * @author: styf   
 * @version: 2016年5月3日 下午3:09:42    
 */
//@Service
public class FileVersionService
{
	//@Autowired
	private FileVersionMapper fileVersionMapper;

	/**
	 * @param appName
	 * @return
	 */
	public List<FileVersion> selectByAppName(String appName)
	{
		
		FileVersionExample example = new FileVersionExample();
		FileVersionExample.Criteria criteria = example.createCriteria();
		criteria.andIsDeleteEqualTo(0);
		criteria.andAppNameEqualTo(appName);
		criteria.andIsReleasedEqualTo(1);
		example.setOrderByClause("update_time desc");
		
		return fileVersionMapper.selectByExample(example);
		
	}

	/**
	 * @param fileVersion
	 */
	public int save(FileVersion fileVersion)
	{
		return fileVersionMapper.insertSelective(fileVersion);
	}

	/**
	 * @param fileVersion
	 * @return
	 */
	public List<FileVersion> selectByAppNameAndVersion(FileVersion fileVersion)
	{
		FileVersionExample example = new FileVersionExample();
		FileVersionExample.Criteria criteria = example.createCriteria();
		criteria.andIsDeleteEqualTo(0);
		criteria.andAppNameEqualTo(fileVersion.getAppName());
		criteria.andAppVersionEqualTo(fileVersion.getAppVersion());
		example.setOrderByClause("update_time desc");
		return fileVersionMapper.selectByExample(example);
	}

	/**
	 * @param fileVersion
	 */
	public int updateById(FileVersion fileVersion)
	{
		return fileVersionMapper.updateByPrimaryKey(fileVersion);
	}
}
