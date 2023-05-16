package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

@Epic("Registration cases")
@Feature("Registration")
public class UserRegisterTest extends BaseTestCase {

  private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

  @Test
  @Description("This test checks registration status if email already in use")
  @DisplayName("Test negative register user")
  public void testCreateUserWithExistingEmail() {
    String email = "vinkotov@example.com";
    Map<String, String> userData = new HashMap<>();
    userData.put("email", email);
    userData = DataGenerator.getRegistrationData(userData);

    Response responseCreateAuth = RestAssured
            .given()
            .body(userData)
            .post("https://playground.learnqa.ru/api/user/")
            .andReturn();

    Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
  }

  @Test
  @Description("This test checks successful registration")
  @DisplayName("Test positive register user")
  public void testCreateUserSuccessfully() {
    Map<String, String> userData = DataGenerator.getRegistrationData();

    Response responseCreateAuth = RestAssured
            .given()
            .body(userData)
            .post("https://playground.learnqa.ru/api/user/")
            .andReturn();

    Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
    Assertions.assertJsonHasField(responseCreateAuth, "id");
  }

  @Test
  @Description("This test checks registration status w/o '@' symbol in the email")
  @DisplayName("Test negative register user")
  public void testCreateUserWithoutAtSymbol() {
    String email = "vinkotovexample.com";
    Map<String, String> userData = new HashMap<>();
    userData.put("email", email);
    userData = DataGenerator.getRegistrationData(userData);

    Response responseCreateAuth = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/", userData);


    Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
    Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
  }

  @Test
  @Description("This test checks registration status with short name: 1 symbol")
  @DisplayName("Test negative register user")
  public void testCreateUserWithShortName() {
    String firstName = "V";
    Map<String, String> userData = new HashMap<>();
    userData.put("firstName", firstName);
    userData = DataGenerator.getRegistrationData(userData);

    Response responseCreateAuth = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/", userData);


    Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'firstName' field is too short");
    Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
  }

  @Test
  @Description("This test checks registration status with long name: 251 symbol")
  @DisplayName("Test negative register user")
  public void testCreateUserWithLongName() {
    String firstName = "Vqkaobfanrukjwnyevtuiwuolvnowoqiggayunrxxfqftwzkktiqozxtngrlabhzpvucsfcdlzgxhiebduqutvmhxkledoirggfmqmfbmajcyxriqoihwbphvcdzzmxkaiadznxidmkxwvfhpxtdacozrbpfuhhkcyapkpucoyahykpalcxpqjazsaklqyoptkoiinwetpxzmzvldaqmiwvokbuzostdrmjrjrxjcmfqnuyjjulvuimdrpj";
    Map<String, String> userData = new HashMap<>();
    userData.put("firstName", firstName);
    userData = DataGenerator.getRegistrationData(userData);

    Response responseCreateAuth = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/", userData);


    Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'firstName' field is too long");
    Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
  }


  @Description("This test checks registration status w/o sending one of required fields")
  @DisplayName("Test negative register user")
  @ParameterizedTest
  @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})
  public void testCreateUserWithoutRequiredField(String condition) {

    Map<String, String> userData = new HashMap<>();
    if (condition.equals("email")) {
      userData.put("email", null);
    } else if (condition.equals("password")) {
      userData.put("password", null);
    } else if (condition.equals("username")) {
      userData.put("username", null);
    } else if (condition.equals("firstName")) {
      userData.put("firstName", null);
    } else if (condition.equals("lastName")) {
      userData.put("lastName", null);
    } else {
      throw new IllegalArgumentException("Condition value is known: " + condition);
    }
    userData = DataGenerator.getRegistrationData(userData);


    Response responseCreateAuth = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/", userData);


    Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: " + condition);
    Assertions.assertResponseCodeEquals(responseCreateAuth, 400);

  }

}