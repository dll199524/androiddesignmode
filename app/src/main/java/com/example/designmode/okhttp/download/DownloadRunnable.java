package com.example.designmode.okhttp.download;

import android.util.Log;

import com.example.designmode.dao.DaoSupportFactroy;
import com.example.designmode.mod.ActivityManager;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Response;

public class DownloadRunnable implements Runnable{

    private static int STATUS_DOWNLOAD = 1;
    private static int STATUS_STOP = 2;
    private int status = STATUS_DOWNLOAD;
    private final long start;
    private final long end;
    private final int threadId;
    private final DownloadCallback callback;
    private final String url;
    private long progress;

    public DownloadRunnable(long start, long end, int threadId, String url, DownloadCallback callback) {
        this.start = start;
        this.end = end;
        this.threadId = threadId;
        this.callback = callback;
        this.url = url;
    }

    @Override
    public void run() {
        InputStream is = null;
        RandomAccessFile raf = null;

        try {
            Response response = OkhttpManager.getInstance().syncResponse(url, start, end);
            Log.d("TAG", "run: " + response.body().contentLength() + " " + start + " " + end);

            File file = FileManager.getInstance().getFile(url);
            is = response.body().byteStream();
            raf = new RandomAccessFile(file, "rwd");

            raf.seek(start);
            int len = 0;
            byte[] butter = new byte[1024 * 10];
            while ((len = is.read(butter)) != -1) {
                if (status == STATUS_STOP) break;
                //保存进度
                progress += len;
                raf.write(butter, 0, len);
            }
            callback.onSuccess(file);
        } catch (IOException e) {callback.onFilure(e);}
        finally {
            close(is);
            close(raf);
            //断点存数据库
        }
    }

    public void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {e.printStackTrace();}
        }
    }

    public void stop() {
    }
}
