using System;
using System.Text;
using UnityEngine;
using UnityEngine.UI;

public class DebugText : MonoBehaviour
{
    private static string HOST = Environment.GetEnvironmentVariable("SPLENDOR_HOST_IP");

    [SerializeField] private Text debug_ip;

    void Start()
    {
        debug_ip.text = HOST;
    }
}