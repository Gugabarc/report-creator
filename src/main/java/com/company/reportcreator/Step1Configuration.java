package com.company.reportcreator;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class Step1Configuration {

	@Bean
    public FlatFileItemReader<Person> fileReader() {
        return new FlatFileItemReaderBuilder<Person>()
            .name("personItemReader")
            .resource(new ClassPathResource("sample-data.csv"))
            .delimited().delimiter(";")
            .names(new String[]{"id", "cpf", "name", "salary"})
            .fieldSetMapper(new CustomFieldsetMapper())
            .build();
    }

	@Bean
	public FlatFileItemWriter<Person> fileWriter() {
		FlatFileItemWriter<Person> writer = new FlatFileItemWriter<Person>();
		writer.setResource(new FileSystemResource("./data/out/teste.dat"));

		DelimitedLineAggregator<Person> aggregator = new DelimitedLineAggregator<Person>();
		aggregator.setDelimiter("|");

		BeanWrapperFieldExtractor<Person> extractor = new BeanWrapperFieldExtractor<>();
		extractor.setNames(new String[] { "name", "cpf", "salary" });
		aggregator.setFieldExtractor(extractor);

		writer.setLineAggregator(aggregator);

		return writer;
	}
}
