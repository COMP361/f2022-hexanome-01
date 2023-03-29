package ca.mcgill.splendorserver.controllers;

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

import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.saves.SaveSession;

public class SaveManager {
	
	private final static Path saveDir = (Paths.get(System.getProperty("user.home"))).resolve("splendorsaves");
	private final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
	
	public static void init() {
		File dir = new File(saveDir.toString());
		if (!dir.exists()){
			dir.mkdirs();
		}
	}
	
	public static void initPlayer(String playerId) {
		File dir = new File(saveDir.resolve(playerId).toString());
		if (!dir.exists()){
			dir.mkdirs();
		}
	}
	
	public static SaveSession loadGame(String saveId, String playerId) {
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
	 
	        return new SaveSession(game);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String saveGame(Game game) {
		FileOutputStream fileOut;
		try {
			initPlayer(game.getCreatorId());
			LocalDateTime now = LocalDateTime.now();  
	        String saveId = game.getId() + "_" + dtf.format(now);
			fileOut = new FileOutputStream(saveDir.resolve(game.getCreatorId()).resolve(saveId + ".save").toString());
	        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
	        objectOut.writeObject(game);
	        objectOut.close();
			return saveId;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<SaveSession> getAllSavedGames() {
		File saveDirectory = new File(saveDir.toString());
		File[] playerDirectories = saveDirectory.listFiles(File::isDirectory);
		List<SaveSession> savedGames = new ArrayList<>();

		for (File playerDir : playerDirectories) {
			String playerId = playerDir.getName();
			File[] saveFiles = playerDir.listFiles((dir, name) -> name.endsWith(".save"));

			for (File saveFile : saveFiles) {
				try {
					FileInputStream fileIn = new FileInputStream(saveFile);
					ObjectInputStream objectIn = new ObjectInputStream(fileIn);

					Game game = (Game) objectIn.readObject();
					objectIn.close();

					savedGames.add(new SaveSession(game));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return savedGames;
	}


	public static void deleteTestSavefile(String saveId, String playerId) {
		File file = new File(saveDir.resolve(playerId).resolve(saveId + ".save").toString());
		file.delete();
	}

}
