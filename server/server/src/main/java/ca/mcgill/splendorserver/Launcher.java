package ca.mcgill.splendorserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Temporary launcher to test maven setup.
 */
@SpringBootApplication(scanBasePackages = {"ca.mcgill.splendorserver"})
public class Launcher {

  public static void main(String[] args) {

    SpringApplication.run(Launcher.class, args);
  }
}
