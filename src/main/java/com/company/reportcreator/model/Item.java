package com.company.reportcreator.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper=false)
public class Item extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private int quantity;
	private double price;

}
