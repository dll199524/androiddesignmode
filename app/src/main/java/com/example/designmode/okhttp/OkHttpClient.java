package com.example.designmode.okhttp;

public class OkHttpClient {
    final Dispatcher dispatcher;
    private OkHttpClient(Bulider bulider) {
        this.dispatcher = bulider.dispatcher;
    }
    public OkHttpClient() {this(new Bulider());}

    public Call newCall(Request request) {return new RealCall(request, this);}

    public static class Bulider {
        Dispatcher dispatcher;
        public Bulider() {
            dispatcher = new Dispatcher();
        }
        public OkHttpClient bulid() {return new OkHttpClient(this);}
    }

    public Dispatcher dispatcher() {return dispatcher;}
}
