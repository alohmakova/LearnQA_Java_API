import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Ex9FindCorrectPasswordTest extends GetPasswordsTest{


  @Test
  public void password() throws Exception {

    Set<String> passwords = getPasswordsList();
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
      System.out.println(responseCookie);
    }


  }
}
