package com.example.jekan.fyp_test.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
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
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_SEND_DATA=3;
    static final int REQUEST_DRAW_DOT=4;

    HashMap<String, String> map = new HashMap<>();

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
        viewFlipper.setOnTouchListener(this); //얘쓸라면 코드 다 갈아엎어야함 ㅠㅠ

        initState();
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

                        if(inputDialog.getEditUerHeight()!=null){
                            float user_height = (float)Float.parseFloat(inputDialog.getEditUerHeight());
                            //(float)Integer.parseInt(inputDialog.getEditUerHeight());
                            //치수 값이 잘 나오는지 테스트
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
                            map.put("top",String.valueOf(topLength));
                            map.put("shoulder",String.valueOf(shoulderLength));
                            map.put("chest", String.valueOf(chestLength));
                            map.put("armhole",String.valueOf(armHoleLength));
                            map.put("arm", String.valueOf(armLength));
                            map.put("bottom", String.valueOf(legLength));
                            map.put("waist", String.valueOf(waistLength));
                            map.put("hip", String.valueOf(hipLength));
                            map.put("thigh",String.valueOf(thighLength));
                            map.put("crotch",String.valueOf(crotchLength));
                            map.put("height", String.valueOf(user_height));



                            /*Toast.makeText(getApplicationContext(), "[사용자 정보]\n상체길이: "+topLength
                                    +"\n하체길이: "+legLength+"\n어깨너비:"+shoulderLength+"\n가슴너비: "+chestLength+"\n허리너비: "+waistLength
                                    +"\n허벅지너비: "+thighLength+"\n엉덩이너비: "+hipLength+"\n팔길이: "+armLength+"\n암홀너비:"+armHoleLength
                                    +"\n밑위길이: "+crotchLength + "\n목너비: " + neckLength,Toast.LENGTH_LONG).show();
*/
                            Log.d("Size","[사용자 정보]\n상체길이: "+topLength
                                    +"\n하체길이: "+legLength+"\n어깨너비:"+shoulderLength+"\n가슴너비: "+chestLength+"\n허리너비: "+waistLength
                                    +"\n허벅지너비: "+thighLength+"\n엉덩이너비: "+hipLength+"\n팔길이: "+armLength+"\n암홀너비:"+armHoleLength
                                    +"\n밑위길이: "+crotchLength + "\n목너비: " + neckLength);


                           /* Intent intent = new Intent(SetImageActivity.this, MainActivity.class);A
                            intent.putExtra("actual_height", inputDialog.getEditUerHeight().toString());
                            intent.putExtra("fPoints", frontDots);
                            intent.putExtra("sPoints", sideDots);
                            startActivity(intent);*/

                            new Thread(){
                                public void run(){
                                    sendUserSize(map,"http://www.smartsizeservice.xyz/index.php?action=editinfo");
                                }
                            }.start();
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
                btnFront.setBackground(getResources().getDrawable(R.drawable.btn_front_white));
                btnSide.setBackground(getResources().getDrawable(R.drawable.btn_side_pink));
                Toast.makeText(getApplicationContext(),"넘김111", Toast.LENGTH_SHORT).show();
                count++;
                if(count<2)
                    viewFlipper.showNext();
                else
                    count--;
            }
            else  if(xAtDown<xAtUp){ //좌에서 우 -> 즉 front로 갈 때
                viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
                viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
                btnFront.setBackground(getResources().getDrawable(R.drawable.btn_front_pink));
                btnSide.setBackground(getResources().getDrawable(R.drawable.btn_side_white));
                Toast.makeText(getApplicationContext(),"넘김222", Toast.LENGTH_SHORT).show();
                count--;
                if(count>-1)
                    viewFlipper.showPrevious();
                else
                    count++;
            }
        }
        return true;
    }

    //사용자의 치수를 웹으로 보내자
    private void sendUserSize(HashMap<String, String> map, String addr){
        String response = ""; // DB 서버의 응답을 담는 변수

        try {
            URL url = new URL(addr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // 해당 URL에 연결

            conn.setConnectTimeout(10000); // 타임아웃: 10초
            conn.setUseCaches(false); // 캐시 사용 안 함
            conn.setRequestMethod("POST"); // POST로 연결
            conn.setDoInput(true);
            conn.setDoOutput(true);

            if (map != null) { // 웹 서버로 보낼 매개변수가 있는 경우우
                OutputStream os = conn.getOutputStream(); // 서버로 보내기 위한 출력 스트림
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8")); // UTF-8로 전송
                bw.write(getPostString(map)); // 매개변수 전송
                bw.flush();
                bw.close();
                os.close();
            }

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) { // 연결에 성공한 경우
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream())); // 서버의 응답을 읽기 위한 입력 스트림

                while ((line = br.readLine()) != null) // 서버의 응답을 읽어옴
                    response += line;
            }

            conn.disconnect();
        } catch (MalformedURLException me) {
            me.printStackTrace();
           // return me.toString();
        } catch (Exception e) {
            e.printStackTrace();
           // return e.toString();
        }
       // return response;
    }

    //매개 변수를 URL에 붙이는 함수
    private String getPostString(HashMap<String, String> map){
        StringBuilder result = new StringBuilder();
        boolean first = true; // 첫 번째 매개변수 여부

        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (first)
                first = false;
            else // 첫 번째 매개변수가 아닌 경우엔 앞에 &를 붙임
                result.append("&");

            try { // UTF-8로 주소에 키와 값을 붙임
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException ue) {
                ue.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result.toString();
    }
}








