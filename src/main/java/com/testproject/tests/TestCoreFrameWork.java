package com.testproject.tests;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.testproject.common.TestProjectAbstractSeleniumTest;
import com.testproject.core.automation.AutomationContextManager;
import com.testproject.core.listener.AutomationListner;
import com.testproject.core.utils.AutomationAssertion;

public class TestCoreFrameWork extends TestProjectAbstractSeleniumTest {

	private static final Logger logger = Logger.getLogger(TestCoreFrameWork.class);

	@Test(enabled = true, description = "this is test project", dataProvider = "CSVDataProvider")
	public void testFrameworkChanges(String tetsName, String LoginName) {

		AutomationContextManager.getDriver().get("https://google.com");

		AutomationAssertion assertion = new AutomationAssertion();
		String actual = AutomationContextManager.getDriver().getTitle();
		String expected = "sagar";
		assertion.assertEquals("test title ", expected, actual, true);
		//assertion.assertAll();

	}

}
