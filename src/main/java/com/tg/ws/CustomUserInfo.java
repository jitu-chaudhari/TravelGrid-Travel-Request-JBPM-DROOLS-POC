package com.tg.ws;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.kie.api.task.model.Group;
import org.kie.api.task.model.OrganizationalEntity;
import org.kie.internal.task.api.UserInfo;
import org.springframework.jdbc.core.JdbcTemplate;

public class CustomUserInfo implements UserInfo
{
	private Map<String, Map<String, Object>> registry = new HashMap<String, Map<String, Object>>();
	private JdbcTemplate jdbcTemplate;

	public CustomUserInfo()
	{
	}
	
	public CustomUserInfo(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
		buildRegistry();	
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public String getDisplayName(OrganizationalEntity entity) 
	{
		System.out.println("CustomUserInfo.getDisplayName:" + entity.getId());
		Map entityInfo = (Map)this.registry.get(entity.getId());

	    if (entityInfo != null) {
	      return (String)entityInfo.get("name");
	    }
	    
		return null;
	}

	@Override
	public String getEmailForEntity(OrganizationalEntity arg0) {
		// TODO Auto-generated method stub
		System.out.println("CustomUserInfo.getEmailForEntity:" + arg0.getId());
		return null;
	}

	@Override
	public String getLanguageForEntity(OrganizationalEntity arg0) {
		// TODO Auto-generated method stub
		System.out.println("CustomUserInfo.getLanguageForEntity:" + arg0.getId());
		return "en-UK";
	}

	@Override
	public Iterator<OrganizationalEntity> getMembersForGroup(Group arg0) {
		// TODO Auto-generated method stub
		System.out.println("CustomUserInfo.getMembersForGroup:" + arg0.getId());
		return null;
	}

	@Override
	public boolean hasEmail(Group arg0) {
		// TODO Auto-generated method stub
		System.out.println("CustomUserInfo.hasEmail:" + arg0.getId());
		return false;
	}

	private void buildRegistry()
	{		
		List<String> userList = jdbcTemplate.queryForList("select id from OrganizationalEntity where DTYPE='User'", String.class);
		
		System.out.println(userList);
		if(userList != null)
		{
			Map entityInfo = new HashMap();
			for(String userStr : userList)
			{
				entityInfo.put("name", userStr);
				synchronized (registry) {
					registry.put(userStr, entityInfo);	
				}				
			}
		}
	}

}