using Ader.Text;
using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.Text;
using System.IO;
using System.Globalization;

public class Yylex {

    /** This character denotes the end of file */
    public const int YYEOF = -1;

    /** initial size of the lookahead buffer */
    private const int ZZ_BUFFERSIZE = 16384;

    /** lexical states */
    public const int YYINITIAL = 0;
    public const int STRING_BEGIN = 2;

    /**
     * ZZ_LEXSTATE[l] is the state input the DFA for the lexical state l
     * ZZ_LEXSTATE[l+1] is the state input the DFA for the lexical state l
     *                  at the beginning of a line
     * l is of the form l = 2*k, k a non negative integer
     */
    private int[] ZZ_LEXSTATE = { 0, 0, 1, 1 };

    /** 
     * Translates characters to character classes
     */
    private static string ZZ_CMAP_PACKED =
    "\u0009\u0000\u0001\u0007\u0001\u0007\u0002\u0000\u0001\u0007\u0012\u0000\u0001\u0007\u0001\u0000\u0001\u0009\u0008\u0000" +
    "\u0001\u0006\u0001\u0019\u0001\u0002\u0001\u0004\u0001\u000a\u000a\u0003\u0001\u001a\u0006\u0000\u0004\u0001\u0001\u0005" +
    "\u0001\u0001\u0014\u0000\u0001\u0017\u0001\u0008\u0001\u0018\u0003\u0000\u0001\u0012\u0001\u000b\u0002\u0001\u0001\u0011" +
    "\u0001\u000c\u0005\u0000\u0001\u0013\u0001\u0000\u0001\u000d\u0003\u0000\u0001\u000e\u0001\u0014\u0001\u000f\u0001\u0010" +
    "\u0005\u0000\u0001\u0015\u0001\u0000\u0001\u0016\uff82\u0000";

    /** 
     * Translates characters to character classes
     */
    private static char[] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

    /** 
     * Translates DFA states to action switch labels.
     */
    private static int[] ZZ_ACTION = zzUnpackAction();

    private const string ZZ_ACTION_PACKED_0 =
    "\u0002\u0000\u0002\u0001\u0001\u0002\u0001\u0003\u0001\u0004\u0003\u0001\u0001\u0005" +
        "\u0001\u0006\u0001\u0007\u0001\u0008\u0001\u0009\u0001\u000a\u0001\u000b\u0001" +
        "\u000c\u0001\u000d\u0005\u0000\u0001\u000c\u0001\u000e\u0001\u000f\u0001\u0010" +
        "\u0001\u0011\u0001\u0012\u0001\u0013\u0001\u0014\u0001\u0000\u0001\u0015" +
        "\u0001\u0000\u0001\u0015\u0004\u0000\u0001\u0016\u0001\u0017\u0002\u0000\u0001\u0018";

    private static int[] zzUnpackAction() {
        int[] result = new int[45];
        int offset = 0;
        offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
        return result;
    }

    private static int zzUnpackAction(string packed, int offset, int[] result) {
        int i = 0;       /* index input packed string  */
        int j = offset;  /* index input unpacked array */
        int l = packed.Length;
        while (i < l) {
            int count = packed[i++];
            int value = packed[i++];
            do result[j++] = value; while (--count > 0);
        }
        return j;
    }


    /** 
     * Translates a state to a row index input the transition table
     */
    private int[] ZZ_ROWMAP = zzUnpackRowMap();

    private static string ZZ_ROWMAP_PACKED_0 =
    "\u0000\u0000\u0000\u001b\u0000\u0036\u0000\u0051\u0000\u006c\u0000\u0087\u0000\u0036\u0000\u00a2" +
    "\u0000\u00bd\u0000\u00d8\u0000\u0036\u0000\u0036\u0000\u0036\u0000\u0036\u0000\u0036\u0000\u0036" +
    "\u0000\u00f3\u0000\u010e\u0000\u0036\u0000\u0129\u0000\u0144\u0000\u015f\u0000\u017a\u0000\u0195" +
    "\u0000\u0036\u0000\u0036\u0000\u0036\u0000\u0036\u0000\u0036\u0000\u0036\u0000\u0036\u0000\u0036" +
    "\0\u01b0\u0000\u01cb\u0000\u01e6\0\u01e6\u0000\u0201\u0000\u021c\u0000\u0237\0\u0252" +
    "\u0000\u0036\u0000\u0036\u0000\u016d\u0000\u0288\u0000\u0036";

