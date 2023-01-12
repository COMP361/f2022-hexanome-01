using System;
using System.Collections;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.IO;
using UnityEngine;

public class JSONArray : ArrayList, IJSONAware, IJSONStreamAware {
    private const long serialVersionUID = 3957988303675231981L;

    /**
	 * Constructs an empty JSONArray.
	 */
    public JSONArray() {
        //base();
    }

    /**
	 * Constructs a JSONArray containing the elements of the specified
	 * Collections, in the order they are returned by the Collections's IEnumerator.
	 * 
	 * @param c the Collections whose elements are to be placed into this JSONArray
	 */
    public JSONArray(ICollection c) : base(c) {
        //base.(c);
    }

    /**
     * Encode a list into JSON text and Write it to stringWriter. 
     * If this list is also a JSONStreamAware or a JSONAware, JSONStreamAware and JSONAware specific behaviours will be ignored at this top level.
     * 
     * @see org.json.simple.JSONValue#WriteJSONString(object, StringWriter)
     * 
     * @param Collections
     * @param stringWriter
     */
    public static void WriteJSONString(ICollection Collections, StringWriter stringWriter) {
        if (Collections == null) {
            stringWriter.Write("null");
            return;
        }

        bool first = true;
        IEnumerator iter = Collections.GetEnumerator();

        stringWriter.Write('[');
        while (iter.MoveNext()) {
            if (first)
                first = false;
            else
                stringWriter.Write(',');

            object value = iter.Current;
            if (value == null) {
                stringWriter.Write("null");
                continue;
            }

            JSONValue.WriteJSONString(value, stringWriter);
        }
        stringWriter.Write(']');
    }

    public void WriteJSONString(StringWriter stringWriter) {
        WriteJSONString(this, stringWriter);
    }

    /**
     * Convert a list to JSON text. The result is a JSON array. 
     * If this list is also a JSONAware, JSONAware specific behaviours will be omitted at this top level.
     * 
     * @see org.json.simple.JSONValue#ToJSONstring(object)
     * 
     * @param Collections
     * @return JSON text, or "null" if list is null.
     */
    public static string ToJSONstring(ICollection Collections) {
        StringWriter StringWriter = new StringWriter();

        try {
            WriteJSONString(Collections, StringWriter);
            return StringWriter.ToString();
        }
        catch (Exception e) {
            // This should never happen for a StringWriter
            throw new Exception(e.ToString());
        }
    }

    public static void WriteJSONString(byte[] array, StringWriter stringWriter) {
        if (array == null) {
            stringWriter.Write("null");
        }
        else if (array.Length == 0) {
            stringWriter.Write("[]");
        }
        else {
            stringWriter.Write("[");
            stringWriter.Write(array[0].ToString());

            for (int i = 1; i < array.Length; i++) {
                stringWriter.Write(",");
                stringWriter.Write(array[i].ToString());
            }

            stringWriter.Write("]");
        }
    }

    public static string ToJSONstring(byte[] array) {
        StringWriter StringWriter = new StringWriter();

        try {
            WriteJSONString(array, StringWriter);
            return StringWriter.ToString();
        }
        catch (Exception e) {
            // This should never happen for a StringWriter
            throw new Exception(e.ToString());
        }
    }

    public static void WriteJSONString(short[] array, StringWriter stringWriter) {
        if (array == null) {
            stringWriter.Write("null");
        }
        else if (array.Length == 0) {
            stringWriter.Write("[]");
        }
        else {
            stringWriter.Write("[");
            stringWriter.Write(array[0].ToString());

            for (int i = 1; i < array.Length; i++) {
                stringWriter.Write(",");
                stringWriter.Write(array[i].ToString());
            }

            stringWriter.Write("]");
        }
    }

    public static string ToJSONstring(short[] array) {
        StringWriter StringWriter = new StringWriter();

        try {
            WriteJSONString(array, StringWriter);
            return StringWriter.ToString();
        }
        catch (Exception e) {
            // This should never happen for a StringWriter
            throw new Exception(e.ToString());
        }
    }

    public static void WriteJSONString(int[] array, StringWriter stringWriter) {
        if (array == null) {
            stringWriter.Write("null");
        }
        else if (array.Length == 0) {
            stringWriter.Write("[]");
        }
        else {
            stringWriter.Write("[");
            stringWriter.Write(array[0].ToString());

            for (int i = 1; i < array.Length; i++) {
                stringWriter.Write(",");
                stringWriter.Write(array[i].ToString());
            }

            stringWriter.Write("]");
        }
    }

    public static string ToJSONstring(int[] array) {
        StringWriter StringWriter = new StringWriter();

        try {
            WriteJSONString(array, StringWriter);
            return StringWriter.ToString();
        }
        catch (Exception e) {
            // This should never happen for a StringWriter
            throw new Exception(e.ToString());
        }
    }

