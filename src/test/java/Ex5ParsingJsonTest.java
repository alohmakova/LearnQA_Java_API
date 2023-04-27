import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class Ex5ParsingJsonTest {
  @Test
  public void testEx5ParsingJson(){

    JsonPath response = RestAssured
            .given()
            //.queryParams(params)
            .get("https://playground.learnqa.ru/api/get_json_homework")
            .jsonPath();

   ArrayList<Object> messages = response.get("messages");
    if (messages != null) {
      System.out.println("\nПолученный JSON " + messages);
    } else {
      System.out.println("There is no messages");
    }

    String secondMessage = response.getString("messages[1].message");
    System.out.println("\nТекст второго сообщения: " + secondMessage);

  }


}
