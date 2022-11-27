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

        if (auth.result != UnityWebRequest.Result.Success)
        {
            failureText.SetActive(true); //if credentials wrong, display error message
            UnityEngine.Debug.Log(auth.downloadHandler.text);
        }
        else
        {
            Login();
            //eventually to be similar to the following (except System.Text.Json doesn't exist anymore):
            //Login(JsonSerializer.Deserialize<Dictionary<string, string>>((auth.downloadHandler.text)));
        }
            
    }

    //eventually will need a parameter as follows:
    //Dictionary<string,string> response
    public void Login() { //log player in. atm just loads next scene

        //eventually should add player data to store across scenes

        SceneManager.LoadScene(1);
    }

}
