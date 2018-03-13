package com.example.jekan.fyp_test.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.example.jekan.fyp_test.R;
import com.example.jekan.fyp_test.RotateTransformation;

/**
 * Created by jekan on 2018-03-13.
 */

public class SetImageActivity extends AppCompatActivity {

    private boolean isFront = true;
    private Button btnRotateImage, btnDrawDot, btnSavePicture;
    private ImageView imgUserFront, imgUserSide = null;
    private String photoPath = null;
    private String[] photoPathArr = new String[2];

    // 액티비티 플래그
    static final int REQUEST_PICK_FROM_ALBUM = 0;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_SEND_DATA=3;
    static final int REQUEST_DRAW_DOT=4;
    private int rotate = 0;
    private int[] rotateArr= new int[2];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_image);
        initState();
    }

    public void initState(){

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
                //회전각도?도 전달해볼까
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
                Intent intent = new Intent(SetImageActivity.this, MainActivity.class); //나중에 데이터 추가해야함
                startActivityForResult(intent, REQUEST_SEND_DATA);
            }
        });

        imgUserFront = (ImageView)findViewById(R.id.img_user_front);
        imgUserSide = (ImageView)findViewById(R.id.img_user_side);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_user_image);
        RadioButton frontRadio = (RadioButton)findViewById(R.id.radio_user_front);
        RadioButton sideRadio = (RadioButton)findViewById(R.id.radio_user_side);
        radioGroup.check(R.id.radio_user_front); // 정면 기본 체크
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio_user_front:
                        imgUserFront.setVisibility(View.VISIBLE);
                        imgUserSide.setVisibility(View.INVISIBLE);
                        isFront = true;
                        if(photoPathArr[0] !=null){
                            btnRotateImage.setVisibility(View.VISIBLE);
                            btnDrawDot.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.radio_user_side:
                        imgUserFront.setVisibility(View.INVISIBLE);
                        imgUserSide.setVisibility(View.VISIBLE);
                        isFront = false;
                        if(photoPathArr[1] !=null){
                            btnRotateImage.setVisibility(View.VISIBLE);
                            btnDrawDot.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            }
        });

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

            case REQUEST_DRAW_DOT: //드로우에서 데이터값을 넘겨받으면 수정해야함!
                if(resultCode == RESULT_OK)
                    btnSavePicture.setVisibility(View.VISIBLE);
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
            }else{
                photoPathArr[1] = photoPath;
                Glide.with(this).load(photoPathArr[1]).into(imgUserSide);
            }
            btnRotateImage.setVisibility(View.VISIBLE);
            btnDrawDot.setVisibility(View.VISIBLE);
        }

    }


    public void userImageRotate(String photoPath, float rotate){
        if(isFront){
            Glide.with(this).load(photoPath).transform( new RotateTransformation(this, rotate )).into(imgUserFront);
        }else{
            Glide.with(this).load(photoPath).transform( new RotateTransformation(this, rotate )).into(imgUserSide);
        }
    }

    public boolean IsTwoImageComplete(){
        if(photoPathArr[0].length()>0 && photoPathArr[1].length()>0)
            return true;
        else
            return false;
    }
}
