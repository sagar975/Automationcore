package com.testproject.core.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.*;
import com.testproject.core.automation.AutomationContextManager;
import com.testproject.core.listener.AutomationListner;

public class AutomationAssertion {

	private static final Logger logger = Logger.getLogger(AutomationAssertion.class);
	private static int counter = 1;
	final SoftAssert assertion = new SoftAssert();

	public void assertAll() {
		this.assertion.assertAll();
	}

	public <T> void assertEquals(String stepName, T expected, T actual, boolean screenshot) {
		this.assertion.assertEquals(actual, expected, stepName);
		this.postAssert(stepName, expected, actual, screenshot);

	}

	private <T> void postAssert(String stepName, T expected, T actual, boolean screenshot) {

		Status logStatus = Status.PASS;
		if (!expected.equals(actual)) {
			logStatus = Status.FAIL;
		}

		log(logStatus, (String) actual);
		if (screenshot) {

			String screenshotpath = this.screenShot();
			try {
				logWithScreenshot(logStatus, stepName,
						MediaEntityBuilder.createScreenCaptureFromPath(screenshotpath).build());
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		AutomationContextManager.flushReport();

	}

	public String screenShot() {

		try {
			WebDriver driver = null;
			WebDriver wd = AutomationContextManager.getDriver();
			if (wd instanceof EventFiringWebDriver) {
				driver = ((EventFiringWebDriver) wd).getWrappedDriver();
			} else {
				driver = wd;
			}

			File screenShotSource = (File) ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss_a");
			getIncrCounter();
			String fileName = sdf.format(new Date());
			FileUtils.copyFile(screenShotSource, new File(fileName));
			return fileName;

		}

		catch (IOException var7) {
			logger.error("exception in Assertion ", var7);
			return null;
		}

	}

	public static int getIncrCounter() {

		return ++counter;
	}

	public static void log(Status status, String markup) {
		AutomationContextManager.getMethod().log(status, markup);
	}

	public static void logWithScreenshot(Status status, String markup, MediaEntityModelProvider screenshot) {

		AutomationContextManager.getMethod().log(status, markup, screenshot);
	}

	public static void logFailed(String markup) {
		log(Status.FAIL, markup);
	}

	public static void logPassed(String markup) {
		log(Status.PASS, markup);
	}

	public static void logSkipped(String markup) {
		log(Status.SKIP, markup);
	}
}
