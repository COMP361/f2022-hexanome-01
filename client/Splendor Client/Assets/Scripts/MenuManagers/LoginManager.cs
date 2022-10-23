using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;

public class LoginManager : MonoBehaviour {
    public InputField usernameField, passwordField; //username and password
    public GameObject failureText; //error text

    public void VerifyLogin() { //verifyCredentials. atm just checks if both fields contain something
        if (usernameField.text != "" && passwordField.text != "")
            Login();
        else
            failureText.SetActive(true); //if credentials wrong, display error message
    }

    public void Login() { //log player in. atm just loads next scene
        SceneManager.LoadScene(1);
    }
}
