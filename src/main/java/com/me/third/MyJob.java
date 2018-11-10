package com.me.third;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MyJob implements Job{
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail job = context.getJobDetail();
		JobDataMap data = job.getJobDataMap();
		System.out.print(this.toString()+":"+data.getString("name")+" ");
		System.out.println("还钱呀!"); 
	}
}
