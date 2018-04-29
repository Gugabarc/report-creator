package com.company.reportcreator;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Salesman {
	private String name;
	private String cnpj;
	private String businessArea;

}
