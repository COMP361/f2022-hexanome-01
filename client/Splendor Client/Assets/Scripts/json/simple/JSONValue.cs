using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using UnityEngine;
using UnityEngine.Analytics;
using System.Text;

public class JSONValue {
    /**
	 * Parse JSON text into java object from the input source. 
	 * Please use parseWithException() if you don't want to ignore the exception.
	 * 
	 * @see org.json.simple.parser.JSONParser#Parse(StringReader)
	 * @see #parseWithException(StringReader)
	 * 
	 * @param stringReader
	 * @return Instance of the following:
	 *	org.json.simple.JSONObject,
	 * 	org.json.simple.JSONArray,
	 * 	java.lang.string,
	 * 	java.lang.Number,
	 * 	java.lang.Boolean,
	 * 	null
	 * 
	 * @deprecated this method may throw an {@code Error} instead of returning
	 * {@code null}; please use {@link JSONValue#parseWithException(StringReader)}
	 * instead
	 */
    public static object Parse(StringReader stringReader) {
        try {
            JSONParser parser = new JSONParser();
            return parser.Parse(stringReader);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
	 * Parse JSON text into java object from the given string. 
	 * Please use parseWithException() if you don't want to ignore the exception.
	 * 
	 * @see org.json.simple.parser.JSONParser#Parse(StringReader)
	 * @see #parseWithException(StringReader)
	 * 
	 * @param s
	 * @return Instance of the following:
	 *	org.json.simple.JSONObject,
	 * 	org.json.simple.JSONArray,
	 * 	java.lang.string,
	 * 	java.lang.Number,
	 * 	java.lang.Boolean,
	 * 	null
	 * 
	 * @deprecated this method may throw an {@code Error} instead of returning
	 * {@code null}; please use {@link JSONValue#parseWithException(string)}
	 * instead
	 */
    public static object Parse(string s) {
        StringReader stringReader = new StringReader(s);
        return Parse(stringReader);
    }

    /**
	 * Parse JSON text into java object from the input source.
	 * 
	 * @see org.json.simple.parser.JSONParser
	 * 
	 * @param stringReader
	 * @return Instance of the following:
	 * 	org.json.simple.JSONObject,
	 * 	org.json.simple.JSONArray,
	 * 	java.lang.string,
	 * 	java.lang.Number,
	 * 	java.lang.Boolean,
	 * 	null
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
    public static object ParseWithException(StringReader stringReader) {

        JSONParser parser = new JSONParser();
        return parser.Parse(stringReader);
    }

    public static object ParseWithException(string s) {
        JSONParser parser = new JSONParser();
        return parser.Parse(s);
    }

    /**
     * Encode an object into JSON text and Write it to stringWriter.
     * <p>
     * If this object is a Map or a List, and it's also a JSONStreamAware or a JSONAware, JSONStreamAware or JSONAware will be considered firstly.
     * <p>
     * DO NOT call this method from WriteJSONString(StringWriter) of a class that implements both JSONStreamAware and (Map or List) with 
     * "this" as the first parameter, use JSONObject.WriteJSONString(Map, StringWriter) or JSONArray.WriteJSONString(List, StringWriter) instead. 
     * 
     * @see org.json.simple.JSONObject#WriteJSONString(Map, StringWriter)
     * @see org.json.simple.JSONArray#WriteJSONString(List, StringWriter)
     * 
     * @param value
     * @param writer
     */
    public static void WriteJSONString(object value, StringWriter stringWriter) {
        if (value == null) {
            stringWriter.Write("null");
            return;
        }

        if (value is string) {
            stringWriter.Write('\"');
            stringWriter.Write(Escape((string)value));
            stringWriter.Write('\"');
            return;
        }

        if (value is double) {
            if (double.IsInfinity((double)value) || double.IsNaN((double)value))
                stringWriter.Write("null");

            else
                stringWriter.Write(value.ToString());
            return;
        }

        if (value is float) {
            if (float.IsInfinity((float)value) || float.IsNaN((float)value))
                stringWriter.Write("null");

            else
                stringWriter.Write(value.ToString());
            return;
        }

        if (value is sbyte
            || value is byte
            || value is short
            || value is ushort
            || value is int
            || value is uint
            || value is long
            || value is ulong) {
            stringWriter.Write(value.ToString());
            return;
        }

        if (value is bool) {
            stringWriter.Write(value.ToString());
            return;
        }

        if (value is IJSONStreamAware) {
            ((IJSONStreamAware)value).WriteJSONString(stringWriter);
            return;
        }

        if (value is IJSONAware) {
            stringWriter.Write(((IJSONAware)value).ToJSONString());
            return;
        }

        if (value is IDictionary) {
            JSONObject.WriteJSONString((IDictionary)value, stringWriter);
            return;
        }

        if (value is ICollection) {
            JSONArray.WriteJSONString((ICollection)value, stringWriter);
            return;
        }

        if (value is byte[]) {
            JSONArray.WriteJSONString((byte[])value, stringWriter);
            return;
        }

        if (value is short[]) {
            JSONArray.WriteJSONString((short[])value, stringWriter);
            return;
        }

        if (value is int[]) {
            JSONArray.WriteJSONString((int[])value, stringWriter);
            return;
        }

        if (value is long[]) {
            JSONArray.WriteJSONString((long[])value, stringWriter);
            return;
        }

        if (value is float[]) {
            JSONArray.WriteJSONString((float[])value, stringWriter);
            return;
        }

        if (value is double[]) {
            JSONArray.WriteJSONString((double[])value, stringWriter);
            return;
        }

        if (value is bool[]) {
            JSONArray.WriteJSONString((bool[])value, stringWriter);
            return;
        }

        if (value is char[]) {
            JSONArray.WriteJSONString((char[])value, stringWriter);
            return;
        }

        if (value is object[]) {
            JSONArray.WriteJSONString((object[])value, stringWriter);
            return;
        }

        stringWriter.Write(value.ToString());
    }

    /**
     * Convert an object to JSON text.
     * <p>
     * If this object is a Map or a List, and it's also a JSONAware, JSONAware will be considered firstly.
     * <p>
     * DO NOT call this method from ToJSONString() of a class that implements both JSONAware and Map or List with 
     * "this" as the parameter, use JSONObject.ToJSONString(Map) or JSONArray.ToJSONString(List) instead. 
     * 
     * @see org.json.simple.JSONObject#ToJSONString(Map)
     * @see org.json.simple.JSONArray#ToJSONString(List)
     * 
     * @param value
     * @return JSON text, or "null" if value is null or it's an NaN or an INF number.
     */
    public static string ToJSONString(object value) {
        StringWriter writer = new StringWriter();

        try {
            WriteJSONString(value, writer);
            return writer.ToString();
        }
        catch (IOException e) {
            // This should never happen for a StringWriter
            throw new Exception(e.ToString());
        }
    }

    /**
     * Escape quotes, \, /, \r, \n, \b, \f, \t and other control characters (U+0000 through U+001F).
     * @param s
     * @return
     */
    public static string Escape(string s) {
        if (s == null)
            return null;
        StringBuilder sb = new StringBuilder();
        Escape(s, sb);
        return sb.ToString();
    }

    /**
     * @param s - Must not be null.
     * @param sb
     */
    public static void Escape(string s, StringBuilder sb) {
        int len = s.Length;
        for (int i = 0; i < len; i++) {
            char ch = s[i];
            switch (ch) {
                case '"':
                    sb.Append("\\\"");
                    break;
                case '\\':
                    sb.Append("\\\\");
                    break;
                case '\b':
                    sb.Append("\\b");
                    break;
                case '\f':
                    sb.Append("\\f");
                    break;
                case '\n':
                    sb.Append("\\n");
                    break;
                case '\r':
                    sb.Append("\\r");
                    break;
                case '\t':
                    sb.Append("\\t");
                    break;
                case '/':
                    sb.Append("\\/");
                    break;
                default:
                    //Reference: http://www.unicode.org/versions/Unicode5.1.0/
                    if ((ch >= '\u0000' && ch <= '\u001F') || (ch >= '\u007F' && ch <= '\u009F') || (ch >= '\u2000' && ch <= '\u20FF')) {
                        string ss = ((int)ch).ToString("X");
                        sb.Append("\\u");
                        for (int k = 0; k < 4 - ss.Length; k++) {
                            sb.Append('0');
                        }
                        sb.Append(ss.ToUpper());
                    }
                    else {
                        sb.Append(ch);
                    }
                    break;
            }
        }//for
    }
}
