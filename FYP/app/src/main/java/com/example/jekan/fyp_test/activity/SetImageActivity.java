package com.example.jekan.fyp_test.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.jekan.fyp_test.InputDialog;
import com.example.jekan.fyp_test.R;
import com.example.jekan.fyp_test.RequestHttpURLConnection;
import com.example.jekan.fyp_test.RotateTransformation;
import com.example.jekan.fyp_test.view.CalcSize;
import com.example.jekan.fyp_test.view.DotPoint;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jekan on 2018-03-13.
 */

public class SetImageActivity extends AppCompatActivity implements  View.OnTouchListener{

    private boolean isFront = true;

    TextView txt_front_default, txt_side_default;
    private Button btnFront,btnSide;
    private Button btnRotateImage, btnDrawDot, btnSavePicture;
    private ImageView imgUserFront, imgUserSide = null;
    private String photoPath = null;
    private String[] photoPathArr = new String[2];
    private int rotate = 0;
    private int[] rotateArr= new int[2];
    private RelativeLayout layoutUserFrontState, layoutUserSideState;
    private ViewFlipper viewFlipper;
    float xAtDown, xAtUp; //x점 이벤트- 눌렀을 때/땠을 때
    int count = 0; //첫, 끝 화면을 알리기 위한 변수
    private String userId;

    static final int REQUEST_PICK_FROM_ALBUM = 0;
    static final int REQUEST_DRAW_DOT=4;


