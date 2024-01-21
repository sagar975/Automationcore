package com.testproject.run;

import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;

public class RunTestNG {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TestNG runner = new TestNG();

		List<String> suiteFiles = new ArrayList<>();

		// add xml file to execute

		suiteFiles.add("testng.xml");

		runner.setTestSuites(suiteFiles);
		runner.run();

	}

}
