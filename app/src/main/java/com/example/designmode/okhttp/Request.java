package com.example.designmode.okhttp;

import java.util.HashMap;
import java.util.Map;

public class Request {

    final String url;
    final Method method;
    final Map<String, String> headers;
    final RequestBody requestBody;


    private Request(Bulider bulider) {
        this.url = bulider.url;
        this.method = bulider.method;
        this.headers = bulider.headers;
        this.requestBody = bulider.requestBody;
    }

    public void headers(String key, String val) {headers.put(key, val);}
    public RequestBody getRequestBody() {return requestBody;}
    public String getUrl() {return url;}
    public Method getMethod() {return method;}

    public static class Bulider {
        String url;
        Method method;
        Map<String, String> headers;
        RequestBody requestBody;
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

        public Bulider post(RequestBody body) {
            requestBody = body;
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
