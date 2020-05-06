package com.tg.ws;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.kie.api.runtime.KieSession;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tg.ws.service.JBPMService;

public class TestDrool extends Thread
{
	private static JBPMService jbpmService;
	private String userId;
	private static ApplicationContext context;
	
	public TestDrool(String userId)
	{
		this.userId = userId;
		this.start();
	}

	public static void main(String[] args) {

		getDRL();
		
		context = new ClassPathXmlApplicationContext("spring.xml");
		
		jbpmService = context.getBean(JBPMService.class);
				
		new TestDrool("abhishek");
	
	}
	
	public void run()
	{


		Map<String,String> map =  new HashMap<String,String>();
/*		map.put("grade", "N");
		map.put("tripType", "D");
		map.put("travelMode", "A");
		map.put("fareClass", "BC");
		
		fireRule(map);

		map =  new HashMap<String,String>();
		map.put("grade", "4");
		map.put("tripType", "D");
		map.put("travelMode", "A");
		map.put("fareClass", "EC");
		
		fireRule(map);

		map =  new HashMap<String,String>();
		map.put("grade", "1");
		map.put("tripType", "D");
		map.put("travelMode", "A");
		map.put("fareClass", "LC");
		
		fireRule(map);*/
		
		map =  new HashMap<String,String>();
		map.put("grade", "N");
		map.put("tripType", "O");
		map.put("travelMode", "A");
		map.put("fareClass", "EC");
		
		fireRule(map);		

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


	public static void fireRule(Map map)
	{
		long s = System.currentTimeMillis();
		KieSession ksession = jbpmService.getRuleKieSession();	
		
		System.out.println("Rule params before firing rules: " + map);
		ksession.insert(map);
		ksession.fireAllRules( new RuleNameStartsWithAgendaFilter("FareClassRule"));
		ksession.dispose();
		System.out.println("Rule params after firing rules: " + map);
		//System.out.println("message: " + map.get("message"));
		long e =  System.currentTimeMillis();
		System.out.println("Time taken: " + (e-s));
		//ksession.dispose();
		
	}
	
	public static void getDRL()
	{
		InputStream is =null;
		try {
		is= new FileInputStream("C:/Users/Jitendra/Desktop/CM/GenericBuild/Research/SpringWebservice/qapprover/src/main/resources/TestDrools.xls");
		} catch (FileNotFoundException e) {
		e.printStackTrace();
		}
		// Create compiler class instance
		SpreadsheetCompiler sc = new SpreadsheetCompiler();

		// Compile the excel to generate the (.drl) file
		StringBuffer drl=new StringBuffer(sc.compile(is, InputType.XLS));		
		System.out.println(drl);
	}
}
