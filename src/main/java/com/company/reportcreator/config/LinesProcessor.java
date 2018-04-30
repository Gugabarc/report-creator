package com.company.reportcreator.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import com.company.reportcreator.dto.ReportDTO;
import com.company.reportcreator.model.BaseEntity;
import com.company.reportcreator.model.Customer;
import com.company.reportcreator.model.Sale;
import com.company.reportcreator.model.Salesman;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LinesProcessor implements Tasklet, StepExecutionListener {

    private List<BaseEntity> lines;
    
    private ReportDTO reportDTO;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
    	int salesmanTotal = 0;
		int customerTotal = 0;
		List<Sale> sales = new ArrayList<>();
		
		for (BaseEntity line : lines) {
			if(line instanceof Salesman) {
				salesmanTotal++;
				
			} else if(line instanceof Customer) {
				customerTotal++;
				
			} else if(line instanceof Sale) {
				Sale sale = (Sale) line;
				sales.add(sale);
			}
		}
		
		sales.sort((s1, s2) -> s2.getTotalItemsPrice().compareTo(s1.getTotalItemsPrice()));
		
		this.reportDTO = ReportDTO.builder()
								.customerTotal(customerTotal)
								.salesmanTotal(salesmanTotal)
								.mostExpensiveSaleId(sales.get(0).getId())
								.build();
		
		log.info("{}", reportDTO);
		
		FileUtils.writeStringToFile(new File("./data/out/out.txt"), reportDTO.toString(), "UTF-8");
		
        return RepeatStatus.FINISHED;
    }

    @SuppressWarnings("unchecked")
	@Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution
										          .getJobExecution()
										          .getExecutionContext();
        
        this.lines = (List<BaseEntity>) executionContext.get("lines");
        
        log.debug("Lines Processor initialized.");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
    	ExecutionContext executionContext = stepExecution
		          .getJobExecution()
		          .getExecutionContext();
    	
    	executionContext.put("reportDTO", reportDTO);
    	
        return ExitStatus.COMPLETED;
    }
}