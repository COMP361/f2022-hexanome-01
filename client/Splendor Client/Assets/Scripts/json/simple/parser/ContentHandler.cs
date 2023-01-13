using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public interface IContentHandler {
    /**
	 * Receive notification of the beginning of JSON processing.
	 * The parser will invoke this method only once.
     * 
	 * @throws ParseException 
	 * 			- JSONParser will stop and throw the same exception to the caller when receiving this exception.
	 */
    void StartJSON() ;

    /**
	 * Receive notification of the End of JSON processing.
	 * 
	 * @throws ParseException
	 */
    void EndJSON() ;

    /**
	 * Receive notification of the beginning of a JSON object.
	 * 
	 * @return false if the handler wants to stop parsing after return.
	 * @throws ParseException
     *          - JSONParser will stop and throw the same exception to the caller when receiving this exception.
     * @see #EndJSON
	 */
    bool StartObject() ;

    /**
	 * Receive notification of the End of a JSON object.
	 * 
	 * @return false if the handler wants to stop parsing after return.
	 * @throws ParseException
     * 
     * @see #StartObject
	 */
    bool EndObject() ;

    /**
	 * Receive notification of the beginning of a JSON object entry.
	 * 
	 * @param key - Key of a JSON object entry. 
	 * 
	 * @return false if the handler wants to stop parsing after return.
	 * @throws ParseException
     * 
     * @see #EndObjectEntry
	 */
    bool StartObjectEntry(string key) ;

    /**
	 * Receive notification of the End of the value of previous object entry.
	 * 
	 * @return false if the handler wants to stop parsing after return.
	 * @throws ParseException
     * 
     * @see #StartObjectEntry
	 */
    bool EndObjectEntry() ;

    /**
	 * Receive notification of the beginning of a JSON array.
	 * 
	 * @return false if the handler wants to stop parsing after return.
	 * @throws ParseException
     * 
     * @see #EndArray
	 */
    bool StartArray() ;

    /**
	 * Receive notification of the End of a JSON array.
	 * 
	 * @return false if the handler wants to stop parsing after return.
	 * @throws ParseException
     * 
     * @see #StartArray
	 */
    bool EndArray() ;

    /**
	 * Receive notification of the JSON primitive values:
	 * 	java.lang.String,
	 * 	java.lang.Number,
	 * 	java.lang.Boolean
	 * 	null
	 * 
	 * @param value - Instance of the following:
	 * 			java.lang.String,
	 * 			java.lang.Number,
	 * 			java.lang.Boolean
	 * 			null
	 * 
	 * @return false if the handler wants to stop parsing after return.
	 * @throws ParseException
	 */
    bool Primitive(object value) ;
}
