package com.example.designmode.rxjava;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public abstract class Schedulers {

    static Schedulers IO;
    static Schedulers main;
    
    static {
        IO = new IOSchedulers();
        main = new MainSchedulers(new Handler(Looper.getMainLooper()));
    }


    public static Schedulers main() {return main;}

    public abstract <T> void scheduleDirect(Runnable runnable);

    public static Schedulers io() {return IO;}

    private static class IOSchedulers extends Schedulers {
        ExecutorService executorService;
        private IOSchedulers() {
            executorService = Executors.newScheduledThreadPool(1,
                    new ThreadFactory() {
                        @Override
                        public Thread newThread(Runnable r) {
                            return new Thread(r);
                        }
                    });
        }

        @Override
        public <T> void scheduleDirect(Runnable runnable) {
//            executorService.submit(runnable);
            executorService.execute(runnable);
        }
    }

    private static class MainSchedulers extends Schedulers {
        Handler handler;
        public MainSchedulers(Handler handler) {
            this.handler = handler;
        }

        @Override
        public <T> void scheduleDirect(Runnable runnable) {
            Message message = Message.obtain(handler, runnable);
            handler.sendMessage(message);
        }
    }
}
