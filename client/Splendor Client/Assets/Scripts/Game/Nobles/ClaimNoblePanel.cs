using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEditor;

public class ClaimNoblePanel : MonoBehaviour
{
    [SerializeField] private PlayerControl playerControl;
    [SerializeField] private GameObject nobleSlot; //Blank noble prefab
    [SerializeField] private GameObject nobleContent;
    [SerializeField] private GameObject claimNoblePanel; //Menu make appear under condition after end turn/disappear after selection
    public bool hasImpressedNoble;
    
    //Check if there is any noble impressed by player
    public void DisplayNobleClaim(NobleRow allNobles, long[] impressedNobles) {

        hasImpressedNoble = false;
        List<Noble> availNobles = new List<Noble>();
        //TokenBank playerBonus = playerControl.client.GetBonusBank();

        NobleSlot[] nobleSlots = allNobles.GetAllNobels();
        for (int i = 0; i < nobleSlots.Length; i++) {
            if (nobleSlots[i] != null)
            {
                Noble noble = nobleSlots[i].GetNoble();
                for (int j = 0; j < impressedNobles.Length; j++)
                {
                    if (noble.id == impressedNobles[j])
                    {
                        availNobles.Add(noble);
                    }
                }
            }
        }
        
        Display(availNobles);
    }

    //Switches noble menu status
    public void NobleMenuStatus() {
        playerControl.inNobleMenu = !playerControl.inNobleMenu;
    }

    //Displays the pop-up window when a player reaches requirements to get a noble
    public void Display(List<Noble> availNobles) {
        //TOCheck
        if (claimNoblePanel.activeInHierarchy) 
            claimNoblePanel.SetActive(false);
        
        else {
            claimNoblePanel.SetActive(true);
            DisplayAvailableNobles(availNobles);
        }
    }

    public void TurnOffDisplay() {
        claimNoblePanel.SetActive(false);
    }

    //Lists the nobles that the player can get
    public void DisplayAvailableNobles(List<Noble> availNobles) {
        ClearChildren(nobleContent);
        foreach (Noble noble in availNobles) {
            GameObject nobleInstance = Instantiate(nobleSlot, nobleContent.transform.position, Quaternion.identity);
            nobleInstance.AddComponent<Button>().onClick.AddListener( delegate
            {
                playerControl.selectedNoble = nobleInstance.GetComponent<NobleSlot>();
            });
            nobleInstance.transform.SetParent(nobleContent.transform);
            nobleInstance.GetComponent<NobleSlot>().SetupInventory(noble);
        }

    }

    //Clear children to avoid repetition everytime the window pops up
    private void ClearChildren(GameObject parent) {
        foreach (Transform child in parent.transform) {
            Destroy(child.gameObject);
        }
    }

}
