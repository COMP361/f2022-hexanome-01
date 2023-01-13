using Ader.Text;
using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using UnityEditor.PackageManager;
using UnityEngine;
using UnityEngine.Analytics;

public class JSONParser {
    public const int S_INIT = 0;
    public const int S_IN_FINISHED_VALUE = 1;//string,number,bool,null,object,array
    public const int S_IN_OBJECT = 2;
    public const int S_IN_ARRAY = 3;
    public const int S_PASSED_PAIR_KEY = 4;
    public const int S_IN_PAIR_VALUE = 5;
    public const int S_END = 6;
    public const int S_IN_ERROR = -1;

    private LinkedList<int> handlerStatusStack;
    private Yylex lexer = new Yylex((StringReader)null);
    private Yytoken token = null;
    private int status = S_INIT;

    private int peekStatus(LinkedList<int> statusStack) {
        if (statusStack.Count == 0)
            return -1;
        return statusStack.First.Value;
    }

    /**
     *  Reset the parser to the initial state without resetting the underlying stringReader.
     *
     */
    public void Reset() {
        token = null;
        status = S_INIT;
        handlerStatusStack = null;
    }

    /**
     * Reset the parser to the initial state with a new character stringReader.
     * 
     * @param stringReader - The new character stringReader.
     * @throws IOException
     * @throws ParseException
     */
    public void Reset(StringReader stringReader) {
        lexer.yyreset(stringReader);
        Reset();
    }

    /**
	 * @return The position of the beginning of the current token.
	 */
    public int GetPosition() {
        return lexer.GetPosition();
    }

    public object Parse(string s) {
            return Parse(s, (IContainerFactory)null);
    }

    public object Parse(string s, IContainerFactory containerFactory) {
        StringReader stringReader = new StringReader(s);
        try {
            return Parse(stringReader, containerFactory);
        }
        catch (IOException ie) {
            /*
             * Actually it will never happen.
             */
            throw new ParseException(-1, ParseException.ERROR_UNEXPECTED_EXCEPTION, ie);
        }
    }

    public object Parse(StringReader stringReader) {
        return Parse(stringReader, (IContainerFactory)null);
    }

    /**
     * Parse JSON text into java object from the input source.
     * 	
     * @param stringReader
     * @param containerFactory - Use this factory to createyour own JSON object and JSON array containers.
     * @return Instance of the following:
     *  org.json.simple.JSONObject,
     * 	org.json.simple.JSONArray,
     * 	java.lang.string,
     * 	java.lang.Number,
     * 	java.lang.Boolean,
     * 	null
     * 
     * @throws IOException
     * @throws ParseException
     */
    public object Parse(StringReader stringReader, IContainerFactory containerFactory) {
        Reset(stringReader);
        LinkedList<int> statusStack = new LinkedList<int>();
        LinkedList<object> valueStack = new LinkedList<object>();

