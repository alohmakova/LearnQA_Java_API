package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static lib.Assertions.assertResponseCodeEquals;
import static lib.Assertions.assertResponseTextEquals;

public class UserRegisterTest extends BaseTestCase {
  @Test
  public void testCreateUserWithExistingEmail() {
    String email = "vinkotov@example.com";
    Map<String, String> userData = new HashMap<>();
    userData.put("email", email);
    userData.put("password", "123");
    userData.put("username", "learnqa");
    userData.put("firstName", "learnqa");
    userData.put("lastName", "learnqa");

    Response responseCreateAuth = RestAssured
            .given()
            .body(userData)
            .post("https://playground.learnqa.ru/api/user/")
            .andReturn();

    System.out.println(responseCreateAuth.asString());
    System.out.println(responseCreateAuth.statusCode());

    Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    Assertions.assertResponseCodeEquals(responseCreateAuth, 400 );
  }
}