package com.example.designmode.glide.resource;

import com.example.designmode.glide.utils.Util;

public class Key {
    private String key;
    public Key(String path) {this.key = Util.getSHA256StrJava(key);}

    public String getKey() {return key;}

    public void setKey(String key) {this.key = key;}
}
