package com.me.third;

import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.TriggerBuilder.newTrigger;

import java.util.HashSet;
import java.util.Set;

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
							.usingJobData("name", "gql")
							.build();
		
		Trigger trigger2 = newTrigger()
				.startNow()
				.withSchedule(calendarIntervalSchedule()
						.withIntervalInSeconds(2)) 
				.usingJobData("name", "gqm")
				.build();
		
		JobDetail job1 = newJob(MyJob.class)
						.withIdentity("Job001")
						.build();
		
		JobDetail job2 = newJob(MyJob.class)
				.withIdentity("Job002", "001")
				.build();
		
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
			
			Set<Trigger> set = new HashSet<>();
			set.add(trigger1);
			set.add(trigger2);
			scheduler.scheduleJob(job1, set, true); //一个job配置多个触发器
			
//			scheduler.scheduleJob(job1, trigger1);
//			scheduler.scheduleJob(job2, trigger2);
			//scheduler.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
	}
}
