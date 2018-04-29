package com.company.reportcreator;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class PersonItemProcessor implements ItemProcessor<Person, Person>{

    @Override
    public Person process(final Person person) throws Exception {
        System.out.println("Converting (" + person + ") into (" + person + ")");

        return person;
    }
}
