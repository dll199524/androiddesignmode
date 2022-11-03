package com.example.designmode.okhttp;

import java.io.IOException;
import java.io.OutputStream;

public interface Bindry {
    long fileLength();
    String type();
    String fileName();
    void onWrite(OutputStream outputStream) throws IOException;
}
