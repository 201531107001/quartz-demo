package com.me.second;


import java.util.Date;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.impl.JobDetailImpl;

@PersistJobDataAfterExecution  //更新JobDataMap
@DisallowConcurrentExecution  //一个执行完后另一个执行
public class MyJob implements Job{
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail job = context.getJobDetail();
		JobDataMap jobMap = job.getJobDataMap();
		JobDataMap triggerMap = context.getTrigger().getJobDataMap();
		
		int triggerNum = 0;//triggerMap.getInt("num");
		int jobNum = jobMap.getInt("num");
		
		job.getJobDataMap().put("num", jobNum+1);;
		//jobMap.put("num", jobNum+1);
		//triggerMap.put("num", triggerNum+1);
		
		System.out.print(Thread.currentThread()+"jobNum:"+jobNum+" "+"triggerNum:"+triggerNum+" "+new Date().toString()+"start"+"    ");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(" "+new Date().toString()+"end");
	}
}
