package com.testproject.core.listener;

import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.IInvokedMethod;
import org.testng.ITestResult;

import com.testproject.common.DriverFactoryAux;
import com.testproject.core.automation.AutomationContextManager;

public class AutomationListner extends WebDriverListener {

	private static final Logger logger = Logger.getLogger(AutomationListner.class);

	public void beforeInvocation(IInvokedMethod method, ITestResult result) {

		if (method.isTestMethod()) {

			AutomationContextManager.startMethod(method.getTestMethod().getMethodName(),
					method.getTestMethod().getClass().getName(), method.getTestMethod().getDescription());
			WebDriver webDriver = null;

			try {
				if (System.getProperty("browser").equalsIgnoreCase("ie")) {

					webDriver = DriverFactoryAux.drivers.get("ie").get();
					
				}
				
				else if (System.getProperty("browser").equalsIgnoreCase("edege")) {
					webDriver = DriverFactoryAux.drivers.get("edge").get();
				}
				else {
				if(System.getProperty("browser").equalsIgnoreCase("chrome")&& Boolean.parseBoolean(System.getProperty("Incognito"))) {
				
					webDriver = DriverFactoryAux.drivers.get("chromeIncognito").get();
					
				}
				else {
					
				}
					
				}

			}

			catch (Exception e) {
				logger.error("Exception in before invocation");
			}
		}

	}

}
