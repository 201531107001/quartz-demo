package com.me.third;

import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.CalendarIntervalScheduleBuilder.calendarIntervalSchedule;

/**
 * 一个job对应一个trigger
 * @author 清明
 *
 */
public class MySchedule {
	public static void main(String[] args){
		
		Trigger trigger1 = newTrigger()
							.startNow()
							.withSchedule(calendarIntervalSchedule()
									.withIntervalInSeconds(1)) //每秒执行一次
							.build();
		
		Trigger trigger2 = newTrigger()
				.startNow()
				.withSchedule(calendarIntervalSchedule()
						.withIntervalInSeconds(1)) 
				.build();
		
		JobDetail job1 = newJob(MyJob.class)
						.withIdentity("Job001", "001")
						.usingJobData("name", "gqm")
						.build();
		
		JobDetail job2 = newJob(MyJob.class)
				.withIdentity("Job002", "001")
				.usingJobData("name", "gql")
				.build();
		
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.scheduleJob(job1, trigger1);
			scheduler.scheduleJob(job2, trigger2);
			scheduler.start();
			//scheduler.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
	}
}
