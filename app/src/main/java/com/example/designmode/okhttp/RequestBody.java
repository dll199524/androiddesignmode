package com.example.designmode.okhttp;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
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

    public static Bindry create(File file) {
        return new Bindry() {
            @Override
            public long fileLength() {
                return file.length();
            }

            @Override
            public String type() {
                FileNameMap fileNameMap = URLConnection.getFileNameMap();
                String type = fileNameMap.getContentTypeFor(file.getAbsolutePath());
                if (TextUtils.isEmpty(type)) return "application/octet-stream";
                return type;
            }

            @Override
            public String fileName() {
                return file.getName();
            }

            @Override
            public void onWrite(OutputStream outputStream) throws IOException {
                FileInputStream fis = new FileInputStream(file);
                byte[] butter = new byte[2048];
                int len = 0;
                while ((len = fis.read(butter)) != -1) {outputStream.write(butter, 0, len);}
                fis.close();
            }
        };
    }

    public String getContentType() {
        return type + "boundray : " + boundary;
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
            if (val instanceof Bindry) {
                String postStr = getText(key, (String) val);
                len += postStr.getBytes().length;
                len += ((Bindry) val).fileLength() + "\r\n".getBytes().length;
            }
        }
        if (params.size() != 0) len += endBoundray.getBytes().length;
        return len;
    }

    private String getText(String key, String val) {
        return startBoundray + "\r\n"
                + "Content-Disposition: form-data; name = \"" + key + "\"\r\n"
                + "Context-type: text/plain\r\n"
                + "\r\n"
                + val
                + "\r\n";
    }

    private String getText(String key, Bindry val) {
        return startBoundray + "\r\n"
                + "Content-Disposition: form-data; name = \"" + key + "\" filename = \"" + val.fileName() + "\""
                + "Context-type: " +val.type() + "\r\n"
                + "\r\n"
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
            if (val instanceof Bindry) {
                String postStr = getText(key, (Bindry) val);
                outputStream.write(postStr.getBytes());
                outputStream.write("\r\n".getBytes());
            }
        }
    }

    public RequestBody addParams(String key, String val) {
        params.put(key, val);
        return this;
    }

    public RequestBody addParams(String key, Bindry val) {
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
