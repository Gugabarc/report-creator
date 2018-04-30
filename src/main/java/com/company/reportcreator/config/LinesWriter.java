package com.company.reportcreator.config;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import com.company.reportcreator.dto.ReportDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LinesWriter implements Tasklet, StepExecutionListener {

	private ReportDTO reportDTO;

	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
		log.info("{}", reportDTO);
		return RepeatStatus.FINISHED;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
		reportDTO = (ReportDTO) executionContext.get("reportDTO");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		log.debug("Lines Processor ended.");
		return ExitStatus.COMPLETED;
	}
}
