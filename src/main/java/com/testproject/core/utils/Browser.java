package com.testproject.core.utils;

import java.time.Duration;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.Status;
import com.testproject.core.automation.AutomationContextManager;
import com.testproject.core.listener.TestProjectListener;

public class Browser {

	static Duration EXPLICIT_WAIT = Duration.ofSeconds(15);
	private static final Logger logger = Logger.getLogger(Browser.class);
	private static final String KILLDRIVEREXCEPTION = "Exception in killDriverProcess : ";

	public static void killDriverProcess() {
		String node = System.getProperty("huburl");
		if (node == null || node.equalsIgnoreCase("")) {
			try {
				Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
				Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");
			}

			catch (Exception e) {
				logger.error(KILLDRIVEREXCEPTION);
			}

		}
	}

	public static void waitForPageLoad() {

		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {

			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}

		};

		WebDriverWait wait = new WebDriverWait(AutomationContextManager.getDriver(), EXPLICIT_WAIT);
		boolean isPageLoad = wait.until(pageLoadCondition);
		if (!isPageLoad) {
			AutomationContextManager.getTest().log(Status.WARNING, "page didnnt load");

		}
	}

}
