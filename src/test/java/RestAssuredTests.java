import io.restassured.RestAssured;
import io.restassured.http.Cookies;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RestAssuredTests {
  @Test
  public void testRestAssured(){
    Map<String, String> params = new HashMap<>();
    params.put("name", "John");


    Response response = RestAssured
            .given()
            .queryParams(params)
            .get(" https://playground.learnqa.ru/api/hello")
            .andReturn();
    response.prettyPrint();
  }

  @Test
  public void testJsonParsing(){
    Map<String, String> params = new HashMap<>();
    params.put("name", "John");


    JsonPath response = RestAssured
            .given()
            .queryParams(params)
            .get(" https://playground.learnqa.ru/api/hello")
            .jsonPath();

    String answer = response.get("answer1");
    if (answer != null) {
      System.out.println(answer);
    } else {
      System.out.println("The key is absent");
    }
  }

  @Test
  public void testGetCheckType(){

    Response response = RestAssured
            .given()
            .queryParam("param1", "value1")
            .queryParam("param2", "value2")
            .get(" https://playground.learnqa.ru/api/check_type")
            .andReturn();
    response.print();
  }

  @Test
  public void testPostCheckType(){
    Map<String, Object> body= new HashMap<>();
    body.put("param1", "value1");
    body.put("param2", "value2");

    Response response = RestAssured
            .given()
            .body(body)
            //.body("{\"param1\":\"value1\",\"param2\":\"value2\"}")
            //.body("param1=value1&param2=value2")
            .post(" https://playground.learnqa.ru/api/check_type")
            .andReturn();
    response.print();
  }

  @Test
  public void testGetStatusCode(){

    Response response = RestAssured
            .given()
            .redirects()
            .follow(false)
            .when()
            .get(" https://playground.learnqa.ru/api/get_303")//get_500//check_type//something//get_303
            .andReturn();
    response.print();
    String locationHeader = response.getHeader("Location");
    int statusCode = response.getStatusCode();
    System.out.println(statusCode);
    System.out.println(locationHeader);
  }

  @Test
  public void testGetHeaders() {
    Map<String, String> headers = new HashMap<>();
    headers.put("myHeader1", "myValue1");
    headers.put("myHeader2", "myValue2");


    Response response = RestAssured
            .given()
            .headers(headers)
            .when()
            .get(" https://playground.learnqa.ru/api/show_all_headers")
            .andReturn();

    response.prettyPrint();


    Headers responseHeaders = response.getHeaders();
    System.out.println(responseHeaders);
  }
  @Test
  public void testGetCookies() {
    Map<String, String> data = new HashMap<>();
    data.put("login", "secret_login");
    data.put("password", "secret_pass");


    Response response = RestAssured
            .given()
            .body(data)
            .when()
            .post(" https://playground.learnqa.ru/api/get_auth_cookie")
            .andReturn();

    System.out.println("\nPretty text: ");
    response.prettyPrint();

    System.out.println("\nHeaders: ");
    Headers responseHeaders = response.getHeaders();
    System.out.println(responseHeaders);

    System.out.println("\nCookies: ");
    Map<String, String> responseCookies = response.getCookies();
    System.out.println(responseCookies);

    String responseCookie = response.getCookie("auth_cookie");
    System.out.println(responseCookie);
  }

  @Test
  public void testCheckCookies() {
    Map<String, String> data = new HashMap<>();
    data.put("login", "secret_login");
    data.put("password", "secret_pass");


    Response responseForGet = RestAssured
            .given()
            .body(data)
            .when()
            .post(" https://playground.learnqa.ru/api/get_auth_cookie")
            .andReturn();


    String responseCookie = responseForGet.getCookie("auth_cookie");
    System.out.println(responseCookie);

    Map<String, String> cookies = new HashMap<>();
    if (responseCookie != null) {
      cookies.put("auth_cookie", responseCookie);
    }


    Response responseForCheck = RestAssured
            .given()
            .body(data)
            .cookies(cookies)
            .when()
            .post(" https://playground.learnqa.ru/api/check_auth_cookie")
            .andReturn();

    responseForCheck.print();
  }
}
