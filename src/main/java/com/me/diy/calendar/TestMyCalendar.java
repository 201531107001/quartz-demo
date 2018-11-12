package com.me.diy.calendar;


import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

/**
 *  使用  AnnualCalender 来 排除不需要执行的时间
 * @author 清明
 *
 */
public class TestMyCalendar {
	
	public static void main(String[] args){
		MyCalendar myCalendar = new MyCalendar();
		myCalendar.setSecondExcluded(5);
		myCalendar.setSecondExcluded(4);
		myCalendar.setSecondExcluded(3);
		myCalendar.setSecondExcluded(2);
		myCalendar.setSecondExcluded(1);
		
		//定义一个Trigger
		Trigger trigger = newTrigger().withIdentity("trigger1", "group1") 
		    .startNow()//一旦加入scheduler，立即生效
		    .modifiedByCalendar("12345Second") //使用Calendar !!
		    .withSchedule(simpleSchedule()
		        .withIntervalInSeconds(1) 
		        .repeatForever()) 
		    .build();
		
		JobDetail job = newJob(MyJob.class)
						.build();
		
		Scheduler scheduler;
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			
			//scheduler加入这个Calendar
			scheduler.addCalendar("12345Second", myCalendar, false, false); 
			scheduler.scheduleJob(job, trigger);
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

}
