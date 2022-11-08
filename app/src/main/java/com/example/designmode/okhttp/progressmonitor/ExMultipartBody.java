package com.example.designmode.okhttp.progressmonitor;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;


//文件上传监听
public class ExMultipartBody extends RequestBody {

    private int currentLength;
    private UploadProgressListener listener;
    private RequestBody requestBody;

    public ExMultipartBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public ExMultipartBody(UploadProgressListener listener, RequestBody requestBody) {
        this.listener = listener;
        this.requestBody = requestBody;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }


    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long contentLength = contentLength();
        ForwardingSink fs = new ForwardingSink(sink) {
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                currentLength += byteCount;
                if (listener != null) listener.onProgress(currentLength, contentLength);
                super.write(source, byteCount);
            }
        };

        BufferedSink bs = Okio.buffer(fs);
        requestBody.writeTo(bs);
        bs.flush();
    }
}
