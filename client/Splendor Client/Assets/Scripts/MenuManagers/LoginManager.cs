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

    public void loginStart() { //allows POST request for authentication in login
        StartCoroutine(VerifyLogin());
    }

    public IEnumerator VerifyLogin() { //verifyCredentials. atm checks with the LobbyService

        string host = Environment.GetEnvironmentVariable("SPLENDOR_HOST_IP");

        string url = "http://" + host + ":4242/oauth/token"; //url for POST request
        username = usernameField.text; //keep the submitted username even if it is changed after login button clicked

        WWWForm form = new WWWForm();
        form.AddField("grant_type", "password");
        form.AddField("username", username);
        form.AddField("password", passwordField.text);

        UnityWebRequest auth = UnityWebRequest.Post(url, form);
        auth.SetRequestHeader("Authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc="); //authenticate the POST request

        yield return auth.SendWebRequest();
        DateTime time = DateTime.Now; //to calculate when the refresh of the access_token needs to happen

        if (auth.result != UnityWebRequest.Result.Success)
        {
            usernameField.text = "";
            passwordField.text = "";
            failureText.SetActive(true); //if credentials wrong, display error message
        }
        else {
            parsePostResponse(auth.downloadHandler.text, username, time);
            SceneManager.LoadScene(1); //loads next scene
        }     
    }

    public void refreshStart() { //allows POST request for authentication in refreshing the access_token
        StartCoroutine(Refresh());
    }

    public IEnumerator Refresh() { //to refresh access_token

        string url = "http://127.0.0.1:4242/oauth/token"; //url for POST request
        username = usernameField.text; //keep the submitted username even if it is changed after login button clicked

        WWWForm form = new WWWForm();
        form.AddField("grant_type", "refresh_token");
        form.AddField("refresh_token", mainPlayer.refresh_token);

        UnityWebRequest auth = UnityWebRequest.Post(url, form);
        auth.SetRequestHeader("Authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc="); //authenticate the POST request

        yield return auth.SendWebRequest();
        DateTime time = DateTime.Now; //to calculate when the refresh of the access_token needs to happen

        if (auth.result == UnityWebRequest.Result.Success) {
            parsePostResponse(auth.downloadHandler.text, username, time);
        }
    }

    private void parsePostResponse(string response, string username, DateTime time) {

        //parse POST JSON response into mainPlayer
        JsonUtility.FromJsonOverwrite(response, mainPlayer);
        Invoke("refreshStart", float.Parse(mainPlayer.expires_in) - 5.0f); //refresh automatically when token is about to expire

        mainPlayer.expires_in = time.AddSeconds(Double.Parse(mainPlayer.expires_in)).ToString(); //set the expiry time
        mainPlayer.username = username; //add the player's username
    }
}
