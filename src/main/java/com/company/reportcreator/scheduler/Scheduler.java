package com.company.reportcreator.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@EnableScheduling
@Slf4j
public class Scheduler {

	@Autowired
	private JobLauncher launcher;
	
	@Autowired
	private Job job;
	
	@Scheduled(cron="*/20 * * * * *")
	public void scheduler() {
		JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
		try {
			JobExecution execution = launcher.run(job, jobParameters);
			
			log.info("Execution status", execution.getStatus());
		} catch (JobExecutionAlreadyRunningException e) {
			log.error("", e);
		} catch (JobRestartException e) {
			log.error("", e);
		} catch (JobInstanceAlreadyCompleteException e) {
			log.error("", e);
		} catch (JobParametersInvalidException e) {
			log.error("", e);
		}
	}
}
