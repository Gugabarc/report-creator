package com.company.reportcreator.step;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.company.reportcreator.config.PropertyValue;
import com.company.reportcreator.dto.ReportDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LinesWriter implements Tasklet, StepExecutionListener {

	@Autowired
    private PropertyValue properties;
	
	private String filename;
	private ReportDTO reportDTO;
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		ExecutionContext executionContext = stepExecution
												.getJobExecution()
												.getExecutionContext();
		
		reportDTO = (ReportDTO) executionContext.get("reportDTO");
		filename = (String) executionContext.get("filename");
	}

	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
		log.info("Starting to writed report to file");
		
		String filenameWithoutExtension = com.company.reportcreator.util.FileUtils.removeFileExtension(this.filename);
		
		File file = new File(getFileOutputPath(filenameWithoutExtension));
		
		FileUtils.writeStringToFile(file, reportDTO.formattedReport(), "UTF-8");
		
		log.debug("Finished. Report generated: {}", file.getAbsolutePath());
		
		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		try {
			org.apache.commons.io.FileUtils.moveFile(new File(getProcessingFilePath()), new File(getProcessedFilePath()));
		} catch (IOException e) {
			log.error("Error when tried to move file from processing folder to processed folder", e);
			return ExitStatus.FAILED;
		}
		
		return ExitStatus.COMPLETED;
	}

	private String getProcessedFilePath() {
		return properties.getDefaultFilesRootDir() + properties.getDefaultFileProcessedSubDir() + "/" + filename;
	}

	private String getProcessingFilePath() {
		return properties.getDefaultFilesRootDir() + properties.getDefaultFileProcessingSubDir() + "/" + filename;
	}
	
	private String getFileOutputPath(String filenameWithoutExtension) {
		return properties.getDefaultFilesRootDir() + properties.getDefaultFileOutputSubDir() + "/" + filenameWithoutExtension + properties.getDefaultOutputFilenameExtension();
	}
}
