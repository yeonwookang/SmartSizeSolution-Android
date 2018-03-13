package com.example.jekan.fyp_test;

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
import com.example.jekan.fyp_test.activity.CameraActivity;
import com.example.jekan.fyp_test.activity.DrawActivity;

/**
 * Created by jekan on 2018-02-20.
 */

public class AlbumActivity extends AppCompatActivity{

    // 정면, 측면 플래그
    private boolean isFront = true;
    private Button uploadBtn, rotateImageBtn, drawBtn;
    private ImageView userFrontImage, userSideImage = null;
    private String path = null;
    private String[] picturePath = new String[2];

    // 액티비티 플래그
    static final int REQUEST_PICK_FROM_ALBUM = 0;
    static final int REQUEST_TAKE_PHOTO = 1;
    private int rotate = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        // 업로드 버튼
        uploadBtn= (Button)findViewById(R.id.uploadImageBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeAlbumImage();
            }
        });

        // 회전 버튼
        rotateImageBtn = (Button)findViewById(R.id.rotateImageBtn);
        rotateImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*//rotateImage(myBitmap);
                rotate++;
                if(rotate == 4 ) rotate = 0;
                //Glide.with(getApplicationContext()).load(path).into(uploadImage);
                 uploadImage.setImageBitmap(rotate(uri, rotate % 4));*/
                rotate++;
                if(rotate == 4 ) rotate = 0;
                if(checkRadioState()){
                    path = picturePath[0];
                    loadImageRotate(path, rotate%4);
                }else{
                    path = picturePath[1];
                    loadImageRotate(path, rotate%4);
                }

            }
        });

        // EXIT 버튼 설정
        Button exitBtn = (Button) findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() { //클릭하는 경우
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // DRAW 버튼 설정
        drawBtn = (Button) findViewById(R.id.drawCircleBtn);
        drawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // DrawActivity 실행
                Intent intent = new Intent(AlbumActivity.this, DrawActivity.class); // 현재 액티비티, 전환할 액티비티
                intent.putExtra("rotate", rotate); //회전각도?도 전달해볼까
                if(checkRadioState())
                    path = picturePath[0];
                else
                    path = picturePath[1];

                intent.putExtra("path", path); // 이미지 경로 전달
                intent.putExtra("isFront", isFront); // 사진 방향 정보 넘김
                startActivity(intent);
            }
        });

        // CAMERA 버튼 설정
        Button cameraBtn = (Button) findViewById(R.id.cameraBtn);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), CameraActivity.class);
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
        });


        // 이미지 뷰
        userFrontImage = (ImageView)findViewById(R.id.img_user_front);
        userSideImage = (ImageView)findViewById(R.id.img_user_side);

        // 라디오 그룹
        RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
        RadioButton frontRadio = (RadioButton)findViewById(R.id.radioFront);
        RadioButton sideRadio = (RadioButton)findViewById(R.id.radioSide);
        group.check(R.id.radioFront); // 정면 기본 체크
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioFront:
                        userFrontImage.setVisibility(View.VISIBLE);
                        userSideImage.setVisibility(View.INVISIBLE);
                        isFront = true;
                        break;
                    case R.id.radioSide:
                        userFrontImage.setVisibility(View.INVISIBLE);
                        userSideImage.setVisibility(View.VISIBLE);
                        isFront = false;
                        break;
                }
            }
        });

    }

    private void takeAlbumImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"select Picture"), REQUEST_PICK_FROM_ALBUM);
    }

    // 액티비티 시작시
    // 이미지가 로드 되지 않은 경우엔 그리기, 회전 버튼 비활성화
    @Override
    protected void onStart() {
        super.onStart();
        if(path == null) {
            rotateImageBtn.setEnabled(false);
            drawBtn.setEnabled(false);
        } else {
            setRotateDrawBtn();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_PICK_FROM_ALBUM:
                    loadPicture(data);
                break;

            case REQUEST_TAKE_PHOTO: //여기 이상해 ㅜㅜㅜ
                    setRotateDrawBtn();
                break;

                //if(uploadImage)
                /*Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Glide.with(this).load(imageBitmap).asBitmap().into(uploadImage);
                rotateImageBtn.setEnabled(true);
                drawBtn.setEnabled(true);*/


               /* uri = data.getData();
                if(uri != null) {
                    path = uri.toString();
                   // uploadImage.setImageURI(data.getData());
                    Toast.makeText(getApplication(), "dddddd", Toast.LENGTH_SHORT).show();
                    Glide.with(this).load(path).asBitmap().into(uploadImage);
                    rotateImageBtn.setEnabled(true);
                    drawBtn.setEnabled(true);
                } else {
                    rotateImageBtn.setEnabled(false);
                    drawBtn.setEnabled(false);
                }*/


        }

    }

    private void loadPicture(Intent data) {
        try {
            Uri uri = data.getData();
            if(uri != null){
                path = uri.toString();
                setPictureToImageView(path);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setPictureToImageView(String path){
        if(path != null && path.length() > 0) {
            if(checkRadioState()){
                picturePath[0] = path;
                Glide.with(this).load(picturePath[0]).into(userFrontImage);
            }else {
                picturePath[1] = path;
                Glide.with(this).load(picturePath[1]).into(userSideImage);
            }
            setRotateDrawBtn();
        }

    }

    public void setRotateDrawBtn(){
        rotateImageBtn.setEnabled(true);
        drawBtn.setEnabled(true);
    }

    //정면or측면
    public boolean checkRadioState(){
        if(isFront)
            return true;
        else
            return false;
    }

    private void loadImageRotate(String path, float rotate) {

        if(checkRadioState()){
            Glide
                    .with(this)
                    .load(path)
                    .transform( new RotateTransformation(this, rotate )).into(userFrontImage);
        }else{
            Glide
                    .with(this)
                    .load(path)
                    .transform( new RotateTransformation(this, rotate )).into(userSideImage);
        }
    }


}
