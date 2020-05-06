package com.tg.ws;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kie.api.task.UserGroupCallback;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class CustomUserGroupCallback implements UserGroupCallback 
{
	private JdbcTemplate jdbcTemplate;
	private Map<String, Map<String, String>> registry = null;

	public CustomUserGroupCallback()
	{
	}

	public CustomUserGroupCallback(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
		updateCache();
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public boolean existsGroup(String groupId)
	{
		boolean isFoundInCache = registry.containsKey(groupId);

		if(isFoundInCache)
		{
			return true;
		}

		try
		{
			String group = jdbcTemplate.queryForObject("select id from OrganizationalEntity where id = ? and DTYPE='Group'", String.class, groupId);

			if(group != null)
			{
				// Update the cache with new entry
				if(!isFoundInCache)
				{
					Map<String, String> entityInfo = new HashMap<String, String>();
					entityInfo.put("name", groupId);					
					synchronized (registry) {
						registry.put(groupId, entityInfo);						
					}				
				}

				return true;
			}
		}
		catch(DataAccessException dae)
		{
			return false;
		}
		return false;
	}

	@Override
	public boolean existsUser(String userId) 
	{
		boolean isFoundInCache = registry.containsKey(userId);

		if(isFoundInCache)
		{
			return true;
		}

		try
		{
			String user = jdbcTemplate.queryForObject("select id from OrganizationalEntity where id = ?", String.class, userId);
			if(user != null)
			{
				// Update the cache with new entry
				if(!isFoundInCache)
				{
					Map<String, String> entityInfo = new HashMap<String, String>();
					entityInfo.put("name", userId);	
					synchronized (registry) {
						registry.put(userId, entityInfo);						
					}					
				}

				return true;
			}

		}
		catch(DataAccessException dae)
		{
			return false;
		}

		return false; 
	}

	@Override
	public List<String> getGroupsForUser(String arg0, List<String> arg1,			
			List<String> arg2) {
		return null;
	}	

	private void updateCache()
	{		
		registry = new HashMap<String, Map<String, String>>();

		List<String> userList = jdbcTemplate.queryForList("select id from OrganizationalEntity", String.class);

		if(userList != null)
		{			
			for(String userStr : userList)
			{
				Map<String, String> entityInfo = new HashMap<String, String>();
				entityInfo.put("name", userStr);
				registry.put(userStr, entityInfo);
			}
		}
	}	
}