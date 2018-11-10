package com.me.second;

import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule; 

/**
 * 任务每秒执行一次，但执行任务的时间大于一秒，采用的是多线程执行
 * @author 清明
 *
 */
public class MySchedule {
	public static void main(String[] args){
		
		Trigger trigger = newTrigger()
							.startNow()
							.withSchedule(simpleSchedule()
									.withIntervalInSeconds(1)
									.repeatForever())
							//.usingJobData("num", 0)
							.build();
		
		JobDetail job = newJob(MyJob.class)
						.usingJobData("num", new Integer(0)) //每个JobDetal不会共享
						.build();
		
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.scheduleJob(job, trigger);
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
