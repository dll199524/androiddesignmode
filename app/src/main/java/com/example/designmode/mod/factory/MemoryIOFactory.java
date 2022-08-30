package com.example.designmode.mod.factory;

public class MemoryIOFactory implements IOFactory{

    @Override
    public IOHandler createIOHandler() {
        return new MemoryIOHandler();
    }
}