        try {
            do {
                NextToken();
                switch (status) {
                    case S_INIT:
                        switch (token.type) {
                            case Yytoken.TYPE_VALUE:
                                status = S_IN_FINISHED_VALUE;
                                statusStack.AddFirst(status);
                                valueStack.AddFirst(token.value);
                                break;
                            case Yytoken.TYPE_LEFT_BRACE:
                                status = S_IN_OBJECT;
                                statusStack.AddFirst(status);
                                valueStack.AddFirst(CreateObjectContainer(containerFactory));
                                break;
                            case Yytoken.TYPE_LEFT_SQUARE:
                                status = S_IN_ARRAY;
                                statusStack.AddFirst(status);
                                valueStack.AddFirst(CreateArrayContainer(containerFactory));
                                break;
                            default:
                                status = S_IN_ERROR;
                                break;
                        }//inner switch
                        break;

                    case S_IN_FINISHED_VALUE:
                        if (token.type == Yytoken.TYPE_EOF) {
                            object first = valueStack.First.Value;
                            valueStack.RemoveFirst();
                            return first;
                        }
                        else
                            throw new ParseException(GetPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);

                    case S_IN_OBJECT:
                        switch (token.type) {
                            case Yytoken.TYPE_COMMA:
                                break;
                            case Yytoken.TYPE_VALUE:
                                if (token.value is string) {
                                    string key = (string)token.value;
                                    valueStack.AddFirst(key);
                                    status = S_PASSED_PAIR_KEY;
                                    statusStack.AddFirst(status);
                                }

                                else {
                                    status = S_IN_ERROR;
                                }
                                break;
                            case Yytoken.TYPE_RIGHT_BRACE:
                                if (valueStack.Count > 1) {
                                    statusStack.RemoveFirst();
                                    valueStack.RemoveFirst();
                                    status = peekStatus(statusStack);
                                }
                                else {
                                    status = S_IN_FINISHED_VALUE;
                                }
                                break;
                            default:
                                status = S_IN_ERROR;
                                break;
                        }//inner switch
                        break;

                    case S_PASSED_PAIR_KEY:
                        switch (token.type) {
                            case Yytoken.TYPE_COLON:
                                break;
                            case Yytoken.TYPE_VALUE:
                                statusStack.RemoveFirst();
                                string key = (string)valueStack.First.Value;
                                valueStack.RemoveFirst();
                                IDictionary parent = (IDictionary)valueStack.First.Value;
                                parent.Add(key, token.value);
                                status = peekStatus(statusStack);
                                break;
                            case Yytoken.TYPE_LEFT_SQUARE:
                                statusStack.RemoveFirst();
                                key = (string)valueStack.First.Value;
                                valueStack.RemoveFirst();
                                parent = (IDictionary)valueStack.First.Value;
                                IList newArray = CreateArrayContainer(containerFactory);
                                parent.Add(key, newArray);
                                status = S_IN_ARRAY;
                                statusStack.AddFirst(status);
                                valueStack.AddFirst(newArray);
                                break;
                            case Yytoken.TYPE_LEFT_BRACE:
                                statusStack.RemoveFirst();
                                key = (string)valueStack.First.Value;
                                valueStack.RemoveFirst();
                                parent = (IDictionary)valueStack.First.Value;
                                IDictionary newObject = CreateObjectContainer(containerFactory);
                                parent.Add(key, newObject);
                                status = S_IN_OBJECT;
                                statusStack.AddFirst(status);
                                valueStack.AddFirst(newObject);
                                break;
                            default:
                                status = S_IN_ERROR;
                                break;
                        }
                        break;

                    case S_IN_ARRAY:
                        switch (token.type) {
                            case Yytoken.TYPE_COMMA:
                                break;
                            case Yytoken.TYPE_VALUE:
                                IList val = (IList)valueStack.First.Value;
                                val.Add(token.value);
                                break;
                            case Yytoken.TYPE_RIGHT_SQUARE:
                                if (valueStack.Count > 1) {
                                    statusStack.RemoveFirst();
                                    valueStack.RemoveFirst();
                                    status = peekStatus(statusStack);
                                }
                                else {
                                    status = S_IN_FINISHED_VALUE;
                                }
                                break;
                            case Yytoken.TYPE_LEFT_BRACE:
                                val = (IList)valueStack.First.Value;
                                IDictionary newObject = CreateObjectContainer(containerFactory);
                                val.Add(newObject);
                                status = S_IN_OBJECT;
                                statusStack.AddFirst(status);
                                valueStack.AddFirst(newObject);
                                break;
                            case Yytoken.TYPE_LEFT_SQUARE:
                                val = (IList)valueStack.First.Value;
                                IList newArray = CreateArrayContainer(containerFactory);
                                val.Add(newArray);
                                status = S_IN_ARRAY;
                                statusStack.AddFirst(status);
                                valueStack.AddFirst(newArray);
                                break;
                            default:
                                status = S_IN_ERROR;
                                break;
                        }//inner switch
                        break;
                    case S_IN_ERROR:
                        throw new ParseException(GetPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
                }//switch
                if (status == S_IN_ERROR) {
                    throw new ParseException(GetPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
                }
            } while (token.type != Yytoken.TYPE_EOF);
        }
        catch (IOException ie) {
            throw ie;
        }

        throw new ParseException(GetPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
    }

    private void NextToken() {

        token = lexer.yylex();
        if (token == null)
            token = new Yytoken(Yytoken.TYPE_EOF, null);
    }

    private IDictionary CreateObjectContainer(IContainerFactory containerFactory) {
        if (containerFactory == null)
            return new JSONObject(new Dictionary<object, object>());
        IDictionary m = containerFactory.CreateObjectContainer();

        if (m == null)
            return new JSONObject(new Dictionary<object, object>());
        return m;
    }

    private IList CreateArrayContainer(IContainerFactory containerFactory) {
        if (containerFactory == null)
            return new JSONArray();
        IList l = containerFactory.CreateArrayContainer();

        if (l == null)
            return new JSONArray();
        return l;
    }

    public void Parse(string s, IContentHandler contentHandler) {
        Parse(s, contentHandler, false);
    }

    public void Parse(string s, IContentHandler contentHandler, bool isResume) {
        StringReader stringReader = new StringReader(s);
        try {
            Parse(stringReader.ToString(), contentHandler, isResume);
        }
        catch (IOException ie) {
            /*
             * Actually it will never happen.
             */
            throw new ParseException(-1, ParseException.ERROR_UNEXPECTED_EXCEPTION, ie);
        }
    }

    public void Parse(StringReader stringReader, IContentHandler contentHandler) {
        Parse(stringReader, contentHandler, false);
    }

    /**
     * Stream processing of JSON text.
     * 
     * @see ContentHandler
     * 
     * @param stringReader
     * @param contentHandler
     * @param isResume - Indicates if it continues previous parsing operation.
     *                   If set to true, resume parsing the old stream, and parameter 'stringReader' will be ignored. 
     *                   If this method is called for the first time stringReader this instance, isResume will be ignored.
     * 
     * @throws IOException
     * @throws ParseException
     */
    public void Parse(StringReader stringReader, IContentHandler contentHandler, bool isResume) {
        if (!isResume) {
            Reset(stringReader);
            handlerStatusStack = new LinkedList<int>();
        }

        else {
            if (handlerStatusStack == null) {
                isResume = false;
                Reset(stringReader);
                handlerStatusStack = new LinkedList<int>();
            }
        }

        LinkedList<int> statusStack = handlerStatusStack;

        try {
            do {
                switch (status) {
                    case S_INIT:
                        contentHandler.StartJSON();
                        NextToken();
                        switch (token.type) {
                            case Yytoken.TYPE_VALUE:
                                status = S_IN_FINISHED_VALUE;
                                statusStack.AddFirst(status);
                                if (!contentHandler.Primitive(token.value))
                                    return;
                                break;
                            case Yytoken.TYPE_LEFT_BRACE:
                                status = S_IN_OBJECT;
                                statusStack.AddFirst(status);
                                if (!contentHandler.StartObject())
                                    return;
                                break;
                            case Yytoken.TYPE_LEFT_SQUARE:
                                status = S_IN_ARRAY;
                                statusStack.AddFirst(status);
                                if (!contentHandler.StartArray())
                                    return;
                                break;
                            default:
                                status = S_IN_ERROR;
                                break;
                        }//inner switch
                        break;

                    case S_IN_FINISHED_VALUE:
                        NextToken();
                        if (token.type == Yytoken.TYPE_EOF) {
                            contentHandler.EndJSON();
                            status = S_END;
                            return;
                        }
                        else {
                            status = S_IN_ERROR;
                            throw new ParseException(GetPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
                        }

                    case S_IN_OBJECT:
                        NextToken();
                        switch (token.type) {
                            case Yytoken.TYPE_COMMA:
                                break;
                            case Yytoken.TYPE_VALUE:
                                if (token.value is string) {
                                    string key = (string)token.value;
                                    status = S_PASSED_PAIR_KEY;
                                    statusStack.AddFirst(status);
                                    if (!contentHandler.StartObjectEntry(key))
                                        return;
                                }

                                else {
                                    status = S_IN_ERROR;
                                }
                                break;
                            case Yytoken.TYPE_RIGHT_BRACE:
                                if (statusStack.Count > 1) {
                                    statusStack.RemoveFirst();
                                    status = peekStatus(statusStack);
                                }
                                else {
                                    status = S_IN_FINISHED_VALUE;
                                }
                                if (!contentHandler.EndObject())
                                    return;
                                break;
                            default:
                                status = S_IN_ERROR;
                                break;
                        }//inner switch
                        break;

                    case S_PASSED_PAIR_KEY:
                        NextToken();
                        switch (token.type) {
                            case Yytoken.TYPE_COLON:
                                break;
                            case Yytoken.TYPE_VALUE:
                                statusStack.RemoveFirst();
                                status = peekStatus(statusStack);
                                if (!contentHandler.Primitive(token.value))
                                    return;
                                if (!contentHandler.EndObjectEntry())
                                    return;
                                break;
                            case Yytoken.TYPE_LEFT_SQUARE:
                                statusStack.RemoveFirst();
                                statusStack.AddFirst(S_IN_PAIR_VALUE);
                                status = S_IN_ARRAY;
                                statusStack.AddFirst(status);
                                if (!contentHandler.StartArray())
                                    return;
                                break;
                            case Yytoken.TYPE_LEFT_BRACE:
                                statusStack.RemoveFirst();
                                statusStack.AddFirst(S_IN_PAIR_VALUE);
                                status = S_IN_OBJECT;
                                statusStack.AddFirst(status);
                                if (!contentHandler.StartObject())
                                    return;
                                break;
                            default:
                                status = S_IN_ERROR;
                                break;
                        }
                        break;

                    case S_IN_PAIR_VALUE:
                        /*
                         * S_IN_PAIR_VALUE is just a marker to indicate the End of an object entry, it doesn't proccess any token,
                         * therefore delay consuming token until next round.
                         */
                        statusStack.RemoveFirst();
                        status = peekStatus(statusStack);
                        if (!contentHandler.EndObjectEntry())
                            return;
                        break;

                    case S_IN_ARRAY:
                        NextToken();
                        switch (token.type) {
                            case Yytoken.TYPE_COMMA:
                                break;
                            case Yytoken.TYPE_VALUE:
                                if (!contentHandler.Primitive(token.value))
                                    return;
                                break;
                            case Yytoken.TYPE_RIGHT_SQUARE:
                                if (statusStack.Count > 1) {
                                    statusStack.RemoveFirst();
                                    status = peekStatus(statusStack);
                                }
                                else {
                                    status = S_IN_FINISHED_VALUE;
                                }
                                if (!contentHandler.EndArray())
                                    return;
                                break;
                            case Yytoken.TYPE_LEFT_BRACE:
                                status = S_IN_OBJECT;
                                statusStack.AddFirst(status);
                                if (!contentHandler.StartObject())
                                    return;
                                break;
                            case Yytoken.TYPE_LEFT_SQUARE:
                                status = S_IN_ARRAY;
                                statusStack.AddFirst(status);
                                if (!contentHandler.StartArray())
                                    return;
                                break;
                            default:
                                status = S_IN_ERROR;
                                break;
                        }//inner switch
                        break;
                    case S_END:
                        return;

                    case S_IN_ERROR:
                        throw new ParseException(GetPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
                }//switch
                if (status == S_IN_ERROR) {
                    throw new ParseException(GetPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
                }
            } while (token.type != Yytoken.TYPE_EOF);
        }
        catch (IOException ie) {
            status = S_IN_ERROR;
            throw ie;
        }
        catch (ParseException pe) {
            status = S_IN_ERROR;
            throw pe;
        }
        catch (Exception e) {
            status = S_IN_ERROR;
            throw e;
        }

        status = S_IN_ERROR;
        throw new ParseException(GetPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
    }
}
