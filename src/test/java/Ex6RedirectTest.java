import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Ex6RedirectTest {
  @Test
  public void testEx6Redirect(){

    Response response = RestAssured
            .given()
            .redirects()
            .follow(false)
            .when()
            .get("https://playground.learnqa.ru/api/long_redirect")
            .andReturn();

    String locationHeader = response.getHeader("Location");
    System.out.println("\n" + locationHeader);
  }
}
