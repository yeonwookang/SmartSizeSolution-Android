package com.example.jekan.fyp_test.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jekan.fyp_test.InputDialog;
import com.example.jekan.fyp_test.R;
import com.example.jekan.fyp_test.RotateTransformation;
import com.example.jekan.fyp_test.view.DotPoint;

import java.util.ArrayList;

/**
 * Created by jekan on 2018-03-13.
 */

public class SetImageActivity extends AppCompatActivity {

    private boolean isFront = true;

    private Button btnRotateImage, btnDrawDot, btnSavePicture;
    private ImageView imgUserFront, imgUserSide = null;
    private String photoPath = null;
    private String[] photoPathArr = new String[2];
    private int rotate = 0;
    private int[] rotateArr= new int[2];

    static final int REQUEST_PICK_FROM_ALBUM = 0;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_SEND_DATA=3;
    static final int REQUEST_DRAW_DOT=4;

    ArrayList<DotPoint> frontDots, sideDots;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_image);
        initState();
    }

    public void initState(){

        Button btnFront = (Button)findViewById(R.id.btn_user_front);
        btnFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFront=true;
                imgUserFront.setVisibility(View.VISIBLE);
                imgUserSide.setVisibility(View.INVISIBLE);
            }
        });
        Button btnSide = (Button)findViewById(R.id.btn_user_side);
        btnSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFront=false;
                imgUserFront.setVisibility(View.INVISIBLE);
                imgUserSide.setVisibility(View.VISIBLE);
            }
        });

        //이미지 가져오기
        Button btnPickImage = (Button)findViewById(R.id.btn_gallery_set_Image);
        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeAlbumImage();
            }
        });

        btnRotateImage = (Button)findViewById(R.id.btn_rotate_set_Image);
        btnRotateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotate++;
                if(rotate == 4 ) rotate = 0;
                if(isFront){
                    // photoPathArr[0].toString();
                    rotateArr[0] = rotate;
                    userImageRotate(photoPathArr[0].toString(), rotateArr[0]%4);
                }else{
                    rotateArr[1] = rotate;
                    userImageRotate(photoPathArr[1].toString(), rotateArr[1]%4);
                }

            }
        });

        btnDrawDot = (Button)findViewById(R.id.btn_draw_set_Image);
        btnDrawDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SetImageActivity.this, DrawActivity.class); // 현재 액티비티, 전환할 액티비티
                if(isFront){
                    photoPath = photoPathArr[0];
                    rotate = rotateArr[0];
                }
                else{
                    photoPath = photoPathArr[1];
                    rotate = rotateArr[1];
                }

                intent.putExtra("path", photoPath); // 이미지 경로 전달
                intent.putExtra("isFront", isFront); // 사진 방향 정보 넘김
                intent.putExtra("rotate", rotate);
                startActivityForResult(intent, REQUEST_DRAW_DOT);

            }
        });

        Button btnTakePicture = (Button)findViewById(R.id.btn_take_picture_set_Image);
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), CameraActivity.class);
                startActivity(intent);
                // startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
        });

        btnSavePicture = (Button)findViewById(R.id.btn_save_set_Image);
        btnSavePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final InputDialog inputDialog = new InputDialog(SetImageActivity.this);
                inputDialog.show();
                inputDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                        Toast.makeText(getApplicationContext(), "당신의 키: "+inputDialog.getEditUerHeight(), Toast.LENGTH_SHORT).show();
                        if(inputDialog.getEditUerHeight()!=null){
                            Intent intent = new Intent(SetImageActivity.this, MainActivity.class);
                            intent.putExtra("actual_height", inputDialog.getEditUerHeight().toString());
                            intent.putExtra("fPoints", frontDots);
                            intent.putExtra("sPoints", sideDots);
                            startActivity(intent);
                        }
                       // Intent intent = new Intent(SetImageActivity.this, MainActivity.class); //나중에 데이터 추가해야함
                        // startActivityForResult(intent, REQUEST_SEND_DATA);
                    }
                });
                inputDialog.setCancelable(false);
            }
        });

        imgUserFront = (ImageView)findViewById(R.id.img_user_front);
        imgUserSide = (ImageView)findViewById(R.id.img_user_side);
        Button btnClose = (Button)findViewById(R.id.btn_close_set_Image);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void takeAlbumImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"select Picture"), REQUEST_PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case REQUEST_PICK_FROM_ALBUM:
                loadUserImage(data);
                break;

            case REQUEST_DRAW_DOT:
                if(resultCode == RESULT_OK){
                    Boolean state = data.getExtras().getBoolean("isFront");

                    if(state){
                        Toast.makeText(getApplicationContext(), "true", Toast.LENGTH_SHORT).show();
                        frontDots = (ArrayList<DotPoint>) data.getSerializableExtra("dotPosition");
                        for(int i=0; i<frontDots.size(); i++){
                            Log.d("정면 좌표: ", frontDots.get(i).getPointX()+", "+frontDots.get(i).getPointY());
                        }
                    }else{
                        sideDots = (ArrayList<DotPoint>) data.getSerializableExtra("dotPosition");
                        for (int i=0; i<sideDots.size(); i++){
                            Log.d("측면 좌표: ", sideDots.get(i).getPointX()+", "+sideDots.get(i).getPointY());
                        }
                        Toast.makeText(getApplicationContext(), "false", Toast.LENGTH_SHORT).show();
                    }
                    btnSavePicture.setVisibility(View.VISIBLE);
                }
                  break;
        }
    }

    public void loadUserImage(Intent data){
        try {
            Uri uri = data.getData();
            if(uri != null){
                photoPath = uri.toString();
                setPictureToImageView(photoPath);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setPictureToImageView(String photoPath){

        if(photoPath!=null && photoPath.length()>0){
            if(isFront){
                photoPathArr[0] = photoPath;
                Glide.with(this).load(photoPathArr[0]).into(imgUserFront);
                btnRotateImage.setVisibility(View.VISIBLE);
                btnDrawDot.setVisibility(View.VISIBLE);
            }else{
                photoPathArr[1] = photoPath;
                Glide.with(this).load(photoPathArr[1]).into(imgUserSide);
                btnRotateImage.setVisibility(View.VISIBLE);
                btnDrawDot.setVisibility(View.VISIBLE);
            }

        }

    }


    public void userImageRotate(String photoPath, float rotate){
        if(isFront){
            Glide.with(this).load(photoPath).transform( new RotateTransformation(this, rotate )).into(imgUserFront);
        }else{
            Glide.with(this).load(photoPath).transform( new RotateTransformation(this, rotate )).into(imgUserSide);
        }
    }

}
