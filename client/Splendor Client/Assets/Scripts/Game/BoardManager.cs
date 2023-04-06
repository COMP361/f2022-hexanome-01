using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.Events;
using UnityEngine.SceneManagement;

public class BoardManager : MonoBehaviour
{
    [SerializeField] private UnityEvent loadBoard;
    [SerializeField] private Authentication mainPlayer;
    [SerializeField] private ActiveSession currentSession;
    [SerializeField] private NobleRow nobles;
    [SerializeField] private AllCards cards;
    [SerializeField] private Player[] boardPlayers;
	[SerializeField] private TokenBank tokens;
    [SerializeField] private CityRow cities;
    [SerializeField] private GameObject tradingPostsDisplay;
    [SerializeField] private Winner winner;
    [SerializeField] private SelectedTokens selectedTokens;
    private Player[] players;

    private string currentPlayer;

    private string boardHash = null;
    
    void Start()
    {
        BoardPolling();
    }

    public void BoardPolling()
    {
        StartCoroutine(GameRequestManager.GetBoard(currentSession.id, boardHash, SetBoard));
    }

    public void SetBoard(string hash, JSONObject boardData)
    {
        if (hash != null && boardData != null)
        {
            boardHash = hash;
            
            //STEP 1: set cards
            JSONArray cardsData = (JSONArray)boardData["cards"];
            cards.ResetAllCards();
            //for each level
            IEnumerator cardLevelEnumerator = cardsData.GetEnumerator();
            for (int level = 0; cardLevelEnumerator.MoveNext(); level++)
            {
                bool orient = false;
                if (level > 2)
                    orient = true;

                //for each card at a level
                IEnumerator cardEnumerator = ((JSONArray)cardLevelEnumerator.Current).GetEnumerator();
                for (int i = 0; cardEnumerator.MoveNext(); i++)
                {
                    cards.SetCard(orient, level % 3, i, (long)cardEnumerator.Current);
                }
            }

            //STEP 2: remove empty deck sprites
            JSONArray decks = (JSONArray)boardData["decks"];
            if (!decks.Contains("level1"))
            {
                GameObject deck = GameObject.Find("CardBack1");
                if (deck != null) deck.SetActive(false);
            }
            if (!decks.Contains("level2")) 
            {
                GameObject deck = GameObject.Find("CardBack2");
                if (deck != null) deck.SetActive(false);
            }
            if (!decks.Contains("level3")) 
            {
                GameObject deck = GameObject.Find("CardBack3");
                if (deck != null) deck.SetActive(false);
            }
            if (!decks.Contains("orientlevel1")) 
            {
                GameObject deck = GameObject.Find("ExpansionCardBack1");
                if (deck != null) deck.SetActive(false);
            }
            if (!decks.Contains("orientlevel2")) 
            {
                GameObject deck = GameObject.Find("ExpansionCardBack2");
                if (deck != null) deck.SetActive(false);
            }
            if (!decks.Contains("orientlevel3")) 
            {
                GameObject deck = GameObject.Find("ExpansionCardBack3");
                if (deck != null) deck.SetActive(false);
            }

            //STEP 3: set nobles
            JSONArray noblesData = (JSONArray)boardData["nobles"];
            IEnumerator nobleEnumerator = noblesData.GetEnumerator();

            nobles.ResetAllNobles();

            nobles.SetSize(noblesData.Count);
            for (int i = 0; nobleEnumerator.MoveNext(); i++)
            {
                nobles.SetNoble((long)nobleEnumerator.Current, i);
            }

            //TO DO: STEP 4: set token bank
            JSONObject tokenBank = (JSONObject)boardData["tokens"];
            tokens.Set(
                (long)tokenBank["gold"],
                (long)tokenBank["blue"],
                (long)tokenBank["green"],
                (long)tokenBank["black"],
                (long)tokenBank["red"],
                (long)tokenBank["white"]
            );
            selectedTokens.clearUI();

            //INTERMEDIATE STEP: set cities if variant is cities
            if (currentSession.name.Equals("cities"))
            {
                JSONArray citiesData = (JSONArray)boardData["cities"];
                cities.ResetAllCities();
                IEnumerator citiesEnumerator = citiesData.GetEnumerator();

                for (int i = 0; citiesEnumerator.MoveNext(); i++)
                {
                    cities.SetCity((long)citiesEnumerator.Current, i);
                }
            }

            //STEP 5: set players
            JSONObject inventories = (JSONObject)boardData["inventories"];

            //set players if they havent yet been set
            if (players == null)
            {
                //player count set up
                switch (inventories.Count)
                {
                    case 2:
                        players = new[] { boardPlayers[0], boardPlayers[1] };
                        break;
                    case 3:
                        players = new[] { boardPlayers[0], boardPlayers[2], boardPlayers[3] };
                        break;
                    case 4:
                        players = new[] { boardPlayers[0], boardPlayers[1], boardPlayers[2], boardPlayers[3] };
                        break;
                }

                //set main player data and display data
                players[0].SetUsername(mainPlayer.GetUsername());
                Dashboard dashboard = players[0].GetComponent<Dashboard>();
                if (dashboard != null)
                    dashboard.SetNobleReserveCount(noblesData.Count);

                //set other players' data
                IEnumerator usernames = ((ICollection)inventories.Keys).GetEnumerator();

                int i = 1;
                while (usernames.MoveNext() && i < players.Length)
                {
                    string username = (string)usernames.Current;
                    if (!username.Equals(mainPlayer.GetUsername()))
                    {
                        players[i].SetUsername(username);
                        i++;
                    }
                }
            }

            //STEP 6: set current player
            currentPlayer = (string)boardData["currentPlayer"];
            //display the current player
            foreach (Player player in players)
            {
                if (player.GetUsername().Equals(currentPlayer))
                    player.SetCurrentPlayer(true);
                else
                    player.SetCurrentPlayer(false);
            }

            //STEP 7: set player inventories
            foreach (Player player in players)
            {
                //reset all inventories
                player.ResetInventory();

                //fill inventories
                JSONObject inventory = (JSONObject) inventories[player.GetUsername()];
                
                //set points 
                player.SetPoints((long)inventory["points"]);

                //set reserved cards
                IEnumerator reservedCards = ((JSONArray)inventory["reservedCards"]).GetEnumerator();
                while (reservedCards.MoveNext())
                {
                    player.AddReservedCard(cards.cards.Find(x => x.id.Equals((long)reservedCards.Current)));
                }

                //set acquired cards
                IEnumerator acquiredCards = ((JSONArray)inventory["acquiredCards"]).GetEnumerator();
                while (acquiredCards.MoveNext())
                {
                    player.AddAcquiredCard(cards.cards.Find(x => x.id.Equals((long)acquiredCards.Current)));
                }

                //set reserved nobles
                IEnumerator reservedNobles = ((JSONArray)inventory["reservedNobles"]).GetEnumerator();
                while (reservedNobles.MoveNext())
                {
                    player.AddReservedNoble(nobles.allNobles.Find(x => x.id.Equals((long)reservedNobles.Current)));
                }

                //set acquired nobles
                IEnumerator acquiredNobles = ((JSONArray)inventory["acquiredNobles"]).GetEnumerator();
                while (acquiredNobles.MoveNext())
                {
                    player.AddAcquiredNoble(nobles.allNobles.Find(x => x.id.Equals((long)acquiredNobles.Current)));
                }

                //set token counts
                JSONObject tokenInventory = (JSONObject)inventory["tokens"];
                player.GetTokenBank().Set(
                    (long)tokenInventory["gold"],
                    (long)tokenInventory["blue"],
                    (long)tokenInventory["green"],
                    (long)tokenInventory["black"],
                    (long)tokenInventory["red"],
                    (long)tokenInventory["white"]
                );

                //set bonus counts
                JSONObject bonusInventory = (JSONObject)inventory["bonuses"];
                player.GetBonusBank().Set(
                    (long)bonusInventory["gold"],
                    (long)bonusInventory["blue"],
                    (long)bonusInventory["green"],
                    (long)bonusInventory["black"],
                    (long)bonusInventory["red"],
                    (long)bonusInventory["white"]
                );

                //set city if cities variant
                if (currentSession.name.Equals("cities") && inventory.Contains("acquiredCity"))
                {
                    long cityId = (long)inventory["acquiredCity"];
                    if (cityId >= 0)
                        player.AddCity(cities.allCities.Find(x => x.id.Equals(cityId)));
                }

                //set trading posts if trading posts variant
                if (currentSession.name.Equals("tradingposts") && inventory.Contains("acquiredTradingPosts"))
                {
                    JSONArray tradingPostData = (JSONArray)inventory["acquiredTradingPosts"];
                    IEnumerator tpEnumerator = tradingPostData.GetEnumerator();
                    while (tpEnumerator.MoveNext())
                    {
                        player.AddTradingPost((string)tpEnumerator.Current);
                    }
                }
            }

            //STEP 8: dislpay the board
            loadBoard.Invoke();

            foreach (Player player in players)
            {
                player.gameObject.SetActive(true);
                //player.GetComponent<Dashboard>().SetActive(true);
                //player.GetComponent<MultiplayerInfoPanel>().SetActive(true);

                //display city slot if variant is cities
                if (currentSession.name.Equals("cities"))
                    player.GetCitySlot().SetActive(true);

                if (currentSession.name.Equals("tradingposts"))
                    player.GetTradingPostSlots().SetActive(true);
            }

            //display trading posts if variant is trading posts
            if (currentSession.name.Equals("tradingposts"))
                tradingPostsDisplay.SetActive(true);

            //takes everyone to WinScene when there's a winner
            //Debug.Log((string)boardData["winner"]);
            if ((string)boardData["winner"] != null) {
                winner.playerName = ((string)boardData["winner"]);
                LSRequestManager.DeleteSession(currentSession.id);
                SceneManager.LoadScene("WinScene");
                //LSRequestManager.DeleteSession(currentSession.id);
                //return;
            }
        }

        BoardPolling();
    }
}