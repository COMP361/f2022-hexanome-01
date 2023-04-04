package ca.mcgill.splendorserver.controllers;

import ca.mcgill.splendorserver.Registrator;
import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.saves.LobbyServiceSaveData;
import ca.mcgill.splendorserver.models.saves.SaveRegistrator;
import ca.mcgill.splendorserver.models.saves.SaveSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Manages the saving of games.
 */
@Component
public class SaveManager {

  private final Path saveDir =
      (Paths.get(System.getProperty("user.home"))).resolve("splendorsaves");
  private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

  @Autowired
  private SaveRegistrator saveRegistrator;

  /**
   * Constructor that sets folders up.
   */
  public SaveManager() {
    File dir = new File(saveDir.toString());
    if (!dir.exists()) {
      dir.mkdirs();
    }
  }

  /**
   * Runs at server startup.
   *
   * @param playerId the id of the game to save's creator
   */
  public void initPlayer(String playerId) {
    File dir = new File(saveDir.resolve(playerId).toString());
    if (!dir.exists()) {
      dir.mkdirs();
    }
  }

  /**
   * Loads a game from a save.
   *
   * @param saveId the id of the save
   * @param playerId the id of the game creator
   * @return the session created from the save
   */
  public SaveSession loadGame(String saveId, String playerId) {
    File file = new File(saveDir.resolve(playerId).resolve(saveId + ".save").toString());
    if (!file.exists()) {
      return null;
    }
    FileInputStream fileIn;
    try {
      initPlayer(playerId);
      fileIn = new FileInputStream(saveDir.resolve(playerId).resolve(saveId + ".save").toString());
      ObjectInputStream objectIn = new ObjectInputStream(fileIn);

      Game game = (Game) objectIn.readObject();
      objectIn.close();

      if (!game.getCreatorId().equals(playerId)) {
        return null;
      }

      return new SaveSession(game, saveId);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Saves a game.
   *
   * @param game the game to save
   * @return the id of the save, or null if an exception was thrown
   */
  public String saveGame(Game game) {
    FileOutputStream fileOut;
    try {
      initPlayer(game.getCreatorId());
      LocalDateTime now = LocalDateTime.now();
      String saveId = game.getId() + "_" + dtf.format(now);
      fileOut = new FileOutputStream(saveDir.resolve(
          game.getCreatorId()).resolve(saveId + ".save").toString());
      ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
      objectOut.writeObject(game);
      objectOut.close();

      if (saveRegistrator != null) {
        saveRegistrator.registerSavedGameWithLobbyService(game.getVariant(),
          new LobbyServiceSaveData(game, saveId));
      }

      return saveId;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Retrieves all saved games for loading upon registration of the game service with the LS.
   *
   * @return the list of all saves as SaveSessions
   */
  public List<SaveSession> getAllSavedGames() {
    List<SaveSession> savedGames = new ArrayList<>();
    File saveDirectory = new File(saveDir.toString());
    if (saveDirectory.exists()) {
      File[] playerDirectories = saveDirectory.listFiles(File::isDirectory);

      for (File playerDir : playerDirectories) {
        String playerId = playerDir.getName();
        File[] saveFiles = playerDir.listFiles((dir, name) -> name.endsWith(".save"));

        for (File saveFile : saveFiles) {
          try {
            FileInputStream fileIn = new FileInputStream(saveFile);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            Game game = (Game) objectIn.readObject();
            objectIn.close();

            savedGames.add(new SaveSession(game, saveFile.getName().replace(".save", "")));
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
    return savedGames;
  }

  /**
   * Retrieves all saved games of user.
   *
   * @return the number of all saves as SaveSessions
   */
  public int countSavedGamesOfUser(String playerId) {
    File saveDirectory = new File(saveDir.toString());
    
    if (saveDirectory.exists()) {
      File[] playerDirectories = saveDirectory.listFiles(File::isDirectory);

      for (File playerDir : playerDirectories) {
        if (!playerId.equals(playerDir.getName())) {
          continue;
        }
        File[] saveFiles = playerDir.listFiles((dir, name) -> name.endsWith(".save"));
        return saveFiles.length;
      }
    }
    return 0;
  }

  /**
   * Retrieves all saved games of user.
   *
   * @return the number of all saves as SaveSessions
   */
  public void deleteSavedGamesOfUser(String playerId) {
    File saveDirectory = new File(saveDir.toString());
    
    if (saveDirectory.exists()) {
      File[] playerDirectories = saveDirectory.listFiles(File::isDirectory);

      for (File playerDir : playerDirectories) {
        if (!playerId.equals(playerDir.getName())) {
          continue;
        }
        File[] saveFiles = playerDir.listFiles((dir, name) -> name.endsWith(".save"));
        for (File saveFile : saveFiles) {
          saveFile.delete();
        }
        playerDir.delete();
        return;
      }
    }
  }

  /**
   * Deletes the tester save.
   *
   * @param saveId the is of the tester save
   * @param playerId the creator of the game whose save is being deleted
   */
  public void deleteTestSavefile(String saveId, String playerId) {
    File file = new File(saveDir.resolve(playerId).resolve(saveId + ".save").toString());
    if (file.exists()) {
      file.delete();
    }
  }
}
