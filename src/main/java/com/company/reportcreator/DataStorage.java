package com.company.reportcreator;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DataStorage {
	
	private List<Person> persons = new ArrayList<>();
	private List<Salesman> salesmen = new ArrayList<>();

}
