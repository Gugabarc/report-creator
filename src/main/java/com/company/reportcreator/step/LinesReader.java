package com.company.reportcreator.step;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;

import com.company.reportcreator.model.BaseEntity;
import com.company.reportcreator.util.FileUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LinesReader implements Tasklet, StepExecutionListener {
	
	private List<BaseEntity> lines;
    private FileUtils fileUtils;
    private String filename;
    
    @Value("${default.dir}")
    private String defaultRootDir = "/data";
    
    private String defaultSubdir = "/in/";
    
    private String processingDir = "/processing/";

    @Override
    public void beforeStep(StepExecution stepExecution) {
        lines = new ArrayList<>();
        
        File file = finder(defaultRootDir + defaultSubdir);
        
        if(file == null) {
        	return;
        }
        
        filename = file.getName();
        
        try {
			org.apache.commons.io.FileUtils.moveFile(file, new File(defaultRootDir + processingDir + filename));
		} catch (IOException e) {
			log.error("",e);
		}
        
        fileUtils = new FileUtils(finder(defaultRootDir + processingDir));
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
        
        ExecutionContext executionContext = stepExecution
										        .getJobExecution()
										        .getExecutionContext();
        
        executionContext.put("lines", this.lines);
        executionContext.put("filename", this.filename);
        
        log.debug("Lines Reader ended.");
        
        return ExitStatus.COMPLETED;
    }
    
    private File finder(String dirName){
        File dir = new File(dirName);
        
        File[] files = dir.listFiles(new FilenameFilter() { 
                 public boolean accept(File dir, String filename)
                      { return filename.endsWith(".dat"); }
        });
        
        if(ArrayUtils.isNotEmpty(files)) {
        	return files[0];
        }
        
        return null;

    }
}
