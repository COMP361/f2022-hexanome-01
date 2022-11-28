using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;
using UnityEngine.SceneManagement;

public class LoginManager : MonoBehaviour {
    public InputField usernameField, passwordField; //username and password
    public GameObject failureText; //error text
    public string username;
    public Authentication mainPlayer; //player authentication data across scenes

    public void loginStart() {
        StartCoroutine(VerifyLogin());
    }

    public IEnumerator VerifyLogin() { //verifyCredentials. atm checks with the LobbyService

        string url = "http://127.0.0.1:4242/oauth/token";
        username = usernameField.text;

        WWWForm form = new WWWForm();
        form.AddField("grant_type", "password");
        form.AddField("username", username);
        form.AddField("password", passwordField.text);

        UnityWebRequest auth = UnityWebRequest.Post(url, form);
        auth.SetRequestHeader("Authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=");

        yield return auth.SendWebRequest();
        DateTime time = DateTime.Now;

        if (auth.result != UnityWebRequest.Result.Success)
        {
            failureText.SetActive(true); //if credentials wrong, display error message
        }
        else
        {
            Login(auth.downloadHandler.text, username, time);
        }
            
    }

    public void Login(string response, string username, DateTime time) { //log player in

        //parse POST JSON response into mainPlayer
        JsonUtility.FromJsonOverwrite(response, mainPlayer);
        
        mainPlayer.expires_in = time.AddMinutes(Double.Parse(mainPlayer.expires_in)).ToString(); //set the expiry time
        mainPlayer.username = username; //add the player's username


        //loads next scene
        SceneManager.LoadScene(1);
    }

}
