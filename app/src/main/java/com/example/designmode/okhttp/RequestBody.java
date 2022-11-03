package com.example.designmode.okhttp;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RequestBody {
    final static String FORM = "multipart/form-data";
    String type;
    Map<String, Object> params;
    String boundary = createBoundary();
    String startBoundray = "--" + boundary;
    String endBoundray = startBoundray + "--";

    public RequestBody() {params = new HashMap<>();}
    public String getContentType() {
        return type;
    }

    public long getContentLength() {
        int len = 0;
        for (Map.Entry<String, Object> param : params.entrySet()) {
            String key = param.getKey();
            Object val = param.getValue();
            if (val instanceof String) {
                String postStr = getText(key, (String) val);
                len += postStr.getBytes().length;
            }
        }
        if (params.size() != 0) len += endBoundray.getBytes().length;
        return len;
    }

    private String getText(String key, String val) {
        return startBoundray + "\r\n"
                + "Content-Disposition: form-data: name = \"" + key + "\"\r\n"
                + "Context-type: text/plain\r\n"
                + "\r\n"
                + val
                + "\r\n";
    }

    public void onWriteBody(OutputStream outputStream) throws IOException {
        for (Map.Entry<String, Object> param : params.entrySet()) {
            String key = param.getKey();
            Object val = param.getValue();
            if (val instanceof String) {
                String postStr = getText(key, (String) val);
                outputStream.write(postStr.getBytes());
                outputStream.write("\r\n".getBytes());
            }
        }
    }

    public RequestBody addParams(String key, String val) {
        params.put(key, val);
        return this;
    }

    public RequestBody type(String form) {
        this.type = form;
        return this;
    }

    private String createBoundary() {
        return "okhttp" + UUID.randomUUID().toString();
    }
}
