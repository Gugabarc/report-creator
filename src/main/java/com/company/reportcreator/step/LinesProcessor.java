package com.company.reportcreator.step;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
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
import com.google.common.collect.Iterables;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LinesProcessor implements Tasklet, StepExecutionListener {

	private List<BaseEntity> entities;
	private ReportDTO reportDTO;

	@SuppressWarnings("unchecked")
	@Override
	public void beforeStep(StepExecution stepExecution) {
		ExecutionContext executionContext = stepExecution
												.getJobExecution()
												.getExecutionContext();

		this.entities = (List<BaseEntity>) executionContext.get("entities");
	}

	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
		log.info("Starting to summarize data for report");
		
		int salesmanTotal = 0;
		int customerTotal = 0;
		List<Sale> sales = new ArrayList<>();

		for (BaseEntity line : entities) {
			if (line instanceof Salesman) {
				salesmanTotal++;

			} else if (line instanceof Customer) {
				customerTotal++;

			} else if (line instanceof Sale) {
				Sale sale = (Sale) line;
				sales.add(sale);
			}
		}

		this.reportDTO = ReportDTO.builder()
								.customerTotal(customerTotal)
								.salesmanTotal(salesmanTotal)
								.build();

		if(CollectionUtils.isNotEmpty(sales)) {
			sales.sort((s1, s2) -> s2.getTotalItemsPrice().compareTo(s1.getTotalItemsPrice()));
			
			this.reportDTO.setMostExpensiveSaleId(sales.get(0).getId());
			this.reportDTO.setWorstSalesmanName((Iterables.getLast(sales).getSalesmanName()));
		}

		log.info("Report finished{}", reportDTO);

		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();

		executionContext.put("reportDTO", reportDTO);

		return ExitStatus.COMPLETED;
	}
}