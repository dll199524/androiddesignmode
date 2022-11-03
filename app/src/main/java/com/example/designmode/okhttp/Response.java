package com.example.designmode.okhttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Response {
    InputStream inputStream;
    public Response(InputStream is) {
        this.inputStream = is;
    }

    public String string() throws IOException {
        return convertStreamToString(inputStream);
    }

    public String convertStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
           while ((line = reader.readLine()) != null)
               sb.append(line);
        } catch (Exception e) {e.printStackTrace();}
        finally {is.close();}
        return sb.toString();
    }
}
