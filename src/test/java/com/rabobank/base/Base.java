package com.rabobank.base;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.apache.log4j.Logger;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.rabobank.pojo.CreateEmployee;
import com.rabobank.utilities.ExcelReader;
import com.rabobank.utilities.TestUtility;

public class Base {

	public int RESPONSE_STATUS_CODE_200 = 200;
	public int RESPONSE_STATUS_CODE_500 = 500;
	public int RESPONSE_STATUS_CODE_400 = 400;
	public int RESPONSE_STATUS_CODE_404 = 404;
	public int RESPONSE_STATUS_CODE_201 = 201;
	public Properties prop;
	public static Logger Log;
	public static ExtentReports report;
	public static ExtentTest logger;
	protected static RequestSpecification requestSpec;
	String endpoint = "/employee";
	ExcelReader reader = new ExcelReader("./src/main/resources/emp-data.xlsx");
	HashMap<String, String> headers;
	static io.restassured.response.Response response;
	CreateEmployee emp = new CreateEmployee();

	public Base() {
		Log = Logger.getLogger(this.getClass());
		try {
			prop = new Properties();
			FileInputStream ip = new FileInputStream(
					System.getProperty("user.dir") + "/src/test/java/config.properties");
			prop.load(ip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@BeforeSuite
	public void setupSuite() {
		TestUtility.setDateForLog4j();
		ExtentHtmlReporter extent = new ExtentHtmlReporter(System.getProperty("user.dir")
				+ "/RaboBankExtentResult/RaboBankExtentReport" + TestUtility.getSystemDate() + ".html");
		report = new ExtentReports();
		report.attachReporter(extent);
	}

	@BeforeTest
	public void setBaseURI() {
		RequestSpecification request = RestAssured.with();
		requestSpec = request.given().contentType(ContentType.JSON).baseUri("localhost:8089/");
	}

	@AfterClass
	public void endReport() {
		report.flush();
	}

	@AfterMethod(alwaysRun = false)
	public void tearDown(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.FAILURE) {
			logger.fail(result.getThrowable().getMessage());
		}
		else if (result.getStatus() == ITestResult.SUCCESS) {
			logger.pass(result.getMethod().getMethodName().concat(" Method Passed"));
		}
		
		else if (result.getStatus() == ITestResult.SKIP) {
			logger.pass(result.getTestName());
		}
		report.flush();

	}

	public Response makePostCall(int currentRow) {
		headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		emp.setName(reader.getCellData("emp-data", "name", currentRow));
		emp.setDepartment(reader.getCellData("emp-data", "status", currentRow));
		response = requestSpec.body(emp).headers(headers).post(endpoint);
		return response;
	}

	public Response makePutCall(String id,int currentRow) {
		headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		emp.setId(id);
		emp.setName(reader.getCellData("emp-data", "updated_name", currentRow));
		emp.setDepartment(reader.getCellData("emp-data", "updated_status", currentRow));
		response = requestSpec.body(emp).headers(headers).put(endpoint);
		return response;
	}

	public Response makeDeleteCall(String id) {
		headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		response = requestSpec.body(emp).headers(headers).delete(endpoint + "/" + id);
		return response;
	}

	public Response makeGetCall(String id) {
		headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		response = requestSpec.headers(headers).get(endpoint + "/" + id);
		return response;
	}

}