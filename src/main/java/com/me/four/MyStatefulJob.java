package com.me.four;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

/**
 * StatefulJob有状态的job
 * @author 清明
 *
 */
public class MyStatefulJob implements StatefulJob{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
	}

}
