package com.example.designmode.mediaCode;

public interface Encoder {

    int getSupportColorFormat();
    int offerEncoder(byte[] input, byte[] output);
}