    private static int[] zzUnpackRowMap() {
        int[] result = new int[45];
        int offset = 0;
        offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
        return result;
    }

    private static int zzUnpackRowMap(string packed, int offset, int[] result) {
        int i = 0;  /* index input packed string  */
        int j = offset;  /* index input unpacked array */
        int l = packed.Length;
        while (i < l) {
            int high = packed[i++] << 16;
            result[j++] = high | packed[i++];
        }
        return j;
    }

    /** 
     * The transition table of the DFA
     */
    private static int[] ZZ_TRANS = {
    2, 2, 3, 4, 2, 2, 2, 5, 2, 6,
    2, 2, 7, 8, 2, 9, 2, 2, 2, 2,
    2, 10, 11, 12, 13, 14, 15, 16, 16, 16,
    16, 16, 16, 16, 16, 17, 18, 16, 16, 16,
    16, 16, 16, 16, 16, 16, 16, 16, 16, 16,
    16, 16, 16, 16, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, 4, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, 4, 19, 20, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, 20, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, 5, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    21, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, 22, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    23, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, 16, 16, 16, 16, 16, 16, 16,
    16, -1, -1, 16, 16, 16, 16, 16, 16, 16,
    16, 16, 16, 16, 16, 16, 16, 16, 16, 16,
    -1, -1, -1, -1, -1, -1, -1, -1, 24, 25,
    26, 27, 28, 29, 30, 31, 32, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    33, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, 34, 35, -1, -1,
    34, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    36, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, 37, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, 38, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, 39, -1, 39, -1, 39, -1, -1,
    -1, -1, -1, 39, 39, -1, -1, -1, -1, 39,
    39, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, 33, -1, 20, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, 20, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, 35,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, 38, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, 40,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, 41, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, 42, -1, 42, -1, 42,
    -1, -1, -1, -1, -1, 42, 42, -1, -1, -1,
    -1, 42, 42, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, 43, -1, 43, -1, 43, -1, -1, -1,
    -1, -1, 43, 43, -1, -1, -1, -1, 43, 43,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, 44,
    -1, 44, -1, 44, -1, -1, -1, -1, -1, 44,
    44, -1, -1, -1, -1, 44, 44, -1, -1, -1,
    -1, -1, -1, -1, -1,
  };

    /* error codes */
    private const int ZZ_UNKNOWN_ERROR = 0;
    private const int ZZ_NO_MATCH = 1;
    private const int ZZ_PUSHBACK_2BIG = 2;

    /* error messages for the codes above */
    private string[] ZZ_ERROR_MSG = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

    /**
     * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
     */
    private int[] ZZ_ATTRIBUTE = zzUnpackAttribute();

    private static string ZZ_ATTRIBUTE_PACKED_0 =
    "\u0002\u0000\u0001\u0009\u0003\u0001\u0001\u0009\u0003\u0001\u0006\u0009\u0002\u0001\u0001\u0009" +
    "\u0005\u0000\u0008\u0009\u0001\u0000\u0001\u0001\u0001\u0000\u0001\u0001\u0004\u0000\u0002\u0009" +
    "\u0002\u0000\u0001\u0009";

    private static int[] zzUnpackAttribute() {
        int[] result = new int[45];
        int offset = 0;
        offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
        return result;
    }

    private static int zzUnpackAttribute(string packed, int offset, int[] result) {
        int i = 0;       /* index input packed string  */
        int j = offset;  /* index input unpacked array */
        int l = packed.Length;
        while (i < l) {
            int count = packed[i++];
            int value = packed[i++];
            do result[j++] = value; while (--count > 0);
        }
        return j;
    }

    /** the input device */
    private StringReader zzReader;

    /** the current state of the DFA */
    private int zzState;

