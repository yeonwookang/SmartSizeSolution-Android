package com.example.jekan.fyp_test.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.jekan.fyp_test.R;
import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class SplashActivity extends AppCompatActivity {
    private GifImageView splashImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashImage = (GifImageView) findViewById(R.id.img_splash_background);
        try {
            InputStream inputStream = getAssets().open("sss_splash.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            splashImage.setBytes(bytes);
            splashImage.startAnimation();
        } catch (IOException ex) {

        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
        }, 2500);
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        recycleView(findViewById(R.id.activity_splash_screen));
        System.gc();
    }

    private void recycleView(View view) {
        if (view != null) {
            Drawable bg = view.getBackground();
            if (bg != null) {
                bg.setCallback(null);
                ((BitmapDrawable) bg).getBitmap().recycle();
                view.setBackgroundDrawable(null);
            }
        }*/

    /*

    private class SplashHandler implements Runnable {
        public void run() {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            SplashActivity.this.finish();
        }
    }

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView = (ImageView)findViewById(R.id.img_splash_background) ;
        Handler handler = new Handler();
        handler.postDelayed(new SplashHandler(), 1000);
    }


    private class SplashHandler implements Runnable {
        public void run() {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            SplashActivity.this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //recycleView(findViewById(R.id.img_splash_background));
         recycleView(imageView);
        System.gc();
    }

    private void recycleView(ImageView view) {
        if (view != null) {
            GlideDrawableImageViewTarget splashImage = new GlideDrawableImageViewTarget(view);
            Glide.with(this).load(R.drawable.img_splash).into(splashImage);
            Drawable bg = view.getBackground();
            if (bg != null) {
                bg.setCallback(null);
                ((BitmapDrawable) bg).getBitmap().recycle();
                view.setBackgroundDrawable(null);
            }
        }
    }*/
    }
