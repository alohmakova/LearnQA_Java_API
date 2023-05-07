package homeTasks;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HomeworkCookieTest {
  @Test
  public void testHomeworkCookie() {
    Response response = RestAssured
            .get("https://playground.learnqa.ru/api/homework_cookie")
            .andReturn();
    Map<String, String> cookies = response.getCookies();//получаю все возможные куки
    boolean cookieHomeWork = response.getCookies().containsKey("HomeWork");//убеждаюсь, что есть куки с нужным мне названием
    String cookie = response.getCookie("HomeWork");//убеждаюсь, что куки с нужным названием содержит правильное значение
    System.out.println(cookies);
    System.out.println(cookie);

    assertNotNull(cookies, "This method doesn't have cookies");
    assertTrue(cookieHomeWork, "This method doesn't have \"HomeWork\" cookie");
    assertEquals("hw_value", cookie, "Value of 'HomeWork' cookie is not correct");
  }
}
