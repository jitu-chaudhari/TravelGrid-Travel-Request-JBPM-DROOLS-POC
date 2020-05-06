package com.tg.ws.service;

import javax.persistence.EntityManagerFactory;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.task.TaskService;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

@Transactional("jbpmTxManager")
public class JBPMService {
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Autowired
	private AbstractPlatformTransactionManager aptransactionManager;
	
	@Autowired(required=false)
	@Qualifier("rm")
	RuntimeManager runtimeManager;

	@Autowired(required=false)
	@Qualifier("ruleRM")
	RuntimeManager ruleRuntimeManager;
	
	public KieSession getKieSession()
	{
		System.out.println("runtimeManager: " + runtimeManager);
		 RuntimeEngine runtimeEngine = runtimeManager.getRuntimeEngine(EmptyContext.get());
		 
		 KieSession ksession = runtimeEngine.getKieSession();
		 System.out.println("runtimeEngine: " + runtimeEngine);
		 System.out.println("ksession: " + ksession);
		 return ksession;
	}

	public KieSession getRuleKieSession()
	{
		System.out.println("ruleRuntimeManager: " + ruleRuntimeManager);
		 RuntimeEngine runtimeEngine = ruleRuntimeManager.getRuntimeEngine(ProcessInstanceIdContext.get());
		 
		 KieSession ksession = runtimeEngine.getKieSession();
		 System.out.println("ruleRuntimeEngine: " + runtimeEngine);
		 System.out.println("ksession: " + ksession);
		 return ksession;
	}
	
	public TaskService getTaskService()
	{
		 RuntimeEngine runtimeEngine = runtimeManager.getRuntimeEngine(EmptyContext.get());
		 
		 TaskService taskService = runtimeEngine.getTaskService();
		 
		 return taskService;
	}
	
	
}