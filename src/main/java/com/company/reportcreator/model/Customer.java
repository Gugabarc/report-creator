package com.company.reportcreator.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper=false)
public class Customer extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
    private String cnpj;
    private String businessArea;

}
