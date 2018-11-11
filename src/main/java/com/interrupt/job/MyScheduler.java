package com.interrupt.job;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;


public class MyScheduler {
	public static void main(String[] args){
		
		JobDetail job = newJob(CheckForInterruptJob.class)
						.withIdentity("hello", "group")
						.build();
		
		Trigger trigger = newTrigger()
							.startNow()
							.withSchedule(simpleSchedule()
									.withIntervalInSeconds(1))
							.build();
		
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.scheduleJob(job, trigger);
			scheduler.start();
			
			Thread.sleep(10*1000);
			scheduler.interrupt(new JobKey("hello", "group"));
			
			//scheduler.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
