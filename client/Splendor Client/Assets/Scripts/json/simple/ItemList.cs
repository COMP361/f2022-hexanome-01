using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.Text;
using Ader.Text;

public class ItemList {
    private string sp = ",";
    IList items = new ArrayList();


    public ItemList() { }


    public ItemList(string s) {
        this.Split(s, sp, items);
    }

    public ItemList(string s, string sp) {
        this.sp = s;
        this.Split(s, sp, items);
    }

    public ItemList(string s, string sp, bool isMultiToken) {
        Split(s, sp, items, isMultiToken);
    }

    public IList GetItems() {
        return items;
    }

    public string[] GetArray() {
        return (string[])((ArrayList)items).ToArray(typeof(string));
    }

    public void Split(string s, string sp, IList Append, bool isMultiToken) {
        if (s == null || sp == null)
            return;
        if (isMultiToken) {
            StringTokenizer tokens = new StringTokenizer(s);
            Token current = tokens.Next();
            while (current.Value != "") {
                if(current.Value != sp)
                    Append.Add(current.Value.Trim());
                current = tokens.Next();
            }
        }
        else {
            this.Split(s, sp, Append);
        }
    }

    public void Split(string s, string sp, IList Append) {
        if (s == null || sp == null)
            return;
        int pos = 0;
        int prevPos = 0;
        do {
            prevPos = pos;
            pos = s.IndexOf(sp, pos);
            if (pos == -1)
                break;
            Append.Add(s.Substring(prevPos, pos).Trim());
            pos += sp.Length;
        } while (pos != -1);
        Append.Add(s.Substring(prevPos).Trim());
    }

    public void SetSP(string sp) {
        this.sp = sp;
    }

    public void Add(int i, string item) {
        if (item == null)
            return;
        items.Insert(i, item.Trim());
    }

    public void Add(string item) {
        if (item == null)
            return;
        items.Add(item.Trim());
    }

    public void AddAll(ItemList list) {
        items.Add(list.items);
    }

    public void AddAll(string s) {
        this.Split(s, sp, items);
    }

    public void AddAll(string s, string sp) {
        this.Split(s, sp, items);
    }

    public void AddAll(string s, string sp, bool isMultiToken) {
        this.Split(s, sp, items, isMultiToken);
    }

    /**
	 * @param i 0-based
	 * @return
	 */
    public string Get(int i) {
        return (string)items[i];
    }

    public int Size() {
        return items.Count;
    }

    override
    public string ToString() {
        return ToString(sp);
    }

    public string ToString(string sp) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < items.Count; i++) {
            if (i == 0)
                sb.Append(items[i]);
            else {
                sb.Append(sp);
                sb.Append(items[i]);
            }
        }
        return sb.ToString();

    }

    public void Clear() {
        items.Clear();
    }

    public void Reset() {
        sp = ",";
        items.Clear();
    }
}
