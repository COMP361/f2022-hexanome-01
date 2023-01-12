package ca.mcgill.splendorserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Temporary launcher to test maven setup.
 */
@SpringBootApplication(scanBasePackages = {"ca.mcgill.splendorserver"})
public class Launcher {
  
  /**
   * Sole constructor.  (For invocation by subclass constructors, typically
   * implicit.)
   */
  public Launcher() {
    
  }

  /**
   * Spring launcher.
   *
   * @param args launch arguments
   */
  public static void main(String[] args) {

    SpringApplication.run(Launcher.class, args);
  }
}
