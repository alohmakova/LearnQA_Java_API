package tests;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Edit user cases")
@Feature("Edition")
public class UserEditTest extends BaseTestCase {
  private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
  String cookie;
  String header;

  @Test
  @TmsLink("test-1")
  @Description("This test successfully edits user")
  @DisplayName("Test positive edit user: замена на новое имя")
  public void testEditJustCreatedUser() {
    //GENERATE USER
    Map<String, String> userData = DataGenerator.getRegistrationData();

    JsonPath responseCreateAuth = apiCoreRequests
            .makePostRequestJsonPath("https://playground.learnqa.ru/api/user/", userData);

    String userId = responseCreateAuth.getString("id");

    //LOGIN
    Map<String, String> authData = new HashMap<>();
    authData.put("email", userData.get("email"));
    authData.put("password", userData.get("password"));

    Response responseGetAuth = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

    //EDIT
    String newName = "Changed Name";
    Map<String, String> editData = new HashMap<>();
    editData.put("firstName", newName);
    this.cookie = this.getCookie(responseGetAuth, "auth_sid");
    this.header = this.getHeader(responseGetAuth, "x-csrf-token");

    Response responseEditUser = apiCoreRequests
            .makePutRequest(
                    "https://playground.learnqa.ru/api/user/" + userId,
                    this.header,
                    this.cookie,
                    editData);

//GET
    Response responseUserData = apiCoreRequests
            .makeGETRequest(
                    "https://playground.learnqa.ru/api/user/" + userId,
                    this.header,
                    this.cookie
            );

    Assertions.assertJsonByName(responseUserData, "firstName", newName);
  }

  @Test
  @TmsLink("test-2")
  @Description("This test checks status of user edition with name = 1 symbol")
  @DisplayName("Test negative edit user: замена на имя из 1 буквы")
  public void testEditChangeToShortName() {
    //GENERATE USER
    Map<String, String> userData = DataGenerator.getRegistrationData();
    String oldName = userData.get("firstName");

    JsonPath responseCreateAuth = apiCoreRequests
            .makePostRequestJsonPath("https://playground.learnqa.ru/api/user/", userData);


    String userId = responseCreateAuth.getString("id");

    //LOGIN
    Map<String, String> authData = new HashMap<>();
    authData.put("email", userData.get("email"));
    authData.put("password", userData.get("password"));

    Response responseGetAuth = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);


    //EDIT
    String newName = "D";
    Map<String, String> editData = new HashMap<>();
    editData.put("firstName", newName);
    this.cookie = this.getCookie(responseGetAuth, "auth_sid");
    this.header = this.getHeader(responseGetAuth, "x-csrf-token");

    Response responseEditUser = apiCoreRequests
            .makePutRequest(
                    "https://playground.learnqa.ru/api/user/" + userId,
                    this.header,
                    this.cookie,
                    editData);

    Assertions.assertJsonByName(responseEditUser, "error", "Too short value for field firstName");


//GET

    Response responseUserData = apiCoreRequests
            .makeGETRequest(
                    "https://playground.learnqa.ru/api/user/" + userId,
                    this.header,
                    this.cookie
            );

    Assertions.assertJsonByName(responseUserData, "firstName", oldName);
  }

  @Test
  @TmsLink("test-3")
  @Description("This test checks status of user edition with email w/o @ symbol")
  @DisplayName("Test negative edit user: замена на емеил без собаки")
  public void testEditWithoutAtInEmail() {
    //GENERATE USER
    Map<String, String> userData = DataGenerator.getRegistrationData();
    String oldEmail = userData.get("email");

    JsonPath responseCreateAuth = apiCoreRequests
            .makePostRequestJsonPath("https://playground.learnqa.ru/api/user/", userData);


    String userId = responseCreateAuth.getString("id");

    //LOGIN
    Map<String, String> authData = new HashMap<>();
    authData.put("email", userData.get("email"));
    authData.put("password", userData.get("password"));

    Response responseGetAuth = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);


    //EDIT
    String newEmail = "emailexampel.com";
    Map<String, String> editData = new HashMap<>();
    editData.put("firstName", newEmail);
    this.cookie = this.getCookie(responseGetAuth, "auth_sid");
    this.header = this.getHeader(responseGetAuth, "x-csrf-token");

    Response responseEditUser = apiCoreRequests
            .makePutRequest(
                    "https://playground.learnqa.ru/api/user/" + userId,
                    this.header,
                    this.cookie,
                    editData);


