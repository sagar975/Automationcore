package com.testproject.common;

import org.apache.log4j.Logger;

import com.testproject.core.listener.AutomationListner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.function.Supplier;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverFactoryAux {

	private static final Logger logger = Logger.getLogger(DriverFactoryAux.class);

	private static final boolean isRemoteDriver = !(System.getProperty("hubUrl") == null
			|| System.getProperty("hubUrl").isEmpty());
	
	
	public static HashMap<String , Supplier<WebDriver>> drivers;
	
	
	

	private static final Supplier<WebDriver> chromeDriverIncongnito = () -> {

		ChromeOptions options = new ChromeOptions();

		if (isRemoteDriver) {

			options.setCapability("platformName", System.getProperty("platform"));
			options.setCapability("browserName", System.getProperty("browser"));

			try {
				return new RemoteWebDriver(new URL(System.getProperty("hubUrl")), options);

			} catch (MalformedURLException e) {
				logger.error("Exception in beforeInvocation");
			}

		}

		return new ChromeDriver(options);

	};

	public static final Supplier<WebDriver> edgeDriver = () -> {

		if (isRemoteDriver) {

			EdgeOptions edgeOptions = new EdgeOptions();
			edgeOptions.setCapability("platformName", System.getProperty("platform"));
			edgeOptions.setCapability("browserName", System.getProperty("browser"));
			try {
				return new RemoteWebDriver(new URL(System.getProperty("hubUrl")), edgeOptions);
			} catch (MalformedURLException x) {
				logger.error("Exception in beforeInvocation");
			}
		}

		return new EdgeDriver();

	};

	public static final Supplier<WebDriver> ieDriver = () -> {

		InternetExplorerOptions ieOptions = new InternetExplorerOptions();

		if (isRemoteDriver) {
			ieOptions.setCapability("platformName", System.getProperty("platform"));
			ieOptions.setCapability("browserName", System.getProperty("browser"));
			try {
				return new RemoteWebDriver(new URL(System.getProperty("hubUrl")), ieOptions);
			} catch (MalformedURLException x) {
				logger.error("Exception in beforeInvocation");
			}

		}

		return new InternetExplorerDriver(ieOptions);
	};
	static {
		drivers = new HashMap();
		drivers.put("chromeIncognito", chromeDriverIncongnito);
		drivers.put("edge", edgeDriver);
		drivers.put("ie", ieDriver);
			
		
	}
	
}
