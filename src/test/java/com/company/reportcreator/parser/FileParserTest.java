package com.company.reportcreator.parser;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.company.reportcreator.model.Item;

import static org.assertj.core.api.Assertions.*;

@RunWith(Theories.class)
public class FileParserTest {
	
	private FileParser parser;
	
	@DataPoints("validItemsOneSize")
	public static final Object[][] VALID_ITEMS_ONE_SIZE = {{"[01-10-1.5]","01", 10, 1.5}, {"[003ABC-1000-199.5]","003ABC", 1000, 199.5}};
	
	@DataPoints("validItemsTwoSize")
	public static final Object[][] VALID_ITEMS_TWO_SIZE = {{"[01-10-1.5,003ABC-1000-199.5]","01", 10, 1.5 ,"003ABC", 1000, 199.5}};
	
	
	@Before
	public void setup() {
		parser = new FileParser(new File("test"));
	}
	
	@Theory
	@Test
	public void parseItems_withOneItem_success(@FromDataPoints("validItemsOneSize") Object[] validItem) {
		List<Item> items = parser.parseItems((String) validItem[0]);
		
		Item item = Item.builder()
						.id((String) validItem[1])
						.quantity((int) validItem[2])
						.price((double) validItem[3])
						.build();
		
		assertThat(items).containsOnly(item);
	}
	
	@Theory
	@Test
	public void parseItems_withTwoItem_success(@FromDataPoints("validItemsTwoSize") Object[] validItem) {
		List<Item> items = parser.parseItems((String) validItem[0]);
		
		Item item1 = Item.builder()
						.id((String) validItem[1])
						.quantity((int) validItem[2])
						.price((double) validItem[3])
						.build();
		
		Item item2 = Item.builder()
				.id((String) validItem[4])
				.quantity((int) validItem[5])
				.price((double) validItem[6])
				.build();
		
		assertThat(items).containsOnly(item1, item2).hasSize(2);
	}
}
