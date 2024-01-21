package com.testproject.core.dataops;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.DataProvider;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.testproject.core.utils.ClasspathUtils;

public class CSVDataProviderUtil {
	private static final Logger log = Logger.getLogger(CSVDataProviderUtil.class);

	@DataProvider(name = "CSVDataProvider")
	public static Iterator<Object[]> geCSVIterator(Method method, char separator) {

		List<Object[]> dataToBeReturned = new ArrayList<Object[]>();
		String fileName = System.getProperty("Environment").toLowerCase() + "/" + method.getName() + ".csv";
		CSVParser parser = new CSVParserBuilder().withSeparator(separator).build();
		String[] data;
		try {
			InputStream is = ClasspathUtils.loadFileFromClasspathAsInputString(fileName);

			if (is == null) {
				log.error("There is null found in finding file : " + fileName);
				throw new RuntimeException("file not found");

			}

			BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
			CSVReader reader = new CSVReaderBuilder(br).withCSVParser(parser).withSkipLines(1).build();

			try {
				while ((data = reader.readNext()) != null) {
					dataToBeReturned.add(data);
				}
			} catch (CsvValidationException ex) {

				ex.printStackTrace();
			}

		} catch (ArrayIndexOutOfBoundsException ae) {

			log.error(ae.getMessage());
		} catch (FileNotFoundException fne) {

			log.error(fne.getMessage());
		} catch (IOException io) {

			log.error(io.getMessage());
		}

		return dataToBeReturned.iterator();

	}

}
