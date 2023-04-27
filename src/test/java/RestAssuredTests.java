import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

  @Test
  public void testGetCheckType(){

    Response response = RestAssured
            .given()
            .queryParam("param1", "value1")
            .queryParam("param2", "value2")
            .get(" https://playground.learnqa.ru/api/check_type")
            .andReturn();
    response.print();
  }

  @Test
  public void testPostCheckType(){
    Map<String, Object> body= new HashMap<>();
    body.put("param1", "value1");
    body.put("param2", "value2");

    Response response = RestAssured
            .given()
            .body(body)
            //.body("{\"param1\":\"value1\",\"param2\":\"value2\"}")
            //.body("param1=value1&param2=value2")
            .post(" https://playground.learnqa.ru/api/check_type")
            .andReturn();
    response.print();
  }
}
