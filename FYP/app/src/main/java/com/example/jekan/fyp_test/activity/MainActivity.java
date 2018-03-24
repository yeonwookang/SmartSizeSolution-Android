package com.example.jekan.fyp_test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.jekan.fyp_test.R;
import com.example.jekan.fyp_test.view.CalcSize;
import com.example.jekan.fyp_test.view.DotPoint;

import java.util.ArrayList;

/**
 * Created by jekan on 2018-02-20.
 */

public class MainActivity extends AppCompatActivity {
    // 액티비티 플래그
    private static final int GET_ALBUM = 1; // 앨범
    private static final int GET_LOGIN = 0; // 로그인

    // 컴포넌트
    private WebView webview; // 웹뷰

    // 하단 바 버튼
    private Button beforeBtn;
    private Button nextBtn;
    private Button homeBtn;
    private Button refreshBtn;
    private Button etcBtn;

    // 슬라이딩 페이지
    private LinearLayout slidingPage;
    private boolean isSlidingPageOpen = false; // 플래그

    // 애니메이션
    private Animation translateUpAnim; // 위쪽으로 움직이기
    private Animation translateDownAnim; // 아래쪽으로 움직이기

    // 슬라이딩 페이지 내부의 버튼
    private Button signInBtn;
    private Button autoSizeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 웹뷰 설정
        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true); // 자바스크립트 허용
        webview.loadUrl("http://www.smartsizeservice.xyz/main.php"); // url
        webview.setWebViewClient(new WebViewClient()); // 새창에서 뜨지 않도록 설정
        Log.d("웹뷰 초기화", "성공");

        // 하단바 버튼 설정
        beforeBtn = (Button) findViewById(R.id.beforeBtn); // 이전 페이지
        beforeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(webview.canGoBack()) // 뒤로 가기가 가능한 경우에만
                    webview.goBack();
            }
        });

        nextBtn = (Button) findViewById(R.id.nextBtn); // 다음 페이지
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(webview.canGoForward()) //앞으로 가기가 가능한 경우에만
                    webview.goForward();
            }
        });

        homeBtn = (Button) findViewById(R.id.homeBtn); // 홈
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webview.loadUrl("http://www.smartsizeservice.xyz/main.php"); // 시작 페이지
            }
        });

        refreshBtn = (Button) findViewById(R.id.refreshBtn); // 새로고침
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentUrl = webview.getUrl();
                webview.loadUrl(currentUrl); // 현재 Url을 새로 불러옴
            }
        });

        // ETC 버튼 누르면 나타날 슬라이딩 페이지 설정
        setSlidingPage();
        // 슬라이딩 페이지 내부 버튼 초기화
        setSlidingPageButon();
        Log.d("슬라이딩 페이지 초기화", "성공");

        etcBtn = (Button) findViewById(R.id.etcBtn); // ETC 버튼 설정
        // ETC 버튼 설정
        setEtcBtnListener();
        Log.d("ETC 버튼 초기화", "성공");

        Log.d("하단 메뉴바 초기화", "성공");
    }

    //메인에 다시가는걸 어떻게알까..??? - 고민
    public void calcurateValue(){
        Intent intent = getIntent();
        ArrayList<DotPoint> fPoints = (ArrayList<DotPoint>) intent.getSerializableExtra("fPoints");
        ArrayList<DotPoint> sPoints = (ArrayList<DotPoint>) intent.getSerializableExtra("sPoints");
        String actual_height = intent.getExtras().getString("actual_height");
        float user_height = (float)Integer.parseInt(actual_height);
        CalcSize calcSize = new CalcSize(fPoints, sPoints, user_height);
        Toast.makeText(getApplicationContext(), calcSize.clacHeightPixel()+"픽셀값이 나와야겠찌", Toast.LENGTH_SHORT).show();
    }

    // 자동 사이즈 측정 버튼 초기화 함수
    void setAutoSizeBtn() {
        autoSizeBtn = (Button) findViewById(R.id.autoSizeBtn); // 자동 사이즈 측정 버튼
        autoSizeBtn.setOnClickListener(new View.OnClickListener() { // 클릭하는 경우
            @Override
            public void onClick(View view) {
                // 앨범 액티비티 실행
                Intent intent = new Intent(MainActivity.this, SetImageActivity.class); // 현재 액티비티, 전환할 액티비티
                startActivity(intent);
            }
        });

    }

    // 슬라이딩 페이지 내부 버튼 초기화 함수
    void setSlidingPageButon() {
        signInBtn = (Button) findViewById(R.id.signInBtn); // 로그인 버튼
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webview.loadUrl("http://www.smartsizeservice.xyz/login.php");
                slidingPage.setVisibility(View.INVISIBLE);
                isSlidingPageOpen = false;
            }
        });
        // 자동 사이즈 측정 버튼 초기화
        setAutoSizeBtn();
    }

    // ETC 버튼 설정 함수
    void setEtcBtnListener() {
        etcBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d("ETC버튼", "클릭됨");

                if (isSlidingPageOpen) {// 열려 있으면
                    slidingPage.startAnimation(translateDownAnim); //아래쪽으로 애니메이션
                } else { // 닫혀 있으면
                    slidingPage.setVisibility(View.VISIBLE); // 보이도록 한 후
                    slidingPage.startAnimation(translateUpAnim);  // 위쪽으로 애니메이션
                }
            }
        });
    }

    // 슬라이딩 페이지 초기화 함수
    void setSlidingPage() {
        // 슬라이딩 페이지
        slidingPage = (LinearLayout) findViewById(R.id.slidingPage);

        // 애니메이션
        translateUpAnim = AnimationUtils.loadAnimation(this, R.anim.translate_up); // 위쪽으로 이동
        translateDownAnim = AnimationUtils.loadAnimation(this, R.anim.translate_down); // 아래쪽으로 이동

        // 슬라이딩 애니메이션을 감시할 리스너
        SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
        translateUpAnim.setAnimationListener(animListener);
        translateDownAnim.setAnimationListener(animListener);

    }

    // 애니메이션 리스너
    private class SlidingPageAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        public void onAnimationEnd(Animation animation) {
            if (isSlidingPageOpen) {
                slidingPage.setVisibility(View.INVISIBLE);
                isSlidingPageOpen = false;
            } else {
                isSlidingPageOpen = true;
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}