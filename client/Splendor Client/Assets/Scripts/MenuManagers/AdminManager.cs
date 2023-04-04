using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class AdminManager : MonoBehaviour
{
    [SerializeField] private GameObject addUserFail;
    [SerializeField] private GameObject addUserSuccess;
    [SerializeField] private InputField username, password;
    [SerializeField] private Authentication mainPlayer;

    //******************************** ADMIN ********************************

    public void AddUser()
    {
        StartCoroutine(LSRequestManager.AddUser(mainPlayer, username.text, password.text, (bool success) => {
            if (!success)
            {
                username.text = "";
                password.text = "";
                addUserFail.SetActive(true); //if credentials wrong, display error message
                addUserSuccess.SetActive(false);
            }
            else
            {
                addUserFail.SetActive(false);
                addUserSuccess.SetActive(true);
            }
        }));
    }
}
