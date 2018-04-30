package com.company.reportcreator.parser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.NumberUtils;

import com.company.reportcreator.model.BaseEntity;
import com.company.reportcreator.model.Customer;
import com.company.reportcreator.model.Item;
import com.company.reportcreator.model.Sale;
import com.company.reportcreator.model.Salesman;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileParser {

    private CSVReader csvReader;
    private FileReader fileReader;
    private File file;

    public FileParser(File file) {
    	if(file == null) {
    		throw new IllegalArgumentException("Null file");
    	}
        this.file = file;
    }

    public BaseEntity readLine() {
    	BaseEntity entity = null;
        try {
            if (csvReader == null) {
            	initReader();
            }
            
            String[] line = csvReader.readNext();
            
            if (ArrayUtils.isEmpty(line)) {
            	return null;
            }
            
            entity = createEntity(entity, line);
            
            return entity;
            
        } catch (Exception e) {
            log.error("Error while reading line in file: " + this.file.getName());
            return null;
        }
    }

	private BaseEntity createEntity(BaseEntity entity, String[] line) {
		if("001".equals(line[0])) {
			entity = createSalesman(line);
			log.info("Created salesman. {}", entity);
			
		} else if("002".equals(line[0])) {
			entity = createCustomer(line);
			log.info("Created customer. {}", entity);
			
		} else if("003".equals(line[0])) {
			entity = createSale(line);
			log.info("Created sale. {}", entity);
		}
		return entity;
	}

	private BaseEntity createSale(String[] line) {
		BaseEntity entity;
		entity = Sale.builder()
					.id(line[1])
					.items(parseItems(line[2]))
					.salesmanName(line[3])
					.build();
		return entity;
	}

	private BaseEntity createCustomer(String[] line) {
		BaseEntity entity;
		entity = Customer.builder()
					.cnpj(line[1])
					.name(line[2])
					.businessArea(line[3])
					.build();
		return entity;
	}

	private BaseEntity createSalesman(String[] line) {
		BaseEntity entity;
		entity = Salesman.builder()
					.cpf(line[1])
					.name(line[2])
					.build();
		return entity;
	}
    
    public List<Item> parseItems(String itemsString){
    	List<Item> items = new ArrayList<>();
    	
    	itemsString = StringUtils.remove(itemsString, "[");
    	itemsString = StringUtils.remove(itemsString, "]");
    	
    	String[] itemsArray = StringUtils.split(itemsString, ",");
    	
    	for (int i = 0; i < itemsArray.length; i++) {
			String itemString = itemsArray[i];
			
			String[] itemPart = StringUtils.split(itemString, "-");
			
			Item item = Item.builder()
							.id(itemPart[0])
							.quantity(NumberUtils.parseNumber(itemPart[1], Integer.class))
							.price(NumberUtils.parseNumber(itemPart[2], Double.class))
							.build();
			
			items.add(item);
		}
    	
    	return items;
    }

    private void initReader() throws Exception {
        if (fileReader == null) fileReader = new FileReader(file);
        
        if (csvReader == null) {
        	CSVParser parser = new CSVParserBuilder()
									.withSeparator('ç')
									.build();

        	csvReader = new CSVReaderBuilder(fileReader)
									.withCSVParser(parser)
									.build();
        }
    }

    public void closeReader() {
        try {
            csvReader.close();
            fileReader.close();
        } catch (IOException e) {
        	log.error("Error while closing reader.");
        }
    }

}
