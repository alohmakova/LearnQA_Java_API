package homeTasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShortPhraseTest {
  @Test
  public void testShortPhrase(){
    String string = "тест";
    int stringLength = string.length();
    System.out.println(stringLength);
    assertTrue(stringLength > 15, "Фраза \"" + string + "\" короче или равна 15 символам");
  }
}
