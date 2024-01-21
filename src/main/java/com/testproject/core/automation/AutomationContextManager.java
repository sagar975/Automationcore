package com.testproject.core.automation;

import java.util.Properties;

import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.testproject.core.utils.AutomationAssertion;

public class AutomationContextManager {

	private static ExtentReports EXTENT;

	private static ThreadLocal<ExtentTest> EXTENT_TEST_CASE_THREADLOCAL = new InheritableThreadLocal<ExtentTest>();

	private static ThreadLocal<AutomationAssertion> ASSERT_THREADLOCAL = new InheritableThreadLocal<AutomationAssertion>();

	private static ThreadLocal<WebDriver> WEBDRIVER = new ThreadLocal<WebDriver>();
	private static ThreadLocal<ExtentTest> EXTENT_TEST_METHOD_THREADLOCAL = new InheritableThreadLocal<ExtentTest>();
// most of the methods in this class will be static as these will be reusbale in
	// most of @Test methods

	public static void setConfigProperties(Properties configProperties) {
		configProperties = configProperties;
	}

	public static void setExtentReport(ExtentReports extentReports) {
		EXTENT = extentReports;

	}

	public static WebDriver getDriver() {
		return WEBDRIVER.get();
	}

	public static ExtentTest startMethod(String methodName, String className, String description) {

		ExtentTest test = getTest().createNode(methodName, description);
		EXTENT_TEST_CASE_THREADLOCAL.set(test);
		AutomationAssertion assertion = new AutomationAssertion();
		ASSERT_THREADLOCAL.set(assertion);
		return test;
	}

	public static void setTest(ExtentTest extentTest) {
		EXTENT_TEST_CASE_THREADLOCAL.set(extentTest);
	}

	public static ExtentTest getTest() {
		return EXTENT_TEST_CASE_THREADLOCAL.get();
	}

	public static void setWebDriver(WebDriver driver) {
		WEBDRIVER.set(driver);
	}

	public static ExtentTest getMethod() {
		return EXTENT_TEST_METHOD_THREADLOCAL.get();
	}

	public static synchronized void flushReport() {
		EXTENT.flush();
	}

}
