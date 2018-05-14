package com.example.jekan.fyp_test.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.example.jekan.fyp_test.R;
import com.example.jekan.fyp_test.RotateTransformation;
import com.example.jekan.fyp_test.view.Dot;
import com.example.jekan.fyp_test.view.DotPoint;

import java.util.ArrayList;

import pl.polidea.view.ZoomView;

/**
 * Created by jekan on 2018-02-20.
 */


public class DrawActivity extends Activity{
    // 점 최대, 최소 크기
    private final int MAX_DOT_SIZE = 100;
    private final int MIN_DOT_SIZE = 5;

    ToggleButton helpBtn;
    ArrayList<DotPoint> dotPoints = new ArrayList<>();
    Dot dot;
    boolean isFront;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.zoom_item, null, false);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ZoomView zoomView = new ZoomView(this);
        zoomView.addView(v);
        zoomView.setLayoutParams(layoutParams);
        zoomView.setMiniMapEnabled(true); // 좌측 상단 검은색 미니맵 설정
        zoomView.setMaxZoom(4f); // 줌 Max 배율 설정  1f 로 설정하면 줌 안됩니다.
        zoomView.setMiniMapCaption("화면 배율"); //미니 맵 내용
        zoomView.setMiniMapCaptionSize(20); // 미니 맵 내용 글씨 크기 설정
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        container.addView(zoomView);

        helpBtn = (ToggleButton)findViewById(R.id.btn_user_help);

        helpBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //LinearLayout helpView = (LinearLayout) findViewById(R.id.view_user_help);
                //TextView helpText = (TextView)findViewById(R.id.txt_user_help);
               // Button btn_help = (Button)findViewById(R.id.btn_help_txt);
                Button btn_help = (Button) findViewById(R.id.txt_user_help);
                if(isChecked)
                    btn_help.setVisibility(View.VISIBLE);
                else
                    btn_help.setVisibility(View.INVISIBLE);
            }
        });

        // EXIT 버튼 설정
        Button saveBtn = (Button) findViewById(R.id.btn_save_draw);
        // OK 버튼 클릭시 MainActivity로 돌아가야함
        // 좌표값들 전달
        dot = (Dot) findViewById(R.id.dot);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setAdjustViewBounds(true);

        //데이터 전달
        int rotate = getIntent().getExtras().getInt("rotate");
        String path = getIntent().getExtras().getString("path");
        // 전달 받은 Uri 이미지뷰에 넣기
        Glide.with(DrawActivity.this).load(path).transform( new RotateTransformation(this, rotate )).into(imageView);

        isFront = (boolean) getIntent().getExtras().get("isFront");
        Log.d("isFront(DrawActivity)", isFront+"");
        dot.setCaption(isFront); // 캡션 지정
        dot.setLocation(isFront); // 점위치 지정

        saveBtn.setOnClickListener(new View.OnClickListener() { //클릭하는 경우
            @Override
            public void onClick(View view) {

                Intent intent  = getIntent();
                ArrayList<DotPoint> dots = dot.dotState();
                intent.putExtra("isFront", dot.getIsFront());
                intent.putExtra("dotPosition", dots);

               /*
                for(int i=0; i<dot.dotState().size(); i++){
                    Toast.makeText(getApplicationContext(), String.valueOf(dot.dotState().get(i).getPointX())+", "+String.valueOf(dot.dotState().get(i).getPointY()), Toast.LENGTH_SHORT).show();
                    //a = dot.dotState().get(i).getPointX();
                }*/
                setResult(RESULT_OK, intent);
                finish();
            }
        });

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
