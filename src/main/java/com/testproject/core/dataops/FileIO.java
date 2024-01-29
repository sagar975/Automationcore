package com.testproject.core.dataops;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.testproject.core.constants.Constants;
import com.testproject.core.utils.ClasspathUtils;

public class FileIO {

	private static final Logger log = Logger.getLogger(FileIO.class);

	/*
	 * return date time for specified file
	 *
	 */

	public static String getFileCratedDateTime(String source) {

		try {
			File file = new File(source);
			if (file.exists()) {
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(new Date(file.lastModified()));
				SimpleDateFormat parseFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
				Date date;
				date = parseFormat.parse(calendar.getTime().toString());
				SimpleDateFormat format = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss_a");
				return format.format(date);

			}
		} catch (ParseException e) {
			log.error("Exception in GetfileCreatedDateTime: " + e.getMessage());
			e.printStackTrace();
			return null;
		}

		return null;

	}

	public static String getReportPath2() {
		
		/*
		 * currently not in use added new version below to archive  html extent reports
		 */
		String reportPath;
		String htmlArchiveFilePath;
		String logfolderpath;
		String screenshotfolderpath;

		if (System.getProperty("ReportFolderPath") == null
				|| System.getProperty("ReportFolderPath").equalsIgnoreCase("")) {
			reportPath = Constants.AUTOMATION_REPORT_PATH;
			createDirs(reportPath);
		}

		else {
			reportPath = System.getProperty("ReportFolderPath");
			createDirs(reportPath);
			log.info(reportPath);

		}
		log.info("report path is - " + reportPath);
		String finalReportfilename = reportPath+"\\reports\\" +Constants.AUTOMATION_EXTENT_REPORT_NAME_PREFIX
				+ Constants.EXTENT_REPORT_NAME_SUFFIX;

		// move the results file to archive folder
		String htmlArchiveFileName = Constants.AUTOMATION_EXTENT_REPORT_NAME_PREFIX + "_"
				+ FileIO.getFileCratedDateTime(finalReportfilename) + ".html";

		//
		// archive report folder path
		if (System.getProperty("ReportFolderPath") == null
				|| System.getProperty("ReportFolderPath").equalsIgnoreCase("")) {
			htmlArchiveFilePath = Constants.AUTOMATION_REPORT_ARCHIVE_PATH;
			htmlArchiveFilePath+="\\reports\\Archive\\";
			createDirs(htmlArchiveFilePath);
		} else {
			htmlArchiveFilePath = System.getProperty("ReportFolderPath") +"Archive" + "\\";
			logfolderpath = System.getProperty("ReportFolderPath") + "\\" + "logs" + "\\";
			screenshotfolderpath = System.getProperty("ReportFolderPath") + "\\" + "screenshots" + "\\";
			createDirs(htmlArchiveFilePath);
			createDirs(logfolderpath);
			createDirs(screenshotfolderpath);

		}

		FileIO.moveFile(new File(finalReportfilename), new File(htmlArchiveFilePath + htmlArchiveFileName));
		return finalReportfilename;

	}
	
	public static String getReportPath() {
		String reportPath;
		if(System.getProperty("ReportFolderPath")!=null &&System.getProperty("ReportFolderPath").equalsIgnoreCase("") ) {
			reportPath = System.getProperty("ReportFolderPath");
			createDirs(reportPath);
		}
		else {
			reportPath = Constants.AUTOMATION_REPORT_PATH;
			reportPath+="\\reports\\";
			createDirs(reportPath);
		}
		log.info("report path is : " + reportPath);
		String htmlArchiveFileName =getFileCratedDateTime(reportPath);
		String htmlArchiveFilePath;
		if(System.getProperty("ReportFolderPath")!=null && !System.getProperty("ReportFolderPath").equalsIgnoreCase("")) {
			htmlArchiveFilePath = System.getProperty("ReportFolderPath") + "\\" + "Archive" + "\\";
			String logfolderpath = System.getProperty("ReportFolderPath") + "\\" + "logs" + "\\";
			String screenshotfolderpath = System.getProperty("ReportFolderPath") + "\\" + "screenshots" + "\\";
			createDirs(htmlArchiveFilePath);
			createDirs(logfolderpath);
			createDirs(screenshotfolderpath);

		}
		else {
			htmlArchiveFilePath=Constants.AUTOMATION_REPORT_ARCHIVE_PATH;
			htmlArchiveFilePath+="";
			
			createDirs(htmlArchiveFilePath);
		}
			
	FileIO.moveFile(new File(htmlArchiveFileName), new File(htmlArchiveFilePath + htmlArchiveFileName));
		return reportPath;
				
	}

	public static void moveFile(File source, File destination) {
		try {

			if (source.exists()) {
				if (!destination.exists()) {

					FileUtils.moveFile(source, destination);

				}
			}
		} catch (IOException e) {
			log.error("Exception while moving : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void createDirs(String directory) {
		File files = new File(directory);
		if (!files.exists()) {

			if (files.mkdirs()) {
				log.info(files.getAbsolutePath());
			}
			log.info(files.getAbsolutePath());
		}

	}

}
