package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Authorisation cases")
@Feature("Get user information")
public class UserGetTest extends BaseTestCase {
  private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
  String cookie;
  String header;

  @Test
  @Description("This test checks user information w/o authorization")
  @DisplayName("Test negative user information")
  public void testGetUserDataNotAuth(){
    Response responseUserData = RestAssured
           .get("https://playground.learnqa.ru/api/user/2")
            .andReturn();
    System.out.println(responseUserData.asString());

    Assertions.assertJsonHasField(responseUserData, "username");
    Assertions.assertJsonHasNotField(responseUserData, "firstName");
    Assertions.assertJsonHasNotField(responseUserData, "lastName");
    Assertions.assertJsonHasNotField(responseUserData, "email");
  }

  @Test
  @Description("This test checks user information after authorization")
  @DisplayName("Test positive user information")
  public void testGetUserDetailsAuthAsSameUser(){
    Map<String, String> authData = new HashMap<>();
    authData.put("email", "vinkotov@example.com");
    authData.put("password", "1234");


    Response responseGetAuth = RestAssured
            .given()
            .body(authData)
            .post("https://playground.learnqa.ru/api/user/login")
            .andReturn();
    String header = responseGetAuth.getHeader("x-csrf-token");
    String cookie = responseGetAuth.getCookie("auth_sid");

    Response responseUserData = RestAssured
            .given()
            .header("x-csrf-token", header)
            .cookie("auth_sid", cookie)
            .get("https://playground.learnqa.ru/api/user/2")
            .andReturn();


    System.out.println(responseUserData.asString());

    String[] expectedFieldNames = {"username", "firstName", "lastName", "email"};
    Assertions.assertJsonHasFields(responseUserData, expectedFieldNames);
  }
  @Test
  @Description("This test checks user information from another account")
  @DisplayName("Test negative user information")
  public void testGetUserDetailsAuthAsAnotherUser(){
    Map<String, String> authData = new HashMap<>();
    authData.put("email", "vinkotov@example.com");
    authData.put("password", "1234");


    Response responseGetAuth = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

    this.cookie = this.getCookie(responseGetAuth, "auth_sid");
    this.header = this.getHeader(responseGetAuth, "x-csrf-token");


    Response responseUserData = apiCoreRequests
            .makeGETRequest(
                    "https://playground.learnqa.ru/api/user/187",
                    this.header,
                    this.cookie
            );


    System.out.println(responseUserData.asString());

    Assertions.assertJsonHasField(responseUserData, "username");
    Assertions.assertJsonHasNotField(responseUserData, "firstName");
    Assertions.assertJsonHasNotField(responseUserData, "lastName");
    Assertions.assertJsonHasNotField(responseUserData, "email");
  }

}
