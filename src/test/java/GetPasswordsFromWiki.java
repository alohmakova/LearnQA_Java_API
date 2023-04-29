import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GetPasswordsFromWiki {

  private WebDriver driver;

  @BeforeEach
  public void setUp() throws Exception {
    System.setProperty("webdriver.chrome.driver", "C:/Windows/System32/chromedriver.exe");
    driver = new ChromeDriver();
  }

  public Set<String> getPasswordsList() throws Exception {

      driver.get("https://en.wikipedia.org/wiki/List_of_the_most_common_passwords");
      Set<String> passwords = new HashSet<>();

      for (int i = 2; i <= 26; i++) {
        List<WebElement> elements = driver.findElements(By.xpath("//*[@id=\"mw-content-text\"]/div[1]/table[2]/tbody/tr[" + i + "]"));


        for (WebElement element : elements) {
          for (int j = 2; j <= 10; j++) {
            String pass = element.findElement(By.xpath(".//td[" + j + "]")).getText();
            passwords.add(pass);
          }
        }
      }

    return passwords;
    }

  @AfterEach
  public void tearDown() throws Exception {
    driver.quit();
  }

  }




