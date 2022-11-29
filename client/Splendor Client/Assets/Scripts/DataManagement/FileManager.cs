using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.IO;
using JetBrains.Annotations;
using System.Text;

public static class FileManager {
    //basic file management classes
    private static void WriteToFile(string fileName, string json) {
        FileStream stream = new FileStream(Application.persistentDataPath + "/" + fileName + ".json", FileMode.Create);
        using (StreamWriter writer = new StreamWriter(stream))
            writer.Write(json);
        stream.Close();
    }

    private static string ReadFromFIle(string fileName) {
        string path = Application.persistentDataPath + "/" + fileName + ".json";
        if (File.Exists(path)) {
            using (StreamReader reader = new StreamReader(path))
                return reader.ReadToEnd();
        }
        else {
            Debug.Log("File not found");
        }

        return "failure";
    }

    //the following are classes to create and read json files/strings. right now filenames are in the format of {ClassName}Data-{identifier}.json
    //we only need to use them as strings, so pass a false to second parameter, but having the file option is useful for debugging purposes
    //location of these files on your machine may vary, for windows its in C:/Users/{profile name}/AppData/LocalLow/Comp 361 2022 Hexanome01/Splendor Client
    //(AppData is a hidden folder, so either enable viewing of hidden folders or in search bar put %appdata% to find the folder
    public static byte[] EncodeSession(Session session, bool desireFileCreation) {
        SessionData data = new SessionData(session);
        string json = JsonUtility.ToJson(data);
        if(desireFileCreation)
            WriteToFile("SessionData-" + session.playerList[0].username, json);
        return Encoding.UTF8.GetBytes(json);
    }

    public static Session DecodeSession(string source, bool isAFile) { //if not a file, pass the json string. if a file, pass filename (same logic applies for all decode methods)
        string json = isAFile ? ReadFromFIle(source) : source;
        SessionData data = new SessionData();
        JsonUtility.FromJsonOverwrite(json, data);
        return new Session(data);
    }

    public static byte[] EncodeGameState(GameData data, bool desireFileCreation) { //maybe instead of passing the data, pass the parameters and create the payload in this method
        //SessionData data = new SessionData(session);
        Debug.Log("before encode :");
        Debug.Log(data.playersInGame);
        string json = JsonUtility.ToJson(data);
        if (desireFileCreation)
            WriteToFile("GameData-" + System.DateTime.Now, json);
        return Encoding.UTF8.GetBytes(json);
    }

    public static GameData DecodeGameState(string source, bool isAFile) {
        string json = isAFile ? ReadFromFIle(source) : source;
        GameData data = new GameData();
        JsonUtility.FromJsonOverwrite(json, data);
        return data;
    }

    public static void LoadSave() {

    }

    public static void CreateSave() {

    }

    public static byte[] EncodeSessionList(SessionListData data, bool desireFileCreation) {
        string json = JsonUtility.ToJson(data);
        if (desireFileCreation)
            WriteToFile("SessionList", json);
        return Encoding.UTF8.GetBytes(json);
    }

    public static SessionListData DecodeSessionListData(string source, bool isAFile) {
        string json = isAFile ? ReadFromFIle(source) : source;
        SessionListData data = new SessionListData();
        JsonUtility.FromJsonOverwrite(json, data);
        return data;
    }

    public static byte[] EncodePlayerData(PlayerData data, bool desireFileCreation) {
        string json = JsonUtility.ToJson(data);
        if (desireFileCreation)
            WriteToFile("PlayerData-" + data.access_token, json);
        return Encoding.UTF8.GetBytes(json);
    }

    public static PlayerData DecodePlayerData(string source, bool isAFile) {
        string json = isAFile ? ReadFromFIle(source) : source;
        PlayerData data = new PlayerData();
        JsonUtility.FromJsonOverwrite(json, data);
        return data;
    }
}
