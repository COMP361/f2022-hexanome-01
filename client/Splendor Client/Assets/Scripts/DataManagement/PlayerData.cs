using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class PlayerData {
    public string username, access_token, refresh_token; //should password be added?
    public DateTime expires_in;
    public CardData[] inventory, reserved;
    public NobleData[] nobles;
    public int[] discounts = new int[5]; //order: red, green, blue, brown, white

    public PlayerData() { }

    public PlayerData(LobbyPlayer player) {
        username = player.username;
        access_token = player.access_token;
        refresh_token = player.refresh_token;
        expires_in = player.expires_in;
    }

    public PlayerData(Player player) {
        inventory = new CardData[player.inventory.Count];
        //reserved = new CardData[player.reserces.Count];
        nobles = new NobleData[player.noblesVisited.Count];
        for (int i = 0; i < inventory.Length; i++)
            inventory[i] = new CardData(player.inventory[i]);
        for (int i = 0; i < nobles.Length; i++)
            nobles[i] = new NobleData(player.noblesVisited[i]);
        /*for (int i = 0;i < reserved.legnth;i++)
         * reserved[i] = new CardData(player.reserves[i]); uncomment when reserved cards are implemented */
        // discounts[0] = player.GetTotalGemsAquired().red;
        // discounts[1] = player.GetTotalGemsAquired().green;
        // discounts[2] = player.GetTotalGemsAquired().blue;
        // discounts[3] = player.GetTotalGemsAquired().brown;
        // discounts[4] = player.GetTotalGemsAquired().white;
    }

    public PlayerData(LobbyPlayer lobbyInfo, Player gameInfo) {
        username = lobbyInfo.username;
        access_token = lobbyInfo.access_token;
        inventory = new CardData[gameInfo.inventory.Count];
        //reserved = new CardData[player.reserces.Count];
        nobles = new NobleData[gameInfo.noblesVisited.Count];
        for (int i = 0; i < inventory.Length; i++)
            inventory[i] = new CardData(gameInfo.inventory[i]);
        for (int i = 0; i < nobles.Length; i++)
            nobles[i] = new NobleData(gameInfo.noblesVisited[i]);
        /*for (int i = 0;i < reserved.legnth;i++)
         * reserved[i] = new CardData(player.reserves[i]); uncomment when reserved cards are implemented */
        // discounts[0] = gameInfo.GetTotalGemsAquired().red;
        // discounts[1] = gameInfo.GetTotalGemsAquired().green;
        // discounts[2] = gameInfo.GetTotalGemsAquired().blue;
        // discounts[3] = gameInfo.GetTotalGemsAquired().brown;
        // discounts[4] = gameInfo.GetTotalGemsAquired().white;
    }
    
}
