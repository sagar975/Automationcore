package com.testproject.core.automation;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.testproject.core.listener.WebDriverListener;

import com.testproject.core.constants.*;

public class SeleniumWebDriverFactory {

	private static final Logger logger = Logger.getLogger(SeleniumWebDriverFactory.class);

	private SeleniumWebDriverFactory() {

	}

	public static class WebDriverOptions {

		public String hubUrl;
		public String browser;
		public String platform;
		public String downloadDir;
		public String httpProxy;
		public String sslProxy;
		public String noProxy;

	}

	public static WebDriver createInstance() throws MalformedURLException {

		return createInstance(null);
	}

	public static WebDriver createInstance(WebDriverOptions webDriverOptions) throws MalformedURLException {
		webDriverOptions = loadWebDriverOptions(webDriverOptions);
		WebDriver driver = null;
		String NODE = webDriverOptions.hubUrl;
		String BROWSER = webDriverOptions.browser;
		String platform = webDriverOptions.platform;

		if (platform != null) {
			platform = platform.toUpperCase();
		}

		Platform PLATFORM = Platform.WINDOWS;
		boolean noGrid = false;

		// GRID URL SETUP

		if (NODE == null || NODE.equalsIgnoreCase("")) {
			logger.info("Running in local machine hubURL is not Specified");
			noGrid = true;
		}

		if (BROWSER == null || BROWSER.equalsIgnoreCase("")) {
			BROWSER = "Chrome";
		}

		// PLATFORM SETUP

		if (platform == null || platform.equalsIgnoreCase("")) {
			PLATFORM = Platform.WINDOWS;
		} else if (platform.contentEquals("LINUX")) {
			PLATFORM = Platform.LINUX;

		} else if (platform.contentEquals("MAC")) {
			PLATFORM = Platform.MAC;
		}

		// Define browser option

		InternetExplorerOptions ieOptions = new InternetExplorerOptions();
		ChromeOptions chromeOptions = new ChromeOptions();
		FirefoxOptions ffOptions = new FirefoxOptions();

		// set proxy info if specified as system properties

		Proxy proxy = new Proxy();
		boolean proxyValueSet = false;

		String httpProxy = webDriverOptions.httpProxy;
		if (httpProxy != null && !httpProxy.isBlank()) {
			proxyValueSet = true;
			proxy.setHttpProxy(httpProxy);
			logger.info("set http proxy - > ");

		}

		String sslProxy = webDriverOptions.sslProxy;

		if (sslProxy != null && !sslProxy.isBlank()) {
			proxyValueSet = true;
			proxy.setSslProxy(sslProxy);
			logger.info("set ssl  proxy - > ");
		}
		String noProxy = webDriverOptions.noProxy;

		if (noProxy != null && noProxy.isBlank()) {
			proxyValueSet = true;
			proxy.setNoProxy(noProxy);
		}

		// Set the download folder if specified by system property
		String downloadDir = webDriverOptions.downloadDir;

		Map<String, Object> prefs = new HashMap<String, Object>();

		if (downloadDir != null && !downloadDir.isBlank()) {
			downloadDir = downloadDir.strip();
			File f = new File(downloadDir);
			if (!f.exists()) {
				logger.warn("Directory Does not Exisists -> ");
			}

			// set the download folder if specified by steme property
			prefs.put("download.default directory", downloadDir);
			prefs.put("download.prompt_for_download", false);

			prefs.put("profile.default_content_setting_values.automatic_downloads", 1);
			prefs.put("download.directory_upgrade", true);
			prefs.put("plugins.plugins_disabled", new String[] { "Chrome PDF Viewer" });

			prefs.put("plugins.always_open_pdf_externally", true);

		}

		if (BROWSER.equalsIgnoreCase(Constants.BROWSER_IE)) {
			ieOptions.setCapability("browserName", "internet explorer");
			ieOptions.setPlatformName(PLATFORM.toString());
			ieOptions.ignoreZoomSettings();
			ieOptions.disableNativeEvents();
			ieOptions.setAcceptInsecureCerts(true);
			ieOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);

			if (proxyValueSet) {
				proxy.setAutodetect(false);
				ieOptions.setProxy(proxy);
				logger.info("capability : " + CapabilityType.PROXY + " set");

			}

		}

