package com.timing.filter.xmlfile;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

public class JobSchedule {
	
	public static void main(String[] args){
		JobDetail job = newJob(ScanDirectoryJob.class)
						.usingJobData("SCAN_DIR", "doc")
						.build();
		
		Trigger trigger = newTrigger()
							.startNow()
							.withSchedule(simpleSchedule()
									.withIntervalInMinutes(1)
									.repeatForever())
							.build();
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			//提交任务
			scheduler.scheduleJob(job, trigger);
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
