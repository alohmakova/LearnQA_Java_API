import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class Ex8TokenTest {
  @Test
  public void testEx8Token() throws InterruptedException {

    JsonPath response = RestAssured
            .get("https://playground.learnqa.ru/ajax/api/longtime_job")
            .jsonPath();

    String token = response.get("token");
    int sec = response.get("seconds");
    int mlsec = sec * 1000;

    JsonPath responseStatus = RestAssured
            .given()
            .queryParam("token", token)
            .get("https://playground.learnqa.ru/ajax/api/longtime_job")
            .jsonPath();

    String statusBefore = responseStatus.get("status");
    assertEquals("Job is NOT ready", statusBefore);
    System.out.println("\nЗапрос ДО того, как задача готова, имеет статус: " + statusBefore);


    Thread.sleep(mlsec);


    JsonPath responseResult = RestAssured
            .given()
            .queryParam("token", token)
            .get("https://playground.learnqa.ru/ajax/api/longtime_job")
            .jsonPath();

    String statusAfter = responseResult.get("status");
    assertEquals("Job is ready", statusAfter);
    System.out.println("\nЗапрос ПОСЛЕ того, как задача готова, имеет статус: " + statusAfter);

    String result = responseResult.get("result");
    assertNotNull(result, "Поле 'result' отсутствует");
    System.out.println("\nВыполнение задачи заняло " + result + " секунд(ы)");

  }

}
