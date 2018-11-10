package com.timing.filter.xmlfile;

import java.util.Date;

import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleScheduler {
	private static final Logger logger = LoggerFactory.getLogger(ScanDirectoryJob.class);
	
	public static void main(String[] args) {
		SimpleScheduler simple = new SimpleScheduler();
		simple.startScheduler();
	}
	
	public void startScheduler(){
		Scheduler scheduler = null;
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
			logger.info("Scheduler start at"+ new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void modifyScheduler(Scheduler scheduler){
		try {
			if(scheduler.isInStandbyMode()){
				//pause the schedule
				scheduler.standby();
			}
			
			//do something
			
			//restart it
			scheduler.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
