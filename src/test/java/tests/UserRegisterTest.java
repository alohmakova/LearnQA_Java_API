package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

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
  @Test
  public void testCreateUserSuccessfully() {
    String email = DataGenerator.getRandomEmail();
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

    Assertions.assertResponseCodeEquals(responseCreateAuth, 200 );
    Assertions.assertJsonHasField(responseCreateAuth, "id");
  }
}