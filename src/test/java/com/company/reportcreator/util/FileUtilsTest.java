package com.company.reportcreator.util;

import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.*;

@RunWith(Theories.class)
public class FileUtilsTest {
	
	@DataPoints("validFilenames")
	public static final String[][] VALID_FILENAMES = {{"TEST.DAT","TEST"}, {"download.zip", "download"}, {"abcd.e", "abcd"}, {"big file.7zip", "big file"}, 
			{"123 4556 678.sh", "123 4556 678"}, {"testWithTwo.Dots.zip", "testWithTwo.Dots"}};
	
	@DataPoints("invalidFilenames")
	public static final String[] INVALID_FILENAMES = {"TESTDAT", "download,zip", "", "	", "test 123 file"};
	
	@Theory
	@Test
	public void removeFileExtension_success(@FromDataPoints("validFilenames") String[] filename) {
		String removedFileExtension = FileUtils.removeFileExtension(filename[0]);
		
		assertThat(removedFileExtension).isEqualTo(filename[1]);
	}
	
	@Theory
	@Test
	public void removeFileExtension_withInvalidParameters(@FromDataPoints("invalidFilenames") String filename) {
		String removedFileExtension = FileUtils.removeFileExtension(filename);
		
		assertThat(removedFileExtension).isEqualTo(filename);
	}
	
	@Test
	public void removeFileExtension_withInvalidParameters() {
		Throwable thrown = catchThrowable(() -> { FileUtils.removeFileExtension(null); });
		
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
			                  .hasNoCause()
			                  .hasMessage("filename is null");
	}
	
}
