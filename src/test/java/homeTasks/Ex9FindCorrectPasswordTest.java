package homeTasks;

import io.qameta.allure.Flaky;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Ex9FindCorrectPasswordTest {

  public List<String> readPasswords(String filePath) throws IOException {

    List<String> passwords = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = br.readLine()) != null) {
        passwords.add(line);
      }
    }
    return passwords;
  }

  @Test
  @Flaky
  public void password() throws IOException {

    List<String> passwords = readPasswords("passwords.txt");
    for (String password : passwords) {
      Map<String, String> credentials = new HashMap<>();
      credentials.put("login", "super_admin");
      credentials.put("password", password);


      Response response = RestAssured
              .given()
              .body(credentials)
              .when()
              .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
              .andReturn();

      String responseCookie = response.getCookie("auth_cookie");


      Map<String, String> cookies = new HashMap<>();
      if (responseCookie != null) {
        cookies.put("auth_cookie", responseCookie);

      }


      Response responseForCheck = RestAssured
              .given()
              .body(credentials)
              .cookies(cookies)
              .when()
              .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
              .andReturn();

      String findPass = responseForCheck.asString();
      //responseForCheck.print();

      if (findPass.equals("You are authorized")) {
        System.out.println(findPass);
        System.out.println(password);
        break;
      }

    }
  }
}