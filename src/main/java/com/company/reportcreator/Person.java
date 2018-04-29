package com.company.reportcreator;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Person {
	private String name;
	private String cpf;
	private double salary;

}
