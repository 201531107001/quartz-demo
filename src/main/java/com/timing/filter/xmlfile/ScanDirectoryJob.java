package com.timing.filter.xmlfile;

import java.io.File;
import java.io.FileFilter;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScanDirectoryJob implements Job {
	private static final Logger logger = LoggerFactory.getLogger(ScanDirectoryJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap dataMap = jobDetail.getJobDataMap();
		String dirName = dataMap.getString("SCAN_DIR");

		if (dirName == null) {
			throw new JobExecutionException("Directory not configured");
		}

		File dir = new File(dirName);
		if (!dir.exists()) {
			throw new JobExecutionException("Invalid Dir" + dirName);
		}

		FileFilter filter = new MyFileFiltter(".xml");
		File[] files = dir.listFiles(filter);

		if (files == null || files.length < 0) {
			logger.info("no xml files found in {}", dir);
			return;
		}

		for (File file : files) {
			File aFile = file.getAbsoluteFile();
			long fileSize = file.length();
			String message = aFile + ":" + fileSize;
			logger.info(message);
		}
	}
}
