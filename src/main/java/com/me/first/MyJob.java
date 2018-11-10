package com.me.first;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MyJob implements Job{

	/**
	 * JobExecutionContext是Job运行的上下文，可以获得Trigger、Scheduler、JobDetail的信息。
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail job = context.getJobDetail();
		
		JobDataMap map = job.getJobDataMap();
		System.out.print(this.toString()+":"+map.getString("name")+" ");
		System.out.println("hello quartz");
	}

}