//GET

    Response responseUserData = apiCoreRequests
            .makeGETRequest(
                    "https://playground.learnqa.ru/api/user/" + userId,
                    this.header,
                    this.cookie
            );

    Assertions.assertJsonByName(responseUserData, "email", oldEmail);
  }

  @Test
  @TmsLink("test-4")
  @Description("This test checks status of user edition by other user")
  @DisplayName("Test negative edit user: редактирование другим пользователем")
  public void testEditOneUserByOtherUser() {
    //GENERATE USER (1)
    Map<String, String> userData = DataGenerator.getRegistrationData();
    String oldEmail = userData.get("email");

    JsonPath responseCreateAuth = apiCoreRequests
            .makePostRequestJsonPath("https://playground.learnqa.ru/api/user/", userData);

    String userId = responseCreateAuth.getString("id");

    //LOGIN OTHER USER (2)
    Map<String, String> authData2 = new HashMap<>();
    authData2.put("email", "vinkotov@example.com");
    authData2.put("password", "1234");

    Response responseGetAuth2 = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/login", authData2);


    //EDIT GENERATED USER (1) BY LOGGED USER (2): TRY TO CHANGE EMAIL
    String newEmail = "email@exampel.com";
    Map<String, String> editData = new HashMap<>();
    editData.put("email", newEmail);
    this.cookie = this.getCookie(responseGetAuth2, "auth_sid");
    this.header = this.getHeader(responseGetAuth2, "x-csrf-token");

    Response responseEditUser = apiCoreRequests
            .makePutRequest(
                    "https://playground.learnqa.ru/api/user/" + userId, //использую id пользователя (1)
                    this.header, //хедер залогиненного пользователя (2)
                    this.cookie, //куки залогиненного пользователя (2)
                    editData);

    Assertions.assertResponseTextEquals(responseEditUser, "Please, do not edit test users with ID 1, 2, 3, 4 or 5.");

    //LOGIN GENERATED USER (1) TO CHECK THAT HE WAS NOT EDITED
    Map<String, String> authData1 = new HashMap<>();
    authData1.put("email", userData.get("email"));
    authData1.put("password", userData.get("password"));


    Response responseGetAuth1 = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/login", authData1);

    this.cookie = this.getCookie(responseGetAuth1, "auth_sid");
    this.header = this.getHeader(responseGetAuth1, "x-csrf-token");

    //GET DATA OF GENERATED  USER (1) TO CHECK THAT HE WAS NOT EDITED (USER STILL HAS OLD EMAIL)
    Response responseUserData = apiCoreRequests
            .makeGETRequest(
                    "https://playground.learnqa.ru/api/user/" + userId,
                    this.header,
                    this.cookie
            );

    Assertions.assertJsonByName(responseUserData, "email", oldEmail);
  }

  @Test
  @TmsLink("test-5")
  @Description("This test checks status of user edition w/o authorisation")
  @DisplayName("Test negative edit user: редактирование неавторизованного пользователя")
  public void testEditNotAuthorisedUser() {
    //GENERATE USER
    Map<String, String> userData = DataGenerator.getRegistrationData();
    String oldName = userData.get("firstName");

    JsonPath responseCreateAuth = apiCoreRequests
            .makePostRequestJsonPath("https://playground.learnqa.ru/api/user/", userData);

    String userId = responseCreateAuth.getString("id");


    //EDIT
    String newName = "Dan";
    Map<String, String> editData = new HashMap<>();
    editData.put("firstName", newName);

    Response responseEditUser = apiCoreRequests
            .makePutRequest(
                    "https://playground.learnqa.ru/api/user/" + userId,
                    this.header,
                    this.cookie,
                    editData);

    Assertions.assertResponseTextEquals(responseEditUser, "Auth token not supplied");


//LOGIN GENERATED USER TO CHECK THAT HE WAS NOT EDITED
    Map<String, String> authData = new HashMap<>();
    authData.put("email", userData.get("email"));
    authData.put("password", userData.get("password"));


    Response responseGetAuth1 = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

    this.cookie = this.getCookie(responseGetAuth1, "auth_sid");
    this.header = this.getHeader(responseGetAuth1, "x-csrf-token");

    //GET DATA OF GENERATED  USER TO CHECK THAT HE WAS NOT EDITED (USER STILL HAS OLD NAME)

    Response responseUserData = apiCoreRequests
            .makeGETRequest(
                    "https://playground.learnqa.ru/api/user/" + userId,
                    this.header,
                    this.cookie
            );

    Assertions.assertJsonByName(responseUserData, "firstName", oldName);
  }

}
