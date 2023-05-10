package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {
  public static void assertJsonByName(Response response, String name, int expectedValue){
    response.then().assertThat().body("$", hasKey(name));

    int value = response.jsonPath().getInt(name);

    assertEquals(expectedValue, value, "JSON value is not equal to expected value");
  }
  public static void assertUserAgent(Response response, String keyName, String expectedValue){
    response.then().assertThat().body("$", hasKey(keyName));

    String value = response.jsonPath().get(keyName);

    assertEquals(expectedValue, value, "JSON value is not equal to expected value");
  }
  public static void assertResponseTextEquals(Response response, String expectedAnswer){
    assertEquals(expectedAnswer, response.asString(), "Response text is not as expected");
  }
  public static void assertResponseCodeEquals(Response response, int expectedCode){
    assertEquals(expectedCode, response.getStatusCode(), "Response status code is not as expected");
  }
}
