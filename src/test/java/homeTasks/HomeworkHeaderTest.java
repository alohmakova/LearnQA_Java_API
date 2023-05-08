package homeTasks;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HomeworkHeaderTest {
  @Test
  public void testHomeworkHeader() {
    Response response = RestAssured
            .get("https://playground.learnqa.ru/api/homework_header")
            .andReturn();
   Headers headers = response.getHeaders();//получаю все возможные заголовки
    boolean headerHomeWork = response.getHeaders().hasHeaderWithName("x-secret-homework-header");//убеждаюсь, что есть заголовок с нужным мне названием
    String header = response.getHeader("x-secret-homework-header");//убеждаюсь, что заголовок с нужным названием содержит правильное значение
    System.out.println(header);

    assertNotNull(headers, "This method doesn't have headers");
    assertTrue(headerHomeWork, "This method doesn't have \"x-secret-homework-header\" header");
    assertEquals("Some secret value", header, "Value of 'x-secret-homework-header' header is not correct");
  }
}
