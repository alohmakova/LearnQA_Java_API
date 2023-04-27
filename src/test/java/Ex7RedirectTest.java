import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Ex7RedirectTest {
  @Test
  public void testEx7LongRedirect() {

    Response response;
    int statusCode = 0;
    String locationHeader = "https://playground.learnqa.ru/api/long_redirect";

    while (statusCode != 200) {
      response = RestAssured
              .given()
              .redirects()
              .follow(false)
              .when()
              .get(locationHeader)
              .andReturn();

      statusCode = response.getStatusCode();
      System.out.println("\n" + statusCode);
      locationHeader = response.getHeader("Location");
      System.out.println(locationHeader);
    }
  }
}