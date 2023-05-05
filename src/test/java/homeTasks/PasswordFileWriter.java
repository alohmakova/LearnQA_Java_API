package homeTasks;

import homeTasks.GetPasswordsFromWiki;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.util.Set;

public class PasswordFileWriter extends GetPasswordsFromWiki {

  @Test
  public void passwordFile() throws Exception {
    Set<String> passwords = getPasswordsList();
    FileWriter writer = new FileWriter("passwords.txt");

    for (String password : passwords) {
      writer.write(password + "\n");
    }

    writer.close();

  }
}