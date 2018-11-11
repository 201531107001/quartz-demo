package com.me.calendar;


import java.util.GregorianCalendar;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.AnnualCalendar;
import static org.quartz.DateBuilder.newDate;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

/**
 *  使用  AnnualCalender 来 排除银行节日
 * @author 清明
 *
 */
public class TestCalendar {
	
	public static void main(String[] args){
		//定义一个每年执行Calendar，精度为天，即不能定义到2.25号下午2:00
		AnnualCalendar cal = new AnnualCalendar(); 
		java.util.Calendar excludeDay = new GregorianCalendar();
		
		excludeDay.setTime(newDate().inMonthOnDay(2, 25).build());
		cal.setDayExcluded(excludeDay, true);  //设置排除2.25这个日期
		
		//定义一个Trigger
		Trigger trigger = newTrigger().withIdentity("trigger1", "group1") 
		    .startNow()//一旦加入scheduler，立即生效
		    .modifiedByCalendar("FebCal") //使用Calendar !!
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
			scheduler.addCalendar("FebCal", cal, false, false); 
			scheduler.scheduleJob(job, trigger);
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

}
