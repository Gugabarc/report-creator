package com.company.reportcreator.model;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper=false)
public class Sale extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
    private List<Item> items;
    private String salesmanName;
    
    public Double getTotalItemsPrice() {
    	double total = 0;
    	
    	if(items == null) {
    		return total;
    	}
    	
    	for (Item item : items) {
    		total += item.getPrice() * item.getQuantity();
		}
    	
    	return total;
    }

}
