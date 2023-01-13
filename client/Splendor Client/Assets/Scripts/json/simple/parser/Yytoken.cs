using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.Text;

public class Yytoken
{
    public const int TYPE_VALUE = 0;//JSON primitive value: string,number,boolean,null
    public const int TYPE_LEFT_BRACE = 1;
    public const int TYPE_RIGHT_BRACE = 2;
    public const int TYPE_LEFT_SQUARE = 3;
    public const int TYPE_RIGHT_SQUARE = 4;
    public const int TYPE_COMMA = 5;
    public const int TYPE_COLON = 6;
    public const int TYPE_EOF = -1;//end of file

    public int type = 0;
    public object value = null;

    public Yytoken(int type, object value) {
        this.type = type;
        this.value = value;
    }

    override
    public string ToString() {
        StringBuilder sb = new StringBuilder();
        switch (type) {
            case TYPE_VALUE:
                sb.Append("VALUE(").Append(value).Append(")");
                break;
            case TYPE_LEFT_BRACE:
                sb.Append("LEFT BRACE({)");
                break;
            case TYPE_RIGHT_BRACE:
                sb.Append("RIGHT BRACE(})");
                break;
            case TYPE_LEFT_SQUARE:
                sb.Append("LEFT SQUARE([)");
                break;
            case TYPE_RIGHT_SQUARE:
                sb.Append("RIGHT SQUARE(])");
                break;
            case TYPE_COMMA:
                sb.Append("COMMA(,)");
                break;
            case TYPE_COLON:
                sb.Append("COLON(:)");
                break;
            case TYPE_EOF:
                sb.Append("END OF FILE");
                break;
        }
        return sb.ToString();
    }
}
