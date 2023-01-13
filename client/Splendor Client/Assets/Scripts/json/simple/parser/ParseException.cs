using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.Text;

public class ParseException : Exception
{
    private const long serialVersionUID = -7880698968187728547L;

    public const int ERROR_UNEXPECTED_CHAR = 0;
    public const int ERROR_UNEXPECTED_TOKEN = 1;
    public const int ERROR_UNEXPECTED_EXCEPTION = 2;

    private int errorType;
    private object unexpectedObject;
    private int position;

    public ParseException(int errorType) : this(-1, errorType, null) { }

    public ParseException(int errorType, object unexpectedObject) : this(-1, errorType, unexpectedObject) { }

    public ParseException(int position, int errorType, object unexpectedObject) {
        this.position = position;
        this.errorType = errorType;
        this.unexpectedObject = unexpectedObject;
    }

    public int GetErrorType() {
        return errorType;
    }

    public void SetErrorType(int errorType) {
        this.errorType = errorType;
    }

    /**
	 * @see org.json.simple.parser.JSONParser#GetPosition()
	 * 
	 * @return The character position (starting with 0) of the input where the error occurs.
	 */
    public int GetPosition() {
        return position;
    }

    public void SetPosition(int position) {
        this.position = position;
    }

    /**
	 * @see org.json.simple.parser.Yytoken
	 * 
	 * @return One of the following base on the value of errorType:
	 * 		   	ERROR_UNEXPECTED_CHAR		java.lang.Character
	 * 			ERROR_UNEXPECTED_TOKEN		org.json.simple.parser.Yytoken
	 * 			ERROR_UNEXPECTED_EXCEPTION	java.lang.Exception
	 */
    public object GetUnexpectedObject() {
        return unexpectedObject;
    }

    public void SetUnexpectedObject(object unexpectedObject) {
        this.unexpectedObject = unexpectedObject;
    }

    public string GetMessage() {
        StringBuilder sb = new StringBuilder();

        switch (errorType) {
            case ERROR_UNEXPECTED_CHAR:
                sb.Append("Unexpected character (").Append(unexpectedObject).Append(") at position ").Append(position).Append(".");
                break;
            case ERROR_UNEXPECTED_TOKEN:
                sb.Append("Unexpected token ").Append(unexpectedObject).Append(" at position ").Append(position).Append(".");
                break;
            case ERROR_UNEXPECTED_EXCEPTION:
                sb.Append("Unexpected exception at position ").Append(position).Append(": ").Append(unexpectedObject);
                break;
            default:
                sb.Append("Unkown error at position ").Append(position).Append(".");
                break;
        }
        return sb.ToString();
    }
}
