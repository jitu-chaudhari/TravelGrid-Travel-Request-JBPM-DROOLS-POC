package com.tg.ws;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.drools.core.factmodel.traits.Trait;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.tg.ws.service.JBPMService;

public class TestJBPM extends Thread
{
	private static JBPMService jbpmService;
	private String userId;
	private static ApplicationContext context;
	
	public TestJBPM(String userId)
	{
		this.userId = userId;
		this.start();
	}

	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext("spring.xml");
		
		jbpmService = context.getBean(JBPMService.class);
		
		new TestJBPM("19001");
	}
	
	public void run()
	{

		
		startTravelRequestProcess(userId);
	}
	
	public void delay(int interval)
	{
		try
		{
			Thread.sleep(interval);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
	}
		

	public void startTravelRequestProcess(String userId)
	{
		long s = System.currentTimeMillis();
		KieSession ksession = jbpmService.getKieSession();
		Map<String,Object> map =  new HashMap<String,Object>();
		map.put("tripType", "D");
		map.put("isSelfApproval", new Boolean(false));
		map.put("traveler", "19000");
		map.put("approver1", userId);
		map.put("approver2", "19002");
		//map.put("money", new Integer(1000));
		System.out.println("Rule params: " + map);
		
		ProcessInstance processInstance = ksession.startProcess("org.tg.domestic.travelrequest",map);

		long processInstanceId = processInstance.getId();

		System.out.println("Process started ... : processInstanceId = "
				+ processInstanceId);
				
		
		approve1(processInstanceId, true, userId);
		System.out.println("Approver 1 approved");
		//approve2(processInstanceId, true, "19002");
		//System.out.println("Approver 2 approved");*/
		
		//traveler(131);
		ksession.dispose();
	//	System.out.println("message: " + map.get("message"));
		long e =  System.currentTimeMillis();
		System.out.println("Time taken: " + (e-s));
		
	}
	
	public void approve1(long processInstanceId, boolean action, String userId)
	{
		TaskService taskService = jbpmService.getTaskService();
		List<Long> taskIds = taskService.getTasksByProcessInstanceId(processInstanceId);

		System.out.println("Task Id list:" + taskIds);
		
		if(taskIds != null && taskIds.size()>0)
		{
			long taskId = taskIds.get(0);
			taskService.start(taskId, userId);
			Map<String,Object> map =  new HashMap<String,Object>();
			map.put("isApproved", new Boolean(action));
			map.put("isViolated", new Boolean(false));
			taskService.complete(taskId, userId, map);
		}
	}
	
	public void approve2(long processInstanceId, boolean action, String userId)
	{
		TaskService taskService = jbpmService.getTaskService();
		List<Long> taskIds = taskService.getTasksByProcessInstanceId(processInstanceId);
		
		System.out.println("Task Id list:" + taskIds);
		
		if(taskIds != null && taskIds.size()>0)
		{
			long taskId = taskIds.get(1);
			taskService.start(taskId, userId);
			Map<String,Object> map =  new HashMap<String,Object>();
			map.put("isApproved", new Boolean(action));
			map.put("isViolated", new Boolean(true));			
			taskService.complete(taskId, userId,map);
		}
	}
	
	public void traveler(long processInstanceId)
	{
		TaskService taskService = jbpmService.getTaskService();
		List<Long> taskIds = taskService.getTasksByProcessInstanceId(processInstanceId);
		
		System.out.println("Task Id list:" + taskIds);
		
		if(taskIds != null && taskIds.size()>0)
		{
			long taskId = taskIds.get(1);
			taskService.start(taskId, "19000");
			Map<String,Object> map =  new HashMap<String,Object>();
			//map.put("tripType", new Boolean(true));
			//map.put("isViolated", new Boolean(true));			
			taskService.complete(taskId, "19000",map);
		}
	}	
}