    /** the current lexical state */
    private int zzLexicalState = YYINITIAL;

    /** this buffer contains the current text to be matched and is
        the source of the yytext() string */
    private char[] zzBuffer = new char[ZZ_BUFFERSIZE];

    /** the textposition at the last accepting state */
    private int zzMarkedPos;

    /** the current text position input the buffer */
    private int zzCurrentPos;

    /** startRead marks the beginning of the yytext() string input the buffer */
    private int zzStartRead;

    /** endRead marks the last character input the buffer, that has been Read
        from input */
    private int zzEndRead;

    /** number of newlines encountered up to the start of the matched text */
    private int yyline;

    /** the number of characters up to the start of the matched text */
    private int yychar;

    /**
     * the number of characters from the last newline up to the start of the 
     * matched text
     */
    private int yycolumn;

    /** 
     * zzAtBOL == true <=> the scanner is currently at the beginning of a line
     */
    private bool zzAtBOL = true;

    /** zzAtEOF == true <=> the scanner is at the EOF */
    private bool zzAtEOF;

    /* user code: */
    private StringBuilder sb = new StringBuilder();

    public int GetPosition() {
        return yychar;
    }



    /**
     * Creates a new scanner
     * There is also a java.io.InputStream version of this constructor.
     *
     * @param   input  the StringReader to Read input from.
     */
    public Yylex(StringReader input) {
        this.zzReader = input;
    }

    /**
     * Creates a new scanner.
     * There is also StringReader version of this constructor.
     *
     * @param   input  the java.io.Inputstream to Read input from.
     */
    //Yylex(Stream input) : this(new StreamReader(input)){ }

    /** 
     * Unpacks the compressed character translation table.
     *
     * @param packed   the packed character translation table
     * @return         the unpacked character translation table
     */
    private static char[] zzUnpackCMap(string packed) {
        char[] map = new char[0x10000];
        int i = 0;  /* index input packed string  */
        int j = 0;  /* index input unpacked array */
        while (i < 90) {
            int count = packed[i++];
            char value = packed[i++];
            do map[j++] = value; while (--count > 0);
        }
        return map;
    }


    /**
     * Refills the input buffer.
     *
     * @return      <code>false</code>, iff there was new input.
     * 
     * @exception   java.io.IOException  if any I/O-Error occurs
     */
    private bool zzRefill() {

        /* first: make room (if you can) */
        if (zzStartRead > 0) {
            Array.Copy(zzBuffer, zzStartRead,
                             zzBuffer, 0,
                             zzEndRead - zzStartRead);

            /* translate stored positions */
            zzEndRead -= zzStartRead;
            zzCurrentPos -= zzStartRead;
            zzMarkedPos -= zzStartRead;
            zzStartRead = 0;
        }

        /* is the buffer big enough? */
        if (zzCurrentPos >= zzBuffer.Length) {
            /* if not: blow it up */
            char[] newBuffer = new char[zzCurrentPos * 2];
            Array.Copy(zzBuffer, 0, newBuffer, 0, zzBuffer.Length);
            zzBuffer = newBuffer;
        }

        /* constly: fill the buffer with new input */
        int numRead = zzReader.Read(zzBuffer, zzEndRead,
                                                zzBuffer.Length - zzEndRead);

        if (numRead > 0) {
            zzEndRead += numRead;
            return false;
        }
        // unlikely but not impossible: Read 0 characters, but not at end of stream    
        if (numRead == 0) {
            int c = zzReader.Read();
            if (c == -1) {
                return true;
            }
            else {
                zzBuffer[zzEndRead++] = (char)c;
                return false;
            }
        }

        // numRead < 0
        return true;
    }


    /**
     * Closes the input stream.
     */
    public void yyclose() {
        zzAtEOF = true;            /* indicate end of file */
        zzEndRead = zzStartRead;  /* invalidate buffer    */

        if (zzReader != null)
            zzReader.Close();
    }


