package com.company.reportcreator.step;

import static org.apache.commons.io.FileUtils.moveFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import com.company.reportcreator.model.BaseEntity;
import com.company.reportcreator.parser.FileParser;
import com.company.reportcreator.util.FileUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LinesReader implements Tasklet, StepExecutionListener {

	private List<BaseEntity> entities;
	private String filename;

	@Autowired
	private PropertyValue properties;

	private FileParser parser;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		entities = new ArrayList<>();

		try {
			File unprocessedFile = FileUtils.findFirst(
					properties.getDefaultFilesRootDir() + properties.getDefaultFileInputSubDir(),
					properties.getDefaultFileExtension()
			);
			
			filename = unprocessedFile.getName();
			
			moveFile(unprocessedFile, 
					new File(properties.getDefaultFilesRootDir() + properties.getDefaultFileProcessingSubDir() + "/" + filename)
			);

			File processingFile = FileUtils.findFirst(
					properties.getDefaultFilesRootDir() + properties.getDefaultFileProcessingSubDir(),
					properties.getDefaultFileExtension()
			);
			
			parser = new FileParser(processingFile);

		} catch (Exception e) {
			log.error("Error in reading lines step", e);
		}
	}

	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
		log.info("Starting reading file's lines. fileName: {}", filename);
		
		BaseEntity entity = null;

		while ((entity = parser.readLine()) != null) {
			log.debug("Read line: " + entity.toString());
			entities.add(entity);
		}

		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		parser.closeReader();

		ExecutionContext executionContext = stepExecution
											.getJobExecution()
											.getExecutionContext();

		executionContext.put("entities", this.entities);
		executionContext.put("filename", this.filename);

		log.info("Finished reading lines");

		return ExitStatus.COMPLETED;
	}
}
