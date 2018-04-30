package com.company.reportcreator.step;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FlowDecision implements JobExecutionDecider {
	
    @Value("${default.dir}")
    private String defaultRootDir = "/data";
    
    private String defaultSubdir = "/in/";
    
	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		File file = finder(defaultRootDir + defaultSubdir);

		if (file == null) {
			return FlowExecutionStatus.STOPPED;
		}
		return FlowExecutionStatus.COMPLETED;
	}

	private File finder(String dirName) {
		File dir = new File(dirName);

		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".dat");
			}
		});

		if (ArrayUtils.isNotEmpty(files)) {
			return files[0];
		}

		return null;

	}

}
