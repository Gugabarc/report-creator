package com.company.reportcreator.step;

import java.io.File;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.reportcreator.config.PropertyValue;
import com.company.reportcreator.util.FileUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FlowDecision implements JobExecutionDecider {
	
	@Autowired
    private PropertyValue properties;
    
	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		log.info("Searching for new files");
		File[] files = FileUtils.findAll(properties.getDefaultFilesRootDir() + properties.getDefaultFileInputSubDir(), properties.getDefaultFileExtension());

		if (ArrayUtils.isEmpty(files)) {
			log.info("Nothing to process");
			return FlowExecutionStatus.STOPPED;
		}
		
		log.info("Found {} files to be processed", files.length);
		
		return FlowExecutionStatus.COMPLETED;
	}

}
