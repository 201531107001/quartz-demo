package com.quartz.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyJobListener implements JobListener{
	private static final Logger logger = LoggerFactory.getLogger(MyJobListener.class);
	
	// getName()主要用于记录日志，对于由特定 Job引用的 JobListener，注册在 JobDetail上的监听器名称必须匹配从监听器上 getName()方法的返回值
	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	//Scheduler 在  JobDetail 将要被执行时调用这个方法
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		String jobName = context.getJobDetail().getKey().getName();
		logger.info(jobName+" is about to be executed!");
	}

	//Scheduler在 JobDetail即将被执行，但又被  TriggerListener否决了时调用这个方法
	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		String jobName = context.getJobDetail().getKey().getName();
		logger.info(jobName+" was vetoed and not executed!");
	}

	//Scheduler 在  JobDetail 被执行之后调用这个方法
	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		String jobName = context.getJobDetail().getKey().getName();
		logger.info(jobName+" was executed!");
	}

}
