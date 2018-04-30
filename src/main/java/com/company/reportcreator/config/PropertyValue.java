package com.company.reportcreator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class PropertyValue {
	
    @Value("${default.files.root.dir:'/data'}")
    private String defaultFilesRootDir;
    
    @Value("${default.file.input.sub.dir:'/in'}")
    private String defaultFileInputSubDir;

    @Value("${default.file.output.sub.dir:'/out'}")
    private String defaultFileOutputSubDir;
    
    @Value("${default.file.processed.sub.dir:'/processed'}")
    private String defaultFileProcessedSubDir;
    
    @Value("${default.file.processing.sub.dir:'/processing'}")
    private String defaultFileProcessingSubDir;
    
    @Value("${default.file.extension:'.dat'}")
    private String defaultFileExtension;
    
    @Value("${default.output.filename.extension:'.done.dat'}")
    private String defaultOutputFilenameExtension;
    
}
