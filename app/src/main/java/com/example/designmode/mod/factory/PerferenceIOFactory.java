package com.example.designmode.mod.factory;

public class PerferenceIOFactory implements IOFactory{
    @Override
    public IOHandler createIOHandler() {
        return new PerferenceIOHandler();
    }
}