    public static void WriteJSONString(long[] array, StringWriter stringWriter) {
        if (array == null) {
            stringWriter.Write("null");
        }
        else if (array.Length == 0) {
            stringWriter.Write("[]");
        }
        else {
            stringWriter.Write("[");
            stringWriter.Write(array[0].ToString());

            for (int i = 1; i < array.Length; i++) {
                stringWriter.Write(",");
                stringWriter.Write(array[i].ToString());
            }

            stringWriter.Write("]");
        }
    }

    public static string ToJSONstring(long[] array) {
        StringWriter StringWriter = new StringWriter();

        try {
            WriteJSONString(array, StringWriter);
            return StringWriter.ToString();
        }
        catch (Exception e) {
            // This should never happen for a StringWriter
            throw new Exception(e.ToString());
        }
    }

    public static void WriteJSONString(float[] array, StringWriter stringWriter) {
        if (array == null) {
            stringWriter.Write("null");
        }
        else if (array.Length == 0) {
            stringWriter.Write("[]");
        }
        else {
            stringWriter.Write("[");
            stringWriter.Write(array[0].ToString());

            for (int i = 1; i < array.Length; i++) {
                stringWriter.Write(",");
                stringWriter.Write(array[i].ToString());
            }

            stringWriter.Write("]");
        }
    }

    public static string ToJSONstring(float[] array) {
        StringWriter StringWriter = new StringWriter();

        try {
            WriteJSONString(array, StringWriter);
            return StringWriter.ToString();
        }
        catch (Exception e) {
            // This should never happen for a StringWriter
            throw new Exception(e.ToString());
        }
    }

    public static void WriteJSONString(double[] array, StringWriter stringWriter) {
        if (array == null) {
            stringWriter.Write("null");
        }
        else if (array.Length == 0) {
            stringWriter.Write("[]");
        }
        else {
            stringWriter.Write("[");
            stringWriter.Write(array[0].ToString());

            for (int i = 1; i < array.Length; i++) {
                stringWriter.Write(",");
                stringWriter.Write(array[i].ToString());
            }

            stringWriter.Write("]");
        }
    }

    public static string ToJSONstring(double[] array) {
        StringWriter StringWriter = new StringWriter();

        try {
            WriteJSONString(array, StringWriter);
            return StringWriter.ToString();
        }
        catch (Exception e) {
            // This should never happen for a StringWriter
            throw new Exception(e.ToString());
        }
    }

    public static void WriteJSONString(bool[] array, StringWriter stringWriter) {
        if (array == null) {
            stringWriter.Write("null");
        }
        else if (array.Length == 0) {
            stringWriter.Write("[]");
        }
        else {
            stringWriter.Write("[");
            stringWriter.Write(array[0].ToString());

            for (int i = 1; i < array.Length; i++) {
                stringWriter.Write(",");
                stringWriter.Write(array[i].ToString());
            }

            stringWriter.Write("]");
        }
    }

    public static string ToJSONstring(bool[] array) {
        StringWriter StringWriter = new StringWriter();

        try {
            WriteJSONString(array, StringWriter);
            return StringWriter.ToString();
        }
        catch (Exception e) {
            // This should never happen for a StringWriter
            throw new Exception(e.ToString());
        }
    }

    public static void WriteJSONString(char[] array, StringWriter stringWriter) {
        if (array == null) {
            stringWriter.Write("null");
        }
        else if (array.Length == 0) {
            stringWriter.Write("[]");
        }
        else {
            stringWriter.Write("[\"");
            stringWriter.Write(array[0].ToString());

            for (int i = 1; i < array.Length; i++) {
                stringWriter.Write("\",\"");
                stringWriter.Write(array[i].ToString());
            }

            stringWriter.Write("\"]");
        }
    }

    public static string ToJSONstring(char[] array) {
        StringWriter StringWriter = new StringWriter();

        try {
            WriteJSONString(array, StringWriter);
            return StringWriter.ToString();
        }
        catch (Exception e) {
            // This should never happen for a StringWriter
            throw new Exception(e.ToString());
        }
    }

    public static void WriteJSONString(object[] array, StringWriter stringWriter) {
        if (array == null) {
            stringWriter.Write("null");
        }
        else if (array.Length == 0) {
            stringWriter.Write("[]");
        }
        else {
            stringWriter.Write("[");
            JSONValue.WriteJSONString(array[0], stringWriter);

            for (int i = 1; i < array.Length; i++) {
                stringWriter.Write(",");
                JSONValue.WriteJSONString(array[i], stringWriter);
            }

            stringWriter.Write("]");
        }
    }

    public static string ToJSONstring(object[] array) {
        StringWriter StringWriter = new StringWriter();

        try {
            WriteJSONString(array, StringWriter);
            return StringWriter.ToString();
        }
        catch (Exception e) {
            // This should never happen for a StringWriter
            throw new Exception(e.ToString());
        }
    }

    public string ToJSONString() {
        return ToJSONstring(this);
    }

    /**
     * Returns a string representation of this array. This is equivalent to
     * calling {@link JSONArray#ToJSONString()}.
     */
    override
    public string ToString() {
        return ToJSONString();
    }
}
