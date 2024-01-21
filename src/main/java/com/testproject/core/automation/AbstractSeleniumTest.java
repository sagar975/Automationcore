package com.testproject.core.automation;

import java.lang.reflect.Method;
import java.util.Iterator;

public interface AbstractSeleniumTest {
	
	public Iterator<?> getDataProvider(Method method);

}
