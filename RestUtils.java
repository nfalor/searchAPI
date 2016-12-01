package Utils;
import com.jayway.restassured.http.ContentType;
import static com.jayway.restassured.RestAssured.*;
import com.jayway.restassured.RestAssured;

public class RestUtils {

    //Global Setup Variables
	
	//Rest request path
    public static String path; 
 
    //Sets Base URI (before test)
    public static void setBaseURI (String baseURI){
        RestAssured.baseURI = baseURI;
    }
 
    //Sets base path (before test)
    public static void setBasePath(String basePathTerm){
        RestAssured.basePath = basePathTerm;
    }
 
    //Reset Base URI (after test)
    public static void resetBaseURI (){
        RestAssured.baseURI = null;
    }
 
    //Reset base path (after test)
    public static void resetBasePath(){
        RestAssured.basePath = null;
    }
 
    ///Sets ContentType (set content type as JSON before start)
    public static void setContentType (ContentType Type){
        given().contentType(Type);
    }
}
