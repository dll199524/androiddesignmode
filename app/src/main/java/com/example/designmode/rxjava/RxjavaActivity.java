package com.example.designmode.rxjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.example.designmode.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RxjavaActivity extends AppCompatActivity {

    private ImageView imageView;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bitmap bitmap = (Bitmap) msg.obj;
            imageView.setImageBitmap(bitmap);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
        imageView = findViewById(R.id.image);
//        createBitmap();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://tse4-mm.cn.bing.net/th/id/OIP-C.n0_p3rYRuofABd3XudbZnAHaEo?pid=ImgDet&rs=1");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    Log.d("TAG", "run: =========");
                    InputStream is = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    bitmap = drawCenterLable(RxjavaActivity.this, bitmap, "rxjava");
                    Message message = Message.obtain();
                    message.obj = bitmap;
                    handler.sendMessage(message);
                } catch (IOException e) {e.printStackTrace();}

            }
        }).start();
//        createBitmapByRxjava();

    }


    public void createBitmap() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://tse4-mm.cn.bing.net/th/id/OIP-C.n0_p3rYRuofABd3XudbZnAHaEo?pid=ImgDet&rs=1");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    Log.d("TAG", "run: =========");
                    InputStream is = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    bitmap = drawCenterLable(RxjavaActivity.this, bitmap, "rxjava");
                    Message message = Message.obtain();
                    message.obj = bitmap;
                    handler.sendMessage(message);
                } catch (Exception e) {e.printStackTrace();}

            }
        }).start();

    }


    public void createBitmapByRxjava() {
        Observable.just("https://tse4-mm.cn.bing.net/th/id/OIP-C.n0_p3rYRuofABd3XudbZnAHaEo?pid=ImgDet&rs=1")
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(String s) throws Throwable {
                        URL url = new URL(s);
                        URLConnection connection = url.openConnection();
                        InputStream is = connection.getInputStream();
                        return BitmapFactory.decodeStream(is);
                    }
                }).map(new Function<Bitmap, Bitmap>() {
                    @Override
                    public Bitmap apply(Bitmap bitmap) throws Throwable {
                        return drawCenterLable(RxjavaActivity.this, bitmap, "rxjava");
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Throwable {
                        imageView.setImageBitmap(bitmap);
                    }
                });
    }



    public static Bitmap drawCenterLable(Context context, Bitmap bmp, String text) {
        float scale = context.getResources().getDisplayMetrics().density;
        //创建一样大小的图片
        Bitmap newBmp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
        //创建画布
        Canvas canvas = new Canvas(newBmp);
        canvas.drawBitmap(bmp, 0, 0, null);  //绘制原始图片
        canvas.save();
        canvas.rotate(45); //顺时针转45度
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.argb(50, 255, 255, 255)); //白色半透明
        paint.setTextSize(100 * scale);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        Rect rectText = new Rect();  //得到text占用宽高， 单位：像素
        paint.getTextBounds(text, 0, text.length(), rectText);
        double beginX = (bmp.getHeight()/2 - rectText.width()/2) * 1.4;  //45度角度值是1.414
        double beginY = (bmp.getWidth()/2 - rectText.width()/2) * 1.4;
        canvas.drawText(text, (int)beginX, (int)beginY, paint);
        canvas.restore();
        return newBmp;
    }




}