    ContentValues map = new ContentValues();
    ArrayList<DotPoint> frontDots, sideDots;
    CalcSize calcSize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_image);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "bebas.otf");
        txt_front_default = (TextView)findViewById(R.id.txt_front_default);
        txt_front_default.setTypeface(typeface);
        txt_side_default = (TextView)findViewById(R.id.txt_side_default);
        txt_side_default.setTypeface(typeface);
        userId = getIntent().getExtras().getString("user_id");

        layoutUserFrontState = (RelativeLayout)findViewById(R.id.layout_user_front_state);
        layoutUserSideState = (RelativeLayout)findViewById(R.id.layout_user_side_state);
        layoutUserSideState.setVisibility(View.INVISIBLE);

        viewFlipper = (ViewFlipper)findViewById(R.id.flipper);
        viewFlipper.setOnTouchListener(this);
        initState();
    }

    public class NetworkTask extends AsyncTask<Void, Void, String>{
        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
/*
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            tv_outPut.setText(s);*/
        }
    }

    public void initState(){

        btnFront = (Button)findViewById(R.id.btn_user_front);
        btnFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFront=true;
                btnFront.setBackground(getResources().getDrawable(R.drawable.btn_front_pink));
                btnSide.setBackground(getResources().getDrawable(R.drawable.btn_side_white));
                layoutUserFrontState.setVisibility(View.VISIBLE);
                layoutUserSideState.setVisibility(View.INVISIBLE);

                if(photoPathArr[0]==null)
                    txt_front_default.setVisibility(View.VISIBLE);
                else
                    txt_front_default.setVisibility(View.INVISIBLE);

            }
        });

        btnSide = (Button)findViewById(R.id.btn_user_side);
        btnSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFront=false;
                btnFront.setBackground(getResources().getDrawable(R.drawable.btn_front_white));
                btnSide.setBackground(getResources().getDrawable(R.drawable.btn_side_pink));
                layoutUserFrontState.setVisibility(View.INVISIBLE);
                layoutUserSideState.setVisibility(View.VISIBLE);

                if(photoPathArr[1] == null)
                    txt_side_default.setVisibility(View.VISIBLE);
                else
                    txt_side_default.setVisibility(View.INVISIBLE);
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

                        if(inputDialog.getEditUerHeight()!=null){
                            float user_height = (float)Float.parseFloat(inputDialog.getEditUerHeight());
                            calcSize = new CalcSize(frontDots, sideDots,user_height);
                            float pixel = calcSize.clacHeightPixel();

                            float topLength = calcSize.getTopLength(); //상체길이 45
                            float legLength = calcSize.getLegLength(); //하체길이 88
                            float armLength = calcSize.getArmLength(); //소매길이 48
                            float shoulderLength = calcSize.getShoulderWidth(); //어깨너비 27
                            double chestLength = calcSize.getChestWidth(); //가슴너비 둘레 91, 길이 29
                            double armHoleLength = calcSize.getArmHoleLength(); //암홀너비 길이 10 둘레 31
                            double waistLength = calcSize.getWaistWidth();  //허리너비 둘레 80, 길이 30
                            double thighLength = calcSize.getThighWidth(); //허벅지 너비 둘레 53 길이 17
                            double hipLength = calcSize.getHipWidth(); //엉덩이 너비 둘레 97
                            float crotchLength = calcSize.getCrotchLength(); //밑위 길이 15
                            float neckLength = calcSize.getNeckLength(); //목너비 19

                            map.put("id", userId);
                            map.put("toplength",String.valueOf(topLength));
                            map.put("bottomlength", String.valueOf(legLength));
                            map.put("shoulder",String.valueOf(shoulderLength));
                            map.put("chest", String.valueOf(chestLength));
                            map.put("armhole",String.valueOf(armHoleLength));
                            map.put("arm", String.valueOf(armLength));
                            map.put("waist", String.valueOf(waistLength));
                            map.put("hip", String.valueOf(hipLength));
                            map.put("thigh",String.valueOf(thighLength));
                            map.put("crotch",String.valueOf(crotchLength));
                            map.put("height", String.valueOf(user_height));


                            NetworkTask networkTask = new NetworkTask("http://www.smartsizeservice.xyz/index.php?action=editinfo", map);
                            networkTask.execute();

                            Log.d("Size","[사용자 정보]\n상체길이: "+topLength
                                    +"\n하체길이: "+legLength+"\n어깨너비:"+shoulderLength+"\n가슴너비: "+chestLength+"\n허리너비: "+waistLength
                                    +"\n허벅지너비: "+thighLength+"\n엉덩이너비: "+hipLength+"\n팔길이: "+armLength+"\n암홀너비:"+armHoleLength
                                    +"\n밑위길이: "+crotchLength + "\n목너비: " + neckLength);
                        }

                        finish();
                        /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);*/
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
                    if(frontDots!=null && sideDots!=null){ //둘다 값이 있어야만 save버튼이 보이도록
                        btnSavePicture.setVisibility(View.VISIBLE);
                    }
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
                txt_front_default.setVisibility(View.INVISIBLE);
                Glide.with(this).load(photoPathArr[0]).into(imgUserFront);
                btnRotateImage.setVisibility(View.VISIBLE);
                btnDrawDot.setVisibility(View.VISIBLE);
            }else{
                photoPathArr[1] = photoPath;
                txt_side_default.setVisibility(View.INVISIBLE);
                Glide.with(this).load(photoPathArr[1]).into(imgUserSide);
                btnRotateImage.setVisibility(View.VISIBLE);
                btnDrawDot.setVisibility(View.VISIBLE);
            }
        }
    }

    public void userImageRotate(String photoPath, float rotate){
        if(isFront){
            txt_front_default.setVisibility(View.INVISIBLE);
            Glide.with(this).load(photoPath).transform( new RotateTransformation(this, rotate )).into(imgUserFront);
        }else{
            txt_side_default.setVisibility(View.INVISIBLE);
            Glide.with(this).load(photoPath).transform( new RotateTransformation(this, rotate )).into(imgUserSide);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v != viewFlipper) return false;

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            xAtDown = event.getX();
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            xAtUp = event.getX();
            if(xAtDown>xAtUp){ // 우에서 좌 -> 즉 side로 갈 때
                viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
                viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
                isFront = false;
                btnFront.setBackground(getResources().getDrawable(R.drawable.btn_front_white));
                btnSide.setBackground(getResources().getDrawable(R.drawable.btn_side_pink));
                count++;
                if(count<2)
                    viewFlipper.showNext();
                else
                    count--;
            }
            else  if(xAtDown<xAtUp){ //좌에서 우 -> 즉 front로 갈 때
                viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
                viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
                isFront = true;
                btnFront.setBackground(getResources().getDrawable(R.drawable.btn_front_pink));
                btnSide.setBackground(getResources().getDrawable(R.drawable.btn_side_white));
                count--;
                if(count>-1)
                    viewFlipper.showPrevious();
                else
                    count++;
            }
        }
        return true;
    }
}










