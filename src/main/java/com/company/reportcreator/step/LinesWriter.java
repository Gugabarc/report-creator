package com.company.reportcreator.step;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;

import com.company.reportcreator.dto.ReportDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LinesWriter implements Tasklet, StepExecutionListener {

	private String filename;
	
	@Value("${default.dir}")
    private String defaultRootDir = "/data";
	
	private String defaultOutuputSubdir = "/out/";
	
	private String defaultProcessedSubdir = "/processed/";
	
	private ReportDTO reportDTO;

	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
		log.info("{}", reportDTO);
		
		String filename = StringUtils.substring(this.filename, 0, this.filename.length() - 4);
		
		FileUtils.writeStringToFile(new File(defaultRootDir + defaultOutuputSubdir + filename + ".done.dat"), reportDTO.toString(), "UTF-8");
		return RepeatStatus.FINISHED;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
		reportDTO = (ReportDTO) executionContext.get("reportDTO");
		filename = (String) executionContext.get("filename");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		log.debug("Lines Processor ended.");
		try {
			org.apache.commons.io.FileUtils.moveFile(new File(defaultRootDir + "/processing/" + filename), new File(defaultRootDir + defaultProcessedSubdir + filename));
		} catch (IOException e) {
			log.error("",e);
		}
		return ExitStatus.COMPLETED;
	}
}
