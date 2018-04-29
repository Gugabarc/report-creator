package com.company.reportcreator;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;

@Component
public class CustomFieldsetMapper implements FieldSetMapper<Person> {
 
    public Person mapFieldSet(FieldSet fieldSet) {
    	if("001".equals(fieldSet.readString(0))) {
    		Person person = Person.builder()
    							.cpf(fieldSet.readString(1))
    							.name(fieldSet.readString(2))
    							.salary(fieldSet.readDouble(3))
    							.build();
    		
    		
    		
    		
    		return person;
    	}
        return null;
    }
}
