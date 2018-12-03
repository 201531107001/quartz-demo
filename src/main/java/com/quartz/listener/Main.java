package com.quartz.listener;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.Matcher;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.TriggerListener;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

public class Main {
	public static void main(String[] args){
		JobDetail job1 = newJob(MyJob.class)
						.withIdentity("myjob1", "group1")
						.build();
		
		JobDetail job2 = newJob(MyJob.class)
				.withIdentity("myjob2", "group1")
				.build();
		
		Trigger trigger1 = newTrigger()
							.withIdentity("trigger1", "group1")
							.startNow()
							.withSchedule(simpleSchedule()
									.withIntervalInSeconds(1)
									.repeatForever())
							.build();
		
		Trigger trigger2 = newTrigger()
				.startNow()
				.withSchedule(simpleSchedule()
						.withIntervalInSeconds(1)
						.repeatForever())
				.build();
		
		Scheduler scheduler;
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.scheduleJob(job1, trigger1);
			//scheduler.scheduleJob(job2, trigger2);
			
			JobListener jobListener = new MyJobListener();
			TriggerListener triggerListener = new MyTriggerListener("triggerListener");
			
			//匹配要监听的job
			Matcher<JobKey> jobMatcher = new Matcher<JobKey>() {
				private static final long serialVersionUID = 1L;
				@Override
				public boolean isMatch(JobKey key) {
					if(key.getName().equals("myjob1")){
						return true;
					}else{
						return false;
					}
				}
			};
			
			//匹配要监听的trigger
			Matcher<TriggerKey> triggerMatcher = new Matcher<TriggerKey>() {
				private static final long serialVersionUID = 1L;
				@Override
				public boolean isMatch(TriggerKey key) {
					if(key.getName().equals("trigger1")){
						return true;
					}else{
						return false;
					}
				}
			};
			
			//添加监听器
			scheduler.getListenerManager().addJobListener(jobListener, jobMatcher);
			scheduler.getListenerManager().addTriggerListener(triggerListener, triggerMatcher);
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
