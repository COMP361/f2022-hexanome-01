using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEditor;

public class OrientPanelManager : MonoBehaviour {
    [SerializeField] private GameObject panel;

    [SerializeField] private PlayerControl playerControl;

    [SerializeField] private GameObject cardSlot; //Blank card prefab
    [SerializeField] private GameObject nobleSlot; //Blank noble prefab
    [SerializeField] private GameObject cardContent, nobleContent;

    [SerializeField] private long selectedCard = -1;
    [SerializeField] private long selectedNoble = -1;
    [SerializeField] private ActionManager.ActionType action;
    [SerializeField] private List<Card> cardsDebug;

    public void Display(List<Card> cards, List<Noble> nobles, ActionManager.ActionType _action) {
        if (!panel.activeInHierarchy) {
            cardsDebug = cards;
            this.action = _action;
            panel.SetActive(true);
            Debug.Log(cards.Count);

#if UNITY_EDITOR
            CardSlot[] boardCards = (CardSlot[])Resources.FindObjectsOfTypeAll(typeof(CardSlot));
            NobleSlot[] boardNobles = (NobleSlot[])Resources.FindObjectsOfTypeAll(typeof(NobleSlot));
            CitySlot[] cities = (CitySlot[])Resources.FindObjectsOfTypeAll(typeof(CitySlot));
#else
            CardSlot[] boardCards = (CardSlot[])Object.FindObjectsOfType(typeof(CardSlot));
            NobleSlot[] boardNobles = (NobleSlot[])Object.FindObjectsOfType(typeof(NobleSlot));
            CitySlot[] cities = (CitySlot[])Object.FindObjectsOfType(typeof(CitySlot));
#endif

            foreach (CardSlot c in boardCards) {
#if UNITY_EDITOR
                if (PrefabUtility.GetPrefabAssetType(c) == PrefabAssetType.NotAPrefab)
                    c.gameObject.SetActive(false);
#else
                    c.gameObject.SetActive(false);
#endif

            }
            foreach (NobleSlot n in boardNobles) {
#if UNITY_EDITOR
                if (PrefabUtility.GetPrefabAssetType(n) == PrefabAssetType.NotAPrefab)
                    n.gameObject.SetActive(false);
#else
                    n.gameObject.SetActive(false);
#endif

            }
            foreach (CitySlot c in cities) {
#if UNITY_EDITOR
                if (PrefabUtility.GetPrefabAssetType(c) == PrefabAssetType.NotAPrefab)
                    c.gameObject.SetActive(false);
#else
                    c.gameObject.SetActive(false);
#endif

            }

            DisplayPlayerCards(cards, nobles);
        }
        else {
#if UNITY_EDITOR
            CardSlot[] boardCards = (CardSlot[])Resources.FindObjectsOfTypeAll(typeof(CardSlot));
            NobleSlot[] boardNobles = (NobleSlot[])Resources.FindObjectsOfTypeAll(typeof(NobleSlot));
            CitySlot[] cities = (CitySlot[])Resources.FindObjectsOfTypeAll(typeof(CitySlot));
#else
            CardSlot[] boardCards = (CardSlot[])Object.FindObjectsOfType(typeof(CardSlot));
            NobleSlot[] boardNobles = (NobleSlot[])Object.FindObjectsOfType(typeof(NobleSlot));
            CitySlot[] cities = (CitySlot[])Object.FindObjectsOfType(typeof(CitySlot));
#endif

            foreach (CardSlot c in boardCards) {
#if UNITY_EDITOR
                if (PrefabUtility.GetPrefabAssetType(c) == PrefabAssetType.NotAPrefab)
                    c.gameObject.SetActive(true);
#else
                    c.gameObject.SetActive(true);
#endif

            }
            foreach (NobleSlot n in boardNobles) {
#if UNITY_EDITOR
                if (PrefabUtility.GetPrefabAssetType(n) == PrefabAssetType.NotAPrefab)
                    n.gameObject.SetActive(true);
#else
                    n.gameObject.SetActive(true);
#endif

            }
            foreach (CitySlot c in cities) {
#if UNITY_EDITOR
                if (PrefabUtility.GetPrefabAssetType(c) == PrefabAssetType.NotAPrefab)
                    c.gameObject.SetActive(true);
#else
                    c.gameObject.SetActive(true);
#endif

            }
        }
    }

