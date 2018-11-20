package com.me.trigger;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import com.me.calendar.MyJob;
import static org.quartz.JobBuilder.newJob;
import java.text.ParseException;
import java.util.Calendar;


public class TestCronTrigger {
	
	public static void main(String[] args) throws ParseException{
		JobDetail job = newJob(MyJob.class)
						.build();
		
		CronTriggerImpl trigger = new CronTriggerImpl(); 
		trigger.setCronExpression("0/5 * * * * ?");
		trigger.setName("trigger");
		
		// Trigger  将会在Scheduler 启动后的下一天开始触发，并只在开始触发后的两天内有效
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		trigger.setStartTime(cal.getTime());
		
		cal.add(Calendar.DATE, 2);
		trigger.setEndTime(cal.getTime());
		
		Scheduler scheduler;
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.scheduleJob(job, trigger);
			
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
		
	}
}
