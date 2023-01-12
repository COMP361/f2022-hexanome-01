using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public interface IContainerFactory {
    /**
         * @return A Map instance to store JSON object, or null if you want to use org.json.simple.JSONObject.
         */
    IDictionary CreateObjectContainer();

    /**
	 * @return A List instance to store JSON array, or null if you want to use org.json.simple.JSONArray. 
	 */
    IList CreateArrayContainer();
}
