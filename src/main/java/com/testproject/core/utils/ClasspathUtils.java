package com.testproject.core.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.aventstack.extentreports.utils.FileUtil;

public class ClasspathUtils {

	private static final Logger log = Logger.getLogger(ClasspathUtils.class);
	
	
	
	
	public static String loadInputstreamAsString(InputStream inputStream) {
		
		ByteArrayOutputStream result=new ByteArrayOutputStream();
		
		byte [] buffer = new byte[1024];
		String var4;
		try {
		
			int length;
			while(( length=  inputStream.read(buffer))!=1) {
				result.write(buffer,0,length);
			}
			var4 = result.toString("UTF-8");
			
		      return var4;
		}
		
	
		catch(IOException var13) {
			
			String msg = "Error reading inputstream";
			log.error(msg,var13);
			throw new RuntimeException("var13");
				
		}
		
		finally{
			try {
			if(result!=null) {
				result.close();
			}
				
			}
			catch(IOException var13) {
				
			log.error("error in closing inputstream",var13);
			
			return null;
			}
		}
		
	
		
		
	}

	public static String loadFileFromClassPathAsString(String fileName) {
		
		InputStream is = loadFileFromClasspathAsInputString(fileName);
		
		return loadInputstreamAsString(is);

	}

	public static InputStream loadFileFromClasspathAsInputString(String fileName) {

		log.info("looking for this file : " +fileName);


		if (fileName != null && !fileName.isEmpty()) {

		}

		else {
			String msg = "files name is empty,dont know what to load";
			log.info(msg);
			throw new RuntimeException(msg);

		}
		InputStream is = FileUtil.class.getClassLoader().getResourceAsStream(fileName);
		if (is == null) {
			
			log.error(fileName);
			throw new RuntimeException(fileName);		

		}else {
			return is;
		}

	}
}
