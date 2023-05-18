package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.http.Header;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
  @Step("Make a GET-request with token and auth cookie")
  public Response makeGETRequest(String url, String token, String cookie) {
    return given()
            .filter(new AllureRestAssured())
            .header(new Header("x-csrf-token", token))
            .cookie("auth_sid", cookie)
            .get(url)
            .andReturn();
  }
  @Step("Make a GET-request with auth cookie only")
  public Response makeGETRequestWithCookie(String url, String cookie) {
    return given()
            .filter(new AllureRestAssured())
            .cookie("auth_sid", cookie)
            .get(url)
            .andReturn();
  }
  @Step("Make a GET-request with token only")
  public Response makeGETRequestWithToken(String url, String token) {
    return given()
            .filter(new AllureRestAssured())
            .header(new Header("x-csrf-token", token))
            .get(url)
            .andReturn();
  }
  @Step("Make a POST-request via Response")
  public Response makePostRequest(String url, Map<String, String> authData) {
    return given()
            .filter(new AllureRestAssured())
            .body(authData)
            .post(url)
            .andReturn();
  }
  @Step("Make a POST-request via JsonPath")
  public JsonPath makePostRequestJsonPath(String url, Map<String, String> authData) {
    return given()
            .filter(new AllureRestAssured())
            .body(authData)
            .post(url)
            .jsonPath();
  }

  @Step("Make a PUT-request with token and auth cookie")
  public Response makePutRequest(String url, String token, String cookie, Map<String, String> editData) {
    return given()
            .filter(new AllureRestAssured())
            .header(new Header("x-csrf-token", token))
            .cookie("auth_sid", cookie)
            .body(editData)
            .put(url)
            .andReturn();
  }

}

