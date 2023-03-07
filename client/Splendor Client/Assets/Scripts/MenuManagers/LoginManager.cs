using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class LoginManager : MonoBehaviour
{
    [SerializeField] private GameObject loginFail;
    [SerializeField] private InputField username, password;
    [SerializeField] private Authentication mainPlayer;

    //******************************** LOGIN ********************************

    public void Login()
    {
        StartCoroutine(LSRequestManager.Login(mainPlayer, username.text, password.text, (bool success) => {
            if (!success)
            {
                username.text = "";
                password.text = "";
                loginFail.SetActive(true); //if credentials wrong, display error message
            }
            else
            {
                SceneManager.LoadScene(1); //loads next scene
            }
        }));
    }
}
