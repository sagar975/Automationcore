package com.testproject.core.listener;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.IInvokedMethod;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.testproject.common.DriverFactoryAux;
import com.testproject.core.automation.AutomationContextManager;
import com.testproject.core.automation.SeleniumWebDriverFactory;
import com.testproject.core.dataops.FileIO;
import com.testproject.core.utils.Browser;
import com.testproject.core.utils.ClasspathUtils;
import com.testproject.core.utils.ExtentManager;

public class TestProjectListener extends AutomationListner {
	private static final Logger logger = Logger.getLogger(TestProjectListener.class);

	public void onExecutionStart() {
		Browser.killDriverProcess();
		InputStream is = null;
		try {
			String propsFile = ClasspathUtils.loadFileFromClassPathAsString(
					"config/" + System.getProperty("Environment").toLowerCase() + "/config.properties");
			is = new ByteArrayInputStream(propsFile.getBytes());
			Properties props = new Properties();
			props.load(is);
			AutomationContextManager.setConfigProperties(props);
		} catch (Exception e) {

		} finally {
			is = null;
		}
		String fileName = FileIO.getReportPath();
		logger.info("*********** file path for report is *********** : " + fileName);
		ExtentReports extentReports = ExtentManager.createInstance(fileName);
		AutomationContextManager.setExtentReport(extentReports);
		logger.info("execution of test suite started");

	}

	public void beforeInvocation(IInvokedMethod method, ITestResult result) {

		if (method.isTestMethod()) {
			AutomationContextManager.startMethod(method.getTestMethod().getMethodName(),
					method.getTestMethod().getInstance().getClass().getName(), method.getTestMethod().getDescription());
			WebDriver webDriver = null;

			try {
				if (System.getProperty("browser").equalsIgnoreCase("ie")) {
					webDriver = DriverFactoryAux.drivers.get("ie").get();
				} else if (System.getProperty("browser").equalsIgnoreCase("edge")) {
					webDriver = DriverFactoryAux.drivers.get("edge").get();
				} else if (System.getProperty("browser").equalsIgnoreCase("chrome")
						&& Boolean.parseBoolean(System.getProperty("incognito"))) {

					webDriver = DriverFactoryAux.drivers.get("chromeIncognito").get();
				} else {
					webDriver = SeleniumWebDriverFactory.createInstance();
				}
			}

			catch (MalformedURLException e) {
				logger.error("exception in beforeInvocation", e);
			}

			if (webDriver != null) {
				logger.error("webdriver instance created");
			}
			EventFiringWebDriver eventDriver = new EventFiringWebDriver(webDriver);
			WebDriverListener handler = new TestProjectListener();
			eventDriver.register(handler);

			AutomationContextManager.setWebDriver(eventDriver);

			AutomationContextManager.getMethod().log(Status.PASS,
					"Test Method Started->" + method.getTestMethod().getMethodName());
		}

	}

	public void onExecutionFinish() {
		AutomationContextManager.flushReport();
		logger.info("extent report ready and execution completed");

	}
}
