using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEditor;

public class ClaimCityPanel : MonoBehaviour
{
    [SerializeField] private PlayerControl playerControl;
    [SerializeField] private GameObject citySlot; //Blank City prefab
    [SerializeField] private GameObject cityContent;
    [SerializeField] private GameObject claimCityPanel; //Menu make appear under condition after end turn/disappear after selection
    public bool hasAquiredCity;
    
  
    public void DisplayCityClaim(CityRow allCities, long[] aquiredCities) {

        hasAquiredCity = false;
        List<City> availCities = new List<City>();
        //TokenBank playerBonus = playerControl.client.GetBonusBank();

        CitySlot[] citySlots = allCities.GetAllCities();
        for (int i = 0; i < citySlots.Length; i++) {
            if (citySlots[i] != null)
            {
                City city = citySlots[i].GetCity();
                for (int j = 0; j < aquiredCities.Length; j++)
                {
                    if (city.id == aquiredCities[j])
                    {
                        availCities.Add(city);
                    }
                }
            }
        }
        
        Display(availCities);
    }

    //Switches city menu status
    public void CityMenuStatus() {
        playerControl.inCityMenu = !playerControl.inCityMenu;
    }

    //Displays the pop-up window when a player reaches requirements to get a City
    public void Display(List<City> availCities) {
        //TOCheck
        if (claimCityPanel.activeInHierarchy) 
            claimCityPanel.SetActive(false);
        
        else {
            claimCityPanel.SetActive(true);
            DisplayAvailableCities(availCities);
        }
    }

    public void TurnOffDisplay() {
        claimCityPanel.SetActive(false);
    }

    //Lists the cities that the player can get
    public void DisplayAvailableCities(List<City> availCities) {
        ClearChildren(cityContent);
        foreach (City city in availCities) {
            GameObject cityInstance = Instantiate(citySlot, cityContent.transform.position, Quaternion.identity);
            cityInstance.AddComponent<Button>().onClick.AddListener( delegate
            {
                playerControl.selectedCity = cityInstance.GetComponent<CitySlot>();
            });
            cityInstance.transform.SetParent(cityContent.transform);
            cityInstance.GetComponent<CitySlot>().SetupInventory(city);
        }

    }

    //Clear children to avoid repetition everytime the window pops up
    private void ClearChildren(GameObject parent) {
        foreach (Transform child in parent.transform) {
            Destroy(child.gameObject);
        }
    }

}
