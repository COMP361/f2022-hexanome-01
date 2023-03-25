package ca.mcgill.splendorserver.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import ca.mcgill.splendorserver.models.Game;
import ca.mcgill.splendorserver.models.saves.SaveSession;

public class SaveManager {
	
	private final static Path saveDir = (Paths.get(System.getProperty("user.home"))).resolve("splendorsaves");
	
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
	
	public static SaveSession loadGame(String id, String playerId) {
		FileInputStream fileIn;
		try {
	        initPlayer(playerId);
			fileIn = new FileInputStream(saveDir.resolve(playerId).resolve(id + ".save").toString());
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
	
	public static boolean saveGame(Game game) {
		FileOutputStream fileOut;
		try {
			initPlayer(game.getCreatorId());
			fileOut = new FileOutputStream(saveDir.resolve(game.getCreatorId()).resolve(game.getId() + ".save").toString());
	        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
	        objectOut.writeObject(game);
	        objectOut.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
