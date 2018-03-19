package com.example.jekan.fyp_test.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.jekan.fyp_test.R;
import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class SplashActivity extends AppCompatActivity {

    ImageView splashImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        RelativeLayout layout = (RelativeLayout) findViewById(R.id.splashRelative);
//        layout.setBackgroundDrawable(new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.background_splash)));

        splashImg = (ImageView)findViewById(R.id.img_splash_background);
        Glide.with(splashImg.getContext()).load(R.drawable.sss_splash).asGif().diskCacheStrategy(DiskCacheStrategy.NONE).into(splashImg);
        Handler handler = new Handler();
        handler.postDelayed(new SplashHandler(), 3500);
    }

    /* 로딩 화면 스플래시 핸들러 */
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
        recycleView(findViewById(R.id.img_splash_background));
        System.gc();
    }

    /* 메모리 관리를 위한 리사이클 함수*/
    private void recycleView(View view) {
        if (view != null) {
            Drawable bg = view.getBackground();
            if (bg != null) {
                bg.setCallback(null);
                ((BitmapDrawable) bg).getBitmap().recycle();
                view.setBackgroundDrawable(null);
            }
        }
    }
}
