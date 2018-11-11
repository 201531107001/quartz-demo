package com.interrupt.job;

import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

/**
 * 可以提供 Job 部署时所用的 Job 的名称和组名调用 Scheduler 的 interrupte() 方法
 * Scheduler 接着会调用你的 Job 的 interrupt() 方法。
 * @author 清明
 *
 */
public class CheckForInterruptJob implements InterruptableJob{

	private boolean flag = true;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		while(flag){
			System.out.println("hello");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		System.out.println("中断作业");
		flag = false;
	}

}
