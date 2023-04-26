import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

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
}
