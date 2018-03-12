package com.example.jekan.fyp_test;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;

/**
 * Created by jekan on 2018-02-20.
 */

public class DrawActivity extends Activity{
    // 점 최대, 최소 크기
    private final int MAX_DOT_SIZE = 100;
    private final int MIN_DOT_SIZE = 10;

    Dot dot;
    Button okBtn; //?
    boolean isFront;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        // EXIT 버튼 설정
        Button exitBtn = (Button) findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() { //클릭하는 경우
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // OK 버튼 클릭시 MainActivity로 돌아가야함
        // 좌표값들 전달
        okBtn = (Button) findViewById(R.id.okBtn);
        dot = (Dot) findViewById(R.id.dot);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setAdjustViewBounds(true);



        int rotate = getIntent().getExtras().getInt("rotate");
        String path = getIntent().getExtras().getString("path");
        // 전달 받은 Uri 이미지뷰에 넣기
        Glide.with(DrawActivity.this).load(path).transform( new RotateTransformation(this, rotate )).into(imageView);

        // 정면, 앞면 라디오 버튼 선택 정보 가져오기
        isFront = (boolean) getIntent().getExtras().get("isFront");
        Log.d("isFront(DrawActivity)", isFront+"");
        dot.setCaption(isFront); // 캡션 지정

        // 시크바 가져오기
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        // 시크바 변경될 때마다 점 크기 변경
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                // 점 최소, 최대 크기 제한
                int progress = seekBar.getProgress();
                if(progress >= MAX_DOT_SIZE)
                    progress = MAX_DOT_SIZE;
                else if (progress <= MIN_DOT_SIZE)
                    progress = MIN_DOT_SIZE;

                dot.setRadius(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

}
