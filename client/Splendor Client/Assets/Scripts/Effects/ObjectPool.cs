using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ObjectPool : MonoBehaviour {

    [SerializeField] private bool useActiveSupply;
    private List<GameObject> objectPool;
    [SerializeField] private GameObject obj;
    [SerializeField] private int amountToPool;
    [SerializeField] private Vector3 scale;

    // Start is called before the first frame update
    void Start() {
        objectPool = new List<GameObject>();
        GameObject temp;
        for(int i = 0;i < amountToPool;i++) {
            temp = Instantiate(obj);
            temp.SetActive(false);
            temp.transform.SetParent(transform);
            temp.transform.localScale = scale;
            objectPool.Add(temp);
        }
    }

    public GameObject GetObject() {
        for(int i = 0;i < objectPool.Count; i++) {
            if (!objectPool[i].activeInHierarchy)
                return objectPool[i];
        }

        return objectPool[0];
    }
}
