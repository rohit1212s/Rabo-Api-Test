package com.rabobank.testcase;

import java.lang.reflect.Method;
import java.util.HashMap;
import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.rabobank.base.Base;
import com.rabobank.pojo.CreateEmployee;
import com.rabobank.utilities.ExcelReader;

public class EmployeeApiEndpoints extends Base {

	Base base = new Base();
	ExcelReader reader = new ExcelReader("./src/main/resources/emp-data.xlsx");
	HashMap<String, String> headers;
	static io.restassured.response.Response response;
	CreateEmployee emp = new CreateEmployee();
	String id;
	
	@Test(enabled = true, priority = 1)
	public void createemp(Method method) throws JSONException {
		logger = report.createTest(method.getName());
		for(int currentRow=2;currentRow<=reader.getRowCount("emp-data");currentRow++) {
		logger.info("Hitting Post call");                     //To get log in TestNGreport
		response=base.makePostCall(currentRow);
		logger.info("Getting response for Post call");        //To get log in TestNGreport
		logger.info("Response Code generated is: " + response.getStatusCode());
		Assert.assertEquals(response.getStatusCode(),RESPONSE_STATUS_CODE_200);
		String id = response.then().extract().path("id").toString();	 
		reader.setCellData("emp-data", "id", currentRow, id); //Writing id value to excel sheet
		Log.info("Id generated is: " + id);           		  //To display information on console and log file
		logger.info("Id generated is: " + id);                //To get log in TestNGreport
		Log.info("Post request response JSON: " + response.getBody().asString());
		logger.pass("Post call successful and Test case Passed");
		}
	}
	
	@Test(enabled = true, priority = 2)
	public void fetchempDetails(Method method) throws JSONException {
		logger = report.createTest(method.getName());
		for(int currentRow=2;currentRow<=reader.getRowCount("emp-data");currentRow++) {
		id= reader.getCellData("emp-data", "id", currentRow);   //fetching id value from excel sheet
		logger.info("The emp id to be fetched is :"+ id);       //To get log in TestNGreport
		response=base.makeGetCall(id);
		logger.info("Response Code generated is: " + response.getStatusCode());
		Assert.assertEquals(response.getStatusCode(),RESPONSE_STATUS_CODE_200);;
		logger.info("Get call successful");   //To get log in TestNGreport
		Log.info("Get call successful");      //To display information on console and log file
		logger.info("Actual id: " + (response.then().extract().path("id").toString()) + " & "+ "Expected id: " + id);
		Assert.assertEquals(response.then().extract().path("id").toString(),id);
		logger.info("Actual name: " + (response.then().extract().path("name").toString()) + " & "+ "Expected name: " + reader.getCellData("emp-data", "name", currentRow));
		Assert.assertEquals(response.then().extract().path("name"),reader.getCellData("emp-data", "name", currentRow));
		logger.info("Actual status: " + (response.then().extract().path("status").toString()) + " & "+ "Expected status: " + reader.getCellData("emp-data", "status", currentRow));		
		Assert.assertEquals(response.then().extract().path("status"),reader.getCellData("emp-data", "status", currentRow));
;
		logger.pass("Test case passed");       //To get log in TestNGreport
		}
	}

	@Test(enabled = true, priority = 3)
	public void updateemp(Method method) throws JSONException {
		logger = report.createTest(method.getName());
		for(int currentRow=2;currentRow<=reader.getRowCount("emp-data");currentRow++) {
		id= reader.getCellData("emp-data", "id", currentRow);  //fetching id value from excel sheet
		logger.info("The emp id for updation is :"+ id);       //To get log in TestNGreport
		response=base.makePutCall(id,currentRow);
		logger.info("Response Code generated is: " + response.getStatusCode());
		Assert.assertEquals(response.getStatusCode(),RESPONSE_STATUS_CODE_200);
		logger.info("Actual id: " + (response.then().extract().path("id").toString()) + " & "+ "Expected id: " + id);
		Assert.assertEquals(response.then().extract().path("id").toString(),id);
		Assert.assertEquals(response.then().extract().path("name"),reader.getCellData("emp-data", "updated_name", currentRow));
		logger.info("Updated Name: " + (response.then().extract().path("name").toString()) + " & "+ "Previous Name: " + reader.getCellData("emp-data", "name", currentRow));
		Assert.assertEquals(response.then().extract().path("status"),reader.getCellData("emp-data", "updated_status", currentRow));
		logger.info("Updated Status: " + (response.then().extract().path("status").toString()) + " & "+ "Previous status: " + reader.getCellData("emp-data", "status", currentRow));
		logger.info("Put call successful");   //To get log in TestNGreport
		Log.info("Put call successful");      //To display information on console and log file
		logger.pass("Test case passed");      //To get log in TestNGreport
		id=null;
		}
	}

	@Test(enabled = true, priority = 4)
	public void deleteemp(Method method) throws JSONException {
		logger = report.createTest(method.getName());
		for(int currentRow=2;currentRow<=reader.getRowCount("emp-data");currentRow++) {
		id= reader.getCellData("emp-data", "id", currentRow); //fetching id value from excel sheet
		logger.info("The emp id to be deleted is :"+ id);     //To get log in TestNGreport
		response=base.makeDeleteCall(id);
		logger.info("Response Code generated is: " + response.getStatusCode());
		Assert.assertEquals(response.getStatusCode(),RESPONSE_STATUS_CODE_200);
		logger.info("Actual message: " + (response.then().extract().path("message")) + " & "+ "Expected message: " + id);
		Assert.assertEquals(response.then().extract().path("message"), id);
		logger.info("Delete call successful");   //To get log in TestNGreport
		Log.info("Delete call successful");      //To display information on console and log file
		response=base.makeGetCall(id);
		logger.info("Get call to validate that the id is deleted");   //To get log in TestNGreport
		Assert.assertEquals(response.getStatusCode(),RESPONSE_STATUS_CODE_404);
		logger.info("Actual message: " + (response.then().extract().path("message")) + " & "+ "Expected mssage: " + "emp not found");
		Assert.assertEquals(response.then().extract().path("message"),"emp not found");
		logger.info("Id deleted successfuly");
		logger.pass("Test case passed");         //To get log in TestNGreport
		}
	}


}
