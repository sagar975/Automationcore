package com.testproject.common;

import java.lang.reflect.Method;
import java.util.Iterator;

import org.testng.annotations.DataProvider;

import com.testproject.core.automation.AbstractSeleniumTest;
import com.testproject.core.dataops.CSVDataProviderUtil;

public class TestProjectAbstractSeleniumTest implements AbstractSeleniumTest {

	@DataProvider(name = "CSVDataProvider")
	public Iterator<Object[]> getDataProvider(Method method) {
		// TODO Auto-generated method stub
		return CSVDataProviderUtil.geCSVIterator(method, '|');
	}

}