    /**
     * Resets the scanner to Read from a new input stream.
     * Does not Close the old reader.
     *
     * All internal variables are reset, the old input stream 
     * <b>cannot</b> be reused (internal buffer is discarded and lost).
     * Lexical state is set to <tt>ZZ_INITIAL</tt>.
     *
     * @param reader   the new input stream 
     */
    public void yyreset(StringReader reader) {
        zzReader = reader;
        zzAtBOL = true;
        zzAtEOF = false;
        zzEndRead = zzStartRead = 0;
        zzCurrentPos = zzMarkedPos = 0;
        yyline = yychar = yycolumn = 0;
        zzLexicalState = YYINITIAL;
    }


    /**
     * Returns the current lexical state.
     */
    public int yystate() {
        return zzLexicalState;
    }


    /**
     * Enters a new lexical state
     *
     * @param newState the new lexical state
     */
    public void yybegin(int newState) {
        zzLexicalState = newState;
    }


    /**
     * Returns the text matched by the current regular expression.
     */
    public string yytext() {
        return new string(zzBuffer, zzStartRead, zzMarkedPos - zzStartRead);
    }


    /**
     * Returns the character at position <tt>pos</tt> from the 
     * matched text. 
     * 
     * It is equivalent to yytext().charAt(pos), but faster
     *
     * @param pos the position of the character to fetch. 
     *            A value from 0 to yyLength-1.
     *
     * @return the character at position pos
     */
    public char yycharat(int pos) {
        return zzBuffer[zzStartRead + pos];
    }


    /**
     * Returns the length of the matched text region.
     */
    public int yyLength() {
        return zzMarkedPos - zzStartRead;
    }


    /**
     * Reports an error that occured while scanning.
     *
     * In a wellformed scanner (no or only correct usage of 
     * yypushback(int) and a match-all fallback rule) this method 
     * will only be called with things that "Can't Possibly Happen".
     * If this method is called, something is seriously wrong
     * (e.g. a JFlex bug producing a faulty scanner etc.).
     *
     * Usual syntax/scanner level error handling should be done
     * input error fallback rules.
     *
     * @param   errorCode  the code of the errormessage to display
     */
    private void zzScanError(int errorCode) {
        string message;
        try {
            message = ZZ_ERROR_MSG[errorCode];
        }
        catch {
            message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
        }

        throw new Exception(message);
    }


    /**
     * Pushes the specified amount of characters back into the input stream.
     *
     * They will be Read again by then next call of the scanning method
     *
     * @param number  the number of characters to be Read again.
     *                This number must not be greater than yyLength!
     */
    public void yypushback(int number) {
        if (number > yyLength())
            zzScanError(ZZ_PUSHBACK_2BIG);

        zzMarkedPos -= number;
    }


