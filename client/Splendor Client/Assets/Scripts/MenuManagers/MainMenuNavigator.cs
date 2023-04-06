using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class MainMenuNavigator : MonoBehaviour
{

    public void MenuScene()
    {
        SceneManager.LoadScene(1); //loads menu scene
    }

    public void AdminScene()
    {
        SceneManager.LoadScene(4); //loads menu scene
    }
}
