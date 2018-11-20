package com.me.calendar;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.ParseException;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.CronCalendar;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule; 

/**
 * CronCalendar排除任务执行的时间
 * @author 清明
 *
 */
public class TestCronCalendar {
	public static void main(String[] args) throws ParseException{
		JobDetail job = newJob(MyJob.class)
						.build();
		
		//指明任务停止执行的时间点
		CronCalendar calendar1 = new CronCalendar("0,15,30,45 * * ? * 2-6");
		CronCalendar calendar2 = new CronCalendar("0/5 * * ? * 2-6");
		Trigger trigger = newTrigger()
							.startNow()
							.modifiedByCalendar("calendar1")
							.modifiedByCalendar("calendar2")
							.withSchedule(simpleSchedule()
									.withIntervalInSeconds(1)
									.repeatForever())
							.build();
		
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.addCalendar("calendar1", calendar1, true, false);
			scheduler.addCalendar("calendar2", calendar2, true, false);
			
			scheduler.scheduleJob(job, trigger);
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
