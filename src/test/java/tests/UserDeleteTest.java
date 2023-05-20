package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
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

@Epic("Delete user cases")
@Feature("Deletion")
@Link(name = "Ex18: Тесты на DELETE", url = "https://software-testing.ru/lms/mod/assign/view.php?id=326423")
public class UserDeleteTest extends BaseTestCase {
  private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
  String cookie;
  String header;
  int userIdOnAuth;

  @Test
  @Description("This test checks status of user with id 2 deletion")
  @DisplayName("Test negative delete user")
  public void testDeleteUser2() {
    //LOGIN
    Map<String, String> authData = new HashMap<>();
    authData.put("email", "vinkotov@example.com");
    authData.put("password", "1234");

    Response responseLogUser = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

    this.cookie = this.getCookie(responseLogUser, "auth_sid");
    this.header = this.getHeader(responseLogUser, "x-csrf-token");
    this.userIdOnAuth = this.getIntFromJson(responseLogUser, "user_id");

    //DELETE USER WITH ID 2
    Response responseDeleteUser = apiCoreRequests
            .makeDeleteRequest("https://playground.learnqa.ru/api/user/" + this.userIdOnAuth,
                    this.header,
                    this.cookie
            );

    Assertions.assertResponseTextEquals(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");

    //GET USER WITH ID 2
    Response responseUserData = apiCoreRequests
            .makeGETRequest(
                    "https://playground.learnqa.ru/api/user/" + this.userIdOnAuth,
                    this.header,
                    this.cookie
            );

    Assertions.assertJsonByName(responseUserData, "id", this.userIdOnAuth);

  }

  @Test
  @Description("This test deletes user successfully")
  @DisplayName("Test positive delete user")
  public void testDeleteUserSuccessfully() {
    //GENERATE USER
    Map<String, String> userData = DataGenerator.getRegistrationData();

    JsonPath responseCreateAuth = apiCoreRequests
            .makePostRequestJsonPath("https://playground.learnqa.ru/api/user/", userData);

    String userId = responseCreateAuth.getString("id");

    //LOGIN
    Map<String, String> authData = new HashMap<>();
    authData.put("email", userData.get("email"));
    authData.put("password", userData.get("password"));

    Response responseLogUser = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

    this.cookie = this.getCookie(responseLogUser, "auth_sid");
    this.header = this.getHeader(responseLogUser, "x-csrf-token");

    //DELETE USER
    Response responseDeleteUser = apiCoreRequests
            .makeDeleteRequest("https://playground.learnqa.ru/api/user/" + userId,
                    this.header,
                    this.cookie
            );

    //GET USER WHO WAS DELETED
    Response responseUserData = apiCoreRequests
            .makeGETRequest(
                    "https://playground.learnqa.ru/api/user/" + userId,
                    this.header,
                    this.cookie
            );

    Assertions.assertResponseTextEquals(responseUserData, "User not found");
  }

  @Test
  @Description("This test checks status of user deletion by other user")
  @DisplayName("Test negative delete user: удаление другим пользователем")
  public void testDeleteOneUserByOtherUser() {
    //GENERATE USER (1)
    Map<String, String> userData = DataGenerator.getRegistrationData();

    JsonPath responseCreateAuth = apiCoreRequests
            .makePostRequestJsonPath("https://playground.learnqa.ru/api/user/", userData);

    String userId = responseCreateAuth.getString("id");

    //LOGIN OTHER USER (2)
    Map<String, String> authData2 = new HashMap<>();
    authData2.put("email", "vinkotov@example.com");
    authData2.put("password", "1234");

    Response responseGetAuth2 = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/login", authData2);

    this.cookie = this.getCookie(responseGetAuth2, "auth_sid");
    this.header = this.getHeader(responseGetAuth2, "x-csrf-token");


    //DELETE GENERATED USER (1) BY LOGGED USER (2)
    Response responseDeleteUser = apiCoreRequests
            .makeDeleteRequest("https://playground.learnqa.ru/api/user/" + userId,//использую id пользователя (1)
                    this.header,//хедер залогиненного пользователя (2)
                    this.cookie //куки залогиненного пользователя (2)
            );

    Assertions.assertResponseTextEquals(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");

    //LOGIN GENERATED USER (1) TO CHECK THAT HE WAS NOT DELETED
    Map<String, String> authData1 = new HashMap<>();
    authData1.put("email", userData.get("email"));
    authData1.put("password", userData.get("password"));


    Response responseGetAuth1 = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/login", authData1);

    this.cookie = this.getCookie(responseGetAuth1, "auth_sid");
    this.header = this.getHeader(responseGetAuth1, "x-csrf-token");

    //GET DATA OF GENERATED  USER (1) TO CHECK THAT HE WAS NOT DELETED
    Response responseUserData = apiCoreRequests
            .makeGETRequest(
                    "https://playground.learnqa.ru/api/user/" + userId,
                    this.header,
                    this.cookie
            );

    Assertions.assertJsonByName(responseUserData, "id", userId);
  }
}
