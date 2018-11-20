package com.me.jobstore;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;

import static org.quartz.JobBuilder.newJob;

import java.text.ParseException;

public class TestJobStoreTX {
	public static void main(String[] agrs){
		try {
			
//			JobDetail job = newJob(MyJob.class)
//							.build();
//			
//			
//			CronTriggerImpl trigger = new CronTriggerImpl();
//			trigger.setCronExpression("0/5 * * * * ?");
//			trigger.setName("triger");
			
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			
			//添加一次后就会将job、triger信息存储到数据库中，下次就不需要自己去主动添加，就会从数据库中读取信息
			//scheduler.scheduleJob(job, trigger);
			scheduler.start();
			
			Thread.sleep(1000*20);
			scheduler.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
//		} catch (ParseException e) {
//			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
