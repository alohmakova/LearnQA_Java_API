package homeTasks;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class HelloWorldTest {
  @Test
   public void testHelloWorld(){
    System.out.println("Hello from Anastasiya");
  }
  @Test
  public void testGetHello(){
    Response response = RestAssured
            .get(" https://playground.learnqa.ru/api/hello")
            .andReturn();
    response.prettyPrint();
  }
  @Test
  public void testGetText(){
    Response response = RestAssured
            .get(" https://playground.learnqa.ru/api/get_text")
            .andReturn();
    response.prettyPrint();
  }
}