    /**
     * Resumes scanning until the next regular expression is matched,
     * the end of input is encountered or an I/O-Error occurs.
     *
     * @return      the next token
     * @exception   java.io.IOException  if any I/O-Error occurs
     */
    public Yytoken yylex() {
        int zzInput;
        int zzAction;

        // cached fields:
        int zzCurrentPosL;
        int zzMarkedPosL;
        int zzEndReadL = zzEndRead;
        char[] zzBufferL = zzBuffer;
        char[] zzCMapL = ZZ_CMAP;

        int[] zzTransL = ZZ_TRANS;
        int[] zzRowMapL = ZZ_ROWMAP;
        int[] zzAttrL = ZZ_ATTRIBUTE;

        while (true) {
            zzMarkedPosL = zzMarkedPos;

            yychar += zzMarkedPosL - zzStartRead;

            zzAction = -1;

            zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

            zzState = ZZ_LEXSTATE[zzLexicalState];


        zzForAction: {
                while (true) {

                    if (zzCurrentPosL < zzEndReadL)
                        zzInput = zzBufferL[zzCurrentPosL++];
                    else if (zzAtEOF) {
                        zzInput = YYEOF;
                        break;
                    }
                    else {
                        // store back cached positions
                        zzCurrentPos = zzCurrentPosL;
                        zzMarkedPos = zzMarkedPosL;
                        bool eof = zzRefill();
                        // Get translated positions and possibly new buffer
                        zzCurrentPosL = zzCurrentPos;
                        zzMarkedPosL = zzMarkedPos;
                        zzBufferL = zzBuffer;
                        zzEndReadL = zzEndRead;
                        if (eof) {
                            zzInput = YYEOF;
                            break;
                        }
                        else {
                            zzInput = zzBufferL[zzCurrentPosL++];
                        }
                    }
                    int zzNext = zzTransL[zzRowMapL[zzState] + zzCMapL[zzInput]];
                    if (zzNext == -1) 
                        break;
                    zzState = zzNext;

                    int zzAttributes = zzAttrL[zzState];
                    if ((zzAttributes & 1) == 1) {
                        zzAction = zzState;
                        zzMarkedPosL = zzCurrentPosL;
                        if ((zzAttributes & 8) == 8) 
                                break;
                    }

                }
            }

            // store back cached position
            zzMarkedPos = zzMarkedPosL;

            switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
                case 11: {
                        sb.Append(yytext()); break;
                    }
                case 25: break;
                case 4: {
                        sb = null; sb = new StringBuilder(); yybegin(STRING_BEGIN); break;
                    }
                case 26: break;
                case 16: {
                        sb.Append('\b'); break;
                    }
                case 27: break;
                case 6: {
                        return new Yytoken(Yytoken.TYPE_RIGHT_BRACE, null);
                    }
                case 28: break;
                case 23: {
                        bool val = bool.Parse(yytext()); return new Yytoken(Yytoken.TYPE_VALUE, val);
                    }
                case 29: break;
                case 22: {
                        return new Yytoken(Yytoken.TYPE_VALUE, null);
                    }
                case 30: break;
                case 13: {
                        yybegin(YYINITIAL); return new Yytoken(Yytoken.TYPE_VALUE, sb.ToString());
                    }
                case 31: break;
                case 12: {
                        sb.Append('\\'); break;
                    }
                case 32: break;
                case 21: {
                        double val = double.Parse(yytext()); return new Yytoken(Yytoken.TYPE_VALUE, val);
                    }
                case 33: break;
                case 1: {
                        throw new ParseException(yychar, ParseException.ERROR_UNEXPECTED_CHAR, yycharat(0));
                    }
                case 34: break;
                case 8: {
                        return new Yytoken(Yytoken.TYPE_RIGHT_SQUARE, null);
                    }
                case 35: break;
                case 19: {
                        sb.Append('\r'); break;
                    }
                case 36: break;
                case 15: {
                        sb.Append('/'); break;
                    }
                case 37: break;
                case 10: {
                        return new Yytoken(Yytoken.TYPE_COLON, null);
                    }
                case 38: break;
                case 14: {
                        sb.Append('"'); break;
                    }
                case 39: break;
                case 5: {
                        return new Yytoken(Yytoken.TYPE_LEFT_BRACE, null);
                    }
                case 40: break;
                case 17: {
                        sb.Append('\f'); break;
                    }
                case 41: break;
                case 24: {
                        try {
                            int ch = int.Parse(yytext().Substring(2), NumberStyles.HexNumber);
                            sb.Append((char)ch);
                        }
                        catch (Exception e) {
                            throw new ParseException(yychar, ParseException.ERROR_UNEXPECTED_EXCEPTION, e);
                        }
                        break;
                    }
                case 42: break;
                case 20: {
                        sb.Append('\t'); break;
                    }
                case 43: break;
                case 7: {
                        return new Yytoken(Yytoken.TYPE_LEFT_SQUARE, null);
                    }
                case 44: break;
                case 2: {
                        long val = long.Parse(yytext()); return new Yytoken(Yytoken.TYPE_VALUE, val);
                    }
                case 45: break;
                case 18: {
                        sb.Append('\n'); break;
                    }
                case 46: break;
                case 9: {
                        return new Yytoken(Yytoken.TYPE_COMMA, null);
                    }
                case 47: break;
                case 3: {
                        break;
                    }
                case 48: break;
                default:
                    if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
                        zzAtEOF = true;
                        return null;
                    }
                    else {
                        zzScanError(ZZ_NO_MATCH);
                    }
                    break;
            }
        }
    }


}
