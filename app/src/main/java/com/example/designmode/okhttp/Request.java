package com.example.designmode.okhttp;

import java.util.HashMap;
import java.util.Map;

public class Request {
    final String url;
    final Method method;
    final Map<String, String> headers;

    private Request(Bulider bulider) {
        this.url = bulider.url;
        this.method = bulider.method;
        this.headers = bulider.headers;
    }

    public static class Bulider {
        String url;
        Method method;
        Map<String, String> headers;
        public Bulider url(String url) {
            this.url = url;
            return this;
        }

        public Bulider() {
            method = Method.GET;
            headers = new HashMap<>();
        }

        public Bulider get() {
            method = Method.GET;
            return this;
        }

        public Bulider post() {
            method = Method.POST;
            return this;
        }

        public Bulider headers(String key, String val) {
            headers.put(key, val);
            return this;
        }


        public Request bulid() {return new Request(this);}
    }
}
