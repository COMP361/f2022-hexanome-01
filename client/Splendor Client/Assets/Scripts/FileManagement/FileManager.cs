using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.IO;

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
    //location of these files on your machine may vary, for windows its in C:/Users/{profile name}/AppData/LocalLow/Comp 361 2022 Hexanome01/Splendor Client
    //(AppData is a hidden folder, so either enable viewing of hidden folders or in search bar put %appdata% to find the folder
    public static string EncodeSession(Session session, bool desireFileCreation) {
        SessionData data = new SessionData(session);
        string json = JsonUtility.ToJson(data);
        if(desireFileCreation)
            WriteToFile("SessionData-" + session.playerList[0].userName, json);
        return json;
    }

    public static Session DecodeSession(string source, bool isAFile) {
        string json = isAFile ? ReadFromFIle(source) : source;
        SessionData data = new SessionData();
        JsonUtility.FromJsonOverwrite(json, data);
        return new Session(data);
    }

    public static void LoadSave() {

    }

    public static void CreateSave() {

    }
}