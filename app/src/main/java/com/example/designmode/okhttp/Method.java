package com.example.designmode.okhttp;

public enum Method {
    POST("POST"),
    GET("GET"),
    HEAD("HEAD"),
    DELETE("DELETE"),
    PUT("PUT"),
    PATCH("PATCH");

    public String name;
    Method (String name) {this.name = name;}

    public boolean doOutput() {
        switch (this) {
            case POST:
            case GET:
                return true;
        }
        return false;
    }
}