		else if (BROWSER.equalsIgnoreCase(Constants.BROWSER_FIREFOX)) {

			ffOptions.setCapability("browserName", "firefox");
			ffOptions.setPlatformName(PLATFORM.toString());
			ffOptions.setCapability("marionette", true);
			ffOptions.setAcceptInsecureCerts(true);
			ffOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
			if (proxyValueSet) {
				proxy.setAutodetect(false);
				ffOptions.setProxy(proxy);
				logger.info("capability: " + CapabilityType.PROXY + " set");
			}

			else if (BROWSER.equalsIgnoreCase(Constants.BROWSER_CHROME)) {
				chromeOptions.setCapability("browserName", "chrome");
				chromeOptions.setPlatformName(PLATFORM.toString());
				chromeOptions.setAcceptInsecureCerts(true);
				chromeOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
				chromeOptions.addArguments("--remote-allow-origins=*");

			}

			if (proxyValueSet) {
				proxy.setAutodetect(false);
				chromeOptions.setProxy(proxy);
				logger.info("capability: " + CapabilityType.PROXY + " set");
			}
			chromeOptions.setExperimentalOption("prefs", prefs);

		}

		// Running on local system or grid setup

		if (noGrid) {
			if (BROWSER.equalsIgnoreCase(Constants.BROWSER_IE)) {
				driver = new InternetExplorerDriver(ieOptions);
			} else if (BROWSER.equalsIgnoreCase(Constants.BROWSER_CHROME)) {
				driver = new ChromeDriver(chromeOptions);
			}

			else if (BROWSER.equalsIgnoreCase(Constants.BROWSER_FIREFOX)) {
				driver = new FirefoxDriver(ffOptions);
			}

		}

		else {
			long t1 = System.currentTimeMillis();
			if (BROWSER.equalsIgnoreCase(Constants.BROWSER_IE)) {
				driver = new RemoteWebDriver(new URL(NODE), ieOptions);
			} else if (BROWSER.equalsIgnoreCase(Constants.BROWSER_CHROME)) {
				driver = new RemoteWebDriver(new URL(NODE), chromeOptions);
			}

			else if (BROWSER.equalsIgnoreCase(Constants.BROWSER_FIREFOX)) {
				driver = new RemoteWebDriver(new URL(NODE), ffOptions);

			}

			logger.info("Time taken to create object : " + (System.currentTimeMillis() - t1));
		}

		return driver;

	}

	public static WebDriverOptions loadWebDriverOptions(WebDriverOptions webDriverOptions) {

		if (webDriverOptions == null)
			webDriverOptions = new WebDriverOptions();
		if (webDriverOptions.hubUrl == null)
			webDriverOptions.hubUrl = getSystemProperty("hubUrl");
		if (webDriverOptions.browser == null)
			webDriverOptions.browser = getSystemProperty("browser");
		if (webDriverOptions.platform == null)
			webDriverOptions.platform = getSystemProperty("platform");
		if (webDriverOptions.downloadDir == null)
			webDriverOptions.downloadDir = getSystemProperty("");
		if (webDriverOptions.httpProxy == null)
			webDriverOptions.httpProxy = getSystemProperty(Constants.SYS_PROP_HTTP_PROXY);
		if (webDriverOptions.sslProxy == null)
			webDriverOptions.sslProxy = getSystemProperty(Constants.SYS_PROP_SSL_PROXY);
		if (webDriverOptions.noProxy == null)
			webDriverOptions.noProxy = getSystemProperty(Constants.SYS_PROP_NO_PROXY);

		return webDriverOptions;
	}

	public static String getSystemProperty(String sysProp) {
		String s = System.getProperty(sysProp);
		if (s == null)
			return null;
		else if (s.isBlank())
			return null;
		else
			return s.strip();

	}

}
