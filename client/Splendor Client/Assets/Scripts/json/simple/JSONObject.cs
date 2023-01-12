using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using UnityEngine;
using System.Text;

public class JSONObject : IDictionary, IJSONAware, IJSONStreamAware {
    private const long serialVersionUID = -503443796854799292L;
    private IDictionary dictionary;

    public bool IsFixedSize => dictionary.IsFixedSize;

    public bool IsReadOnly => dictionary.IsReadOnly;

    public ICollection Keys => dictionary.Keys;

    public ICollection Values => dictionary.Values;

    public int Count => dictionary.Count;

    public bool IsSynchronized => dictionary.IsSynchronized;

    public object SyncRoot => dictionary.SyncRoot;

    public object this[object key] { get => dictionary[key]; set => dictionary[key] = value; }

    /*
    public JSONObject() {
        dictionary = new IDictionary();
    }
    */

    /**
	 * Allows creation of a JSONObject from a IDictionary. After that, both the
	 * generated JSONObject and the IDictionary can be modified independently.
	 * 
	 * @param dictionary
	 */
    public JSONObject(IDictionary d) {
        dictionary = d;
    }


    /**
     * Encode a dictionary into JSON text and Write it to stringWriter.
     * If this dictionary is also a JSONAware or JSONStreamAware, JSONAware or JSONStreamAware specific behaviours will be ignored at this top level.
     * 
     * @see org.json.simple.JSONValue#WriteJSONString(object, StringWriter)
     * 
     * @param dictionary
     * @param stringWriter
     */
    public static void WriteJSONString(IDictionary dictionary, StringWriter stringWriter) {
        if (dictionary == null) {
            stringWriter.Write("null");
            return;
        }


        bool first = true;
        IDictionaryEnumerator iter = dictionary.GetEnumerator();
        stringWriter.Write('{');
        while (iter.MoveNext()) {
            if (first)
                first = false;
            else
                stringWriter.Write(',');
            //IDictionaryEnumerator entry = (IDictionaryEnumerator)iter.Current;
            stringWriter.Write('\"');
            stringWriter.Write(Escape(iter.Key.ToString()));
            stringWriter.Write('\"');
            stringWriter.Write(':');
            JSONValue.WriteJSONString(iter.Value, stringWriter);
        }
        stringWriter.Write('}');
    }

    public void WriteJSONString(StringWriter stringWriter) {
        WriteJSONString(this, stringWriter);
    }

    /**
     * Convert a dictionary to JSON text. The result is a JSON object. 
     * If this dictionary is also a JSONAware, JSONAware specific behaviours will be omitted at this top level.
     * 
     * @see org.json.simple.JSONValue#ToJSONString(object)
     * 
     * @param dictionary
     * @return JSON text, or "null" if dictionary is null.
     */
    public static string ToJSONString(IDictionary dictionary) {
        StringWriter stringWriter = new StringWriter();

        try {
            WriteJSONString(dictionary, stringWriter);
            return stringWriter.ToString();
        }
        catch (IOException e) {
            // This should never happen with a StringWriter
            throw new Exception(e.ToString());
        }
    }

    public string ToJSONString() {
        return ToJSONString(this);
    }

    override
    public string ToString() {
        return ToJSONString();
    }

    public static string ToString(string key, object value) {
        StringBuilder sb = new StringBuilder();
        sb.Append('\"');
        if (key == null)
            sb.Append("null");
        else
            JSONValue.Escape(key, sb);
        sb.Append('\"').Append(':');

        sb.Append(JSONValue.ToJSONString(value));

        return sb.ToString();
    }

    /**
     * Escape quotes, \, /, \r, \n, \b, \f, \t and other control characters (U+0000 through U+001F).
     * It's the same as JSONValue.Escape() only for compatibility here.
     * 
     * @see org.json.simple.JSONValue#Escape(string)
     * 
     * @param s
     * @return
     */
    public static string Escape(string s) {
        return JSONValue.Escape(s);
    }

    public void Add(object key, object value) {
        dictionary.Add(key, value);
    }

    public void Clear() {
        dictionary.Clear();
    }

    public bool Contains(object key) {
        return dictionary.Contains(key);
    }

    public IDictionaryEnumerator GetEnumerator() {
        return dictionary.GetEnumerator();
    }

    public void Remove(object key) {
        dictionary.Remove(key);
    }

    public void CopyTo(Array array, int index) {
        dictionary.CopyTo(array, index);
    }

    IEnumerator IEnumerable.GetEnumerator() {
        return dictionary.GetEnumerator();
    }
}
