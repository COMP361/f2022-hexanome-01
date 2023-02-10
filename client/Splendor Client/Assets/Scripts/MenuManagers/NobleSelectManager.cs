using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class NobleSelectManager : MonoBehaviour
{
    [SerializeField] private GameObject blankNobleSlot, blankCardSlot, content; //empty noble/card prefab, content = panel to display options, and button to backout of a sacrifice
    [SerializeField] private PlayerControl playerControl;
    //[SerializeField] private Text actionText, errorText; //text boxes
    private Noble currentNoble; //selected noble
    // Start is called before the first frame update
    

    public void NobleMenuStatus() { //flips menu status (needed for use with a button)
        playerControl.inNobleMenu = !playerControl.inNobleMenu;
    }

    void ClearChildren() { //clears all things in the menu
        foreach (Transform child in content.transform)
            Destroy(child.gameObject);
    }

    void ResetSelections() {  //reset selections from previous menu usage
        currentNoble = null;
    }

    public void PopulateMenu(List<Noble> noblesImpressed) { //fill the menu with the objects passed in the list
        ClearChildren(); //first clear menu of all remnants of previous populating
        ResetSelections(); //reset all card selections since this is a new selection
        foreach (Noble i in noblesImpressed) { //for all objects in the list
            
                GameObject temp = Instantiate(blankNobleSlot, content.transform.position, Quaternion.identity);
                temp.transform.SetParent(content.transform);
                //temp.transform.localScale = new Vector3(0.2f, 0.4f, 1);
                temp.GetComponent<NobleSlot>().SetupNobleSelect(this, i);
            
        }
    }

    

}
