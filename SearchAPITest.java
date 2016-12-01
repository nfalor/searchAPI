//@author: nfalor

package com.apple.SearchAPI;
import org.apache.commons.logging.Log;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import static com.jayway.restassured.RestAssured.*;
import java.util.List;
import Utils.*;

public class SearchAPITest {
	
	@BeforeClass
    public void setup (){
        //Test Setup
        RestUtils.setBaseURI("https://itunes.apple.com"); //Setup Base URI
        RestUtils.setBasePath("/search"); //Setup Base Path
        RestUtils.setContentType(ContentType.JSON); //Setup Content Type
    }
	
	//DataProvider to store test data for validating the status code
	@DataProvider(name="searchDataValidateStatusCode")
	public Object[][] dataProviderStatusCode (){
		return new Object[][] {
				{ "jack johnson", "US", "all", 50, 200 },//testing default value for all optional fields
				{ "jack johnson", "DO", "software", 100, 200 },//all valid params
				{ "", "US", "shortFilm", 150, 200 },//mandatory value missing
				{ "jack johnson", "", "ebook", 150, 200 },//country misssing
				{ "", "PPP", "tvShow", 150, 400},//optional value invalid
		};
	}
	
	@Test(dataProvider = "searchDataValidateStatusCode")
	public void searchApiTestStatusCode(String term, String country, String media, int limit, int statusCode) {  
		Response resp = 
	    given().
	        params("term",term).
	        params("country",country).
	        params("media",media).
	        params("limit",limit). 
	    when().
	        get("RestAssured.baseURI + RestAssured.basePath");
		Assert.assertEquals(resp.getStatusCode(), statusCode);

	}
	
	//DataProvider to store test data for validating the count
	@DataProvider(name="searchDataValidateResultCount")
	public Object[][] dataProviderResultCount (){
		return new Object[][] {
				{ "jack", "US", "all", 50, 50 },//default value for all optional fields
				{ "jack johnson", "US", "ebook", 150, 57 },//all valid params
				{ "", "IN", "podcast", 150, 0},//mandatory value missing returns 0 count in result
				{ "jack johnson", "", "music", 150, 150 },//optional value missing
		};
	}
	
	@Test(dataProvider = "searchDataValidateResultCount")
	public void searchApiTestResultCount(String term, String country, String media, int limit, int count) {  
		Response resp = 
	    given().
	        params("term",term).
	        params("country",country).
	        params("media",media).
	        params("limit",limit). 
	    when().
	        get("RestAssured.baseURI + RestAssured.basePath");
		List<String> results = resp.jsonPath().getList("results");
		int resultCount = results.size();
		Assert.assertEquals(resultCount, count);
	}
			
	//DataProvider to store test data for validating the error message
	@DataProvider(name="searchDataValidateErrorMessages")
	public Object[][] dataProviderErrorMesssages (){
		return new Object[][] {
				{ "jack johnson", "US", "ebook", 150 , null},//all valid params, no error message
				{ "jack johnson", "PPP", "podcast", 150, "Invalid value(s) for key(s): [country]"},//invalid country
				{ "jack johnson", "US", "pencil", 150, "Invalid value(s) for key(s): [mediaType]"},//invalid media
		};
	}
		
	@Test(dataProvider = "searchDataValidateErrorMessages")
	public void searchApiTestErrorMessage(String term, String country, String media, int limit, String errorMessage) {  
		Response resp = 
	    given().
	        params("term",term).
	        params("country",country).
	        params("media",media).
	        params("limit",limit). 
	    when().
	        get("RestAssured.baseURI + RestAssured.basePath");
		String errorMsg = resp.jsonPath().get("errorMessage");
		Assert.assertEquals(errorMsg, errorMessage);
	}
	
	@AfterClass
    public void tearDown (){
        //Test Setup
        RestUtils.resetBaseURI(); //Reset Base URI
        RestUtils.resetBasePath(); //Reset Base Path
    }
}