    public void DisplayPlayerCards(List<Card> cards, List<Noble> nobles) { //displays acquired cards/nobles
        ClearChildren(cardContent, nobleContent);
        if (cards.Count > 0) {
            cardContent.SetActive(true);
            foreach (Card card in cards) {
                GameObject temp = Instantiate(cardSlot, cardContent.transform.position, Quaternion.identity);
                long x = card.GetId();
                temp.transform.GetComponent<Button>().onClick.AddListener(() => SelectCard(x));
                temp.transform.SetParent(cardContent.transform);
                temp.transform.localScale = new Vector3(1, 1, 1);
                temp.GetComponent<CardSlot>().SetupInventory(card);
            }
        }
        if (nobles.Count > 0) {
            cardContent.SetActive(true);
            foreach (Noble noble in nobles) {
                GameObject temp = Instantiate(nobleSlot, cardContent.transform.position, Quaternion.identity);
                long x = noble.id;
                temp.transform.GetComponent<Button>().onClick.AddListener(() => SelectCard(x));
                temp.transform.SetParent(cardContent.transform);
                temp.transform.localScale = new Vector3(1, 1, 1);
                temp.GetComponent<NobleSlot>().SetupInventory(noble);
            }
        }
    }

    public void SelectCard(long selected) {
        selectedCard = selected;
        selectedNoble = -1;
    }

    public void SelectNoble(long selected) {
        selectedNoble = selected;
        selectedCard = -1;
    }

    public void Select() {
        if (selectedCard != -1 && action == ActionManager.ActionType.domino) {
            playerControl.dominoCardAction(selectedCard);
            Display(null, null, ActionManager.ActionType.satchel);
            panel.SetActive(false);
        }
        else if (selectedCard != -1 && action == ActionManager.ActionType.satchel) {
            playerControl.satchelAction(selectedCard);
            Display(null, null, ActionManager.ActionType.satchel);
            panel.SetActive(false);
        }
        else if (selectedCard != -1 && action == ActionManager.ActionType.dominoSatchel) {
            playerControl.dominoSatchelAction(selectedCard);
            Display(null, null, ActionManager.ActionType.satchel);
            panel.SetActive(false);
        }
        else if (selectedCard != -1 && action == ActionManager.ActionType.reserveNoble) {
            playerControl.reserveNobleAction(selectedCard);
            Display(null, null, ActionManager.ActionType.satchel);
            panel.SetActive(false);
        }
    }

    private void ClearChildren(GameObject parent, GameObject nobleParent) { //Need to clear children to not repeat cards everytime we open and close the panel
        foreach (Transform child in parent.transform) {
            Destroy(child.gameObject);
        }
        parent.SetActive(false);
        foreach (Transform child in nobleParent.transform) {
            Destroy(child.gameObject);

        }
        nobleParent.SetActive(false);
    }

    private void ShowObjects() {
#if UNITY_EDITOR
        CardSlot[] boardCards = (CardSlot[])Resources.FindObjectsOfTypeAll(typeof(CardSlot));
        NobleSlot[] boardNobles = (NobleSlot[])Resources.FindObjectsOfTypeAll(typeof(NobleSlot));
        CitySlot[] cities = (CitySlot[])Resources.FindObjectsOfTypeAll(typeof(CitySlot));
#else
        CardSlot[] boardCards = (CardSlot[])Object.FindObjectsOfType(typeof(CardSlot));
        NobleSlot[] boardNobles = (NobleSlot[])Object.FindObjectsOfType(typeof(NobleSlot));
        CitySlot[] cities = (CitySlot[])Object.FindObjectsOfType(typeof(CitySlot));
#endif

        foreach (CardSlot c in boardCards) {
#if UNITY_EDITOR
            if (PrefabUtility.GetPrefabAssetType(c) == PrefabAssetType.NotAPrefab)
                c.gameObject.SetActive(true);
#else
                c.gameObject.SetActive(true);
#endif

        }
        foreach (NobleSlot n in boardNobles) {
#if UNITY_EDITOR
            if (PrefabUtility.GetPrefabAssetType(n) == PrefabAssetType.NotAPrefab)
                n.gameObject.SetActive(true);
#else
                n.gameObject.SetActive(true);
#endif

        }
        foreach (CitySlot c in cities) {
#if UNITY_EDITOR
            if (PrefabUtility.GetPrefabAssetType(c) == PrefabAssetType.NotAPrefab)
                c.gameObject.SetActive(true);
#else
                c.gameObject.SetActive(true);
#endif

        }
    }
}
