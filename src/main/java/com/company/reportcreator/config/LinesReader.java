package com.company.reportcreator.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.company.reportcreator.model.BaseEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LinesReader implements Tasklet, StepExecutionListener {
	
	private List<BaseEntity> lines;
    private FileUtils fileUtils;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        lines = new ArrayList<>();
        fileUtils = new FileUtils("sample-data.csv");
        log.debug("Lines Reader initialized.");
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        BaseEntity line = fileUtils.readLine();
        
        while (line != null) {
            lines.add(line);
            log.debug("Read line: " + line.toString());
            line = fileUtils.readLine();
        }
        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        fileUtils.closeReader();
        
        stepExecution
          .getJobExecution()
          .getExecutionContext()
          .put("lines", this.lines);
        
        log.debug("Lines Reader ended.");
        
        return ExitStatus.COMPLETED;
    }
}
