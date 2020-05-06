package com.tg.ws.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.EnvironmentName;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.internal.io.ResourceFactory;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

public class EnvironmentProvider
{
	public static RuntimeManager getRuntimeManager(EntityManagerFactory entityManagerFactory, AbstractPlatformTransactionManager aptransactionManager, List<String> resourceList, EntityManager entityManager)
	{
	
		System.out.println("*********************BUILDING EMF****************************");
		RuntimeEnvironmentBuilder reb = RuntimeEnvironmentBuilder.Factory.get()
				.newDefaultBuilder()
				.entityManagerFactory(entityManagerFactory)
				.addEnvironmentEntry(EnvironmentName.TRANSACTION_MANAGER, aptransactionManager)
				.addEnvironmentEntry("IS_JTA_TRANSACTION", Boolean.valueOf(false))
				.addEnvironmentEntry("IS_SHARED_ENTITY_MANAGER", Boolean.valueOf(true))
				.addEnvironmentEntry("org.kie.api.persistence.jpa.AppScopedEntityManager", entityManager);
				//.addEnvironmentEntry("IS_JTA_TRANSACTION", Boolean.valueOf(false));

		System.out.println("*********************BUILDING RESOURCE****************************");
		
		if(resourceList != null)
		{
			for(String resource : resourceList)
			{
				if(resource.endsWith("xls"))
				{
					reb.addAsset(ResourceFactory.newClassPathResource(resource),ResourceType.DTABLE);
				}
				else
				{
					reb.addAsset(ResourceFactory.newClassPathResource(resource),ResourceType.BPMN2);
				}
			}			
		}
		
		System.out.println("*********************BUILDING EMF****************************");
		RuntimeEnvironment environment = reb.get();
		
		//RuntimeManager runtimeManager = RuntimeManagerFactory.Factory.get().newSingletonRuntimeManager(environment);		
		RuntimeManager runtimeManager = RuntimeManagerFactory.Factory.get().newPerRequestRuntimeManager(environment);
		return runtimeManager;
	}
		
	public static Map<Resource, ResourceType> getAssets(List<String> resourceList)
	{
		Map<Resource, ResourceType> assets = new HashMap<Resource, ResourceType>();
		
		if(resourceList != null)
		{
			for(String resource : resourceList)
			{
				if(resource.endsWith("xls"))
				{
					assets.put(ResourceFactory.newClassPathResource(resource),ResourceType.DTABLE);
				}
				else if(resource.endsWith(".bpmn") || resource.endsWith(".bpmn2"))
				{
					assets.put(ResourceFactory.newClassPathResource(resource),ResourceType.BPMN2);
				}
			}			
		}		

		System.out.println(assets);
		return assets;
	}	
}
