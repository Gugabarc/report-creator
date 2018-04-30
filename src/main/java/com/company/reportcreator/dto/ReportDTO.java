package com.company.reportcreator.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int salesmanTotal;
	private int customerTotal;
	private String mostExpensiveSaleId;
	private String worstSalesmanName; 

}
