package com.example.jekan.fyp_test.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.jekan.fyp_test.R;

/**
 * Created by jekan on 2018-02-20.
 */

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{
    // 액티비티 플래그
    private static final int GET_ALBUM = 1; // 앨범
    private static final int GET_LOGIN = 0; // 로그인

    // 컴포넌트
    private WebView webview; // 웹뷰
    CookieManager cookieManager;
    private static String userId = null;

    // 슬라이딩 페이지
    private LinearLayout slidingPage;
    private LinearLayout layoutUserLogin;
    private LinearLayout layoutUserLogout;
    private LinearLayout layoutfadeBg;
    private boolean isSlidingPageOpen = false; // 플래그

    // 애니메이션
    private Animation translateUpAnim; // 위쪽으로 움직이기
    private Animation translateDownAnim; // 아래쪽으로 움직이기
    private Animation fadeInAnim; // 점점 선명하게 보여주는 애니메이션 객체
    private Animation fadeOutAnim; // 점점 사라지도록 하는 애니메이션 객체

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        layoutUserLogin = (LinearLayout)findViewById(R.id.layout_when_user_login);
        layoutUserLogout = (LinearLayout)findViewById(R.id.layout_when_user_logout);
        layoutfadeBg = (LinearLayout)findViewById(R.id.layout_fadebg);

       /* CookieSyncManager.createInstance(this);
        cookieManager = android.webkit.CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        *//*CookieSyncManager.getInstance().startSync();*//*
        new Thread(){
            public void run(){
                setSyncCookie();
            }
        }.start();*/

        // 웹뷰 설정
        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true); // 자바스크립트 허용
        webview.getSettings().setDomStorageEnabled(true);
        webview.loadUrl("http://www.smartsizeservice.xyz/index.php?action=main"); // url
        webview.setWebViewClient(new WebViewClient() {
            // 페이지 읽기가 시작되었을 때의 동작을 설정한다


            @Override
            public void onPageStarted(WebView view, String url, Bitmap b) {
            }

            // 페이지 읽기가 끝났을 때의 동작을 설정한다
            @Override
            public void onPageFinished(WebView view, String url) {
                String cookies = CookieManager.getInstance().getCookie(url);
                Toast.makeText(getApplicationContext(), "쿠키값-->onPageFinished: "+cookies, Toast.LENGTH_SHORT).show();

                String [] temp =  cookies.split(";");
                for(String arg : temp){
                    if(arg.contains("id")){
                        String[] _arg = arg.split("=");
                        setUserId(_arg[1]);
                        Toast.makeText(getApplicationContext(), getUserId()+"-->onPageFinished getId", Toast.LENGTH_SHORT).show();
                    }else{
                        setUserId("null");
                        Toast.makeText(getApplicationContext(), getUserId()+"-->onPageFinished getId", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onLoadResource(WebView view, String url) {
            }
        });

        initButtonState();
    }


    public void initButtonState(){

        Button logoutBtn = (Button)findViewById(R.id.btn_logout);
        Button myPageBtn = (Button)findViewById(R.id.btn_my_page);
        Button joinBtn = (Button)findViewById(R.id.btn_join);
        Button btn_menu_down = (Button)findViewById(R.id.btn_menu_down);
        Button beforeBtn = (Button) findViewById(R.id.beforeBtn); // 이전 페이지
        Button nextBtn = (Button) findViewById(R.id.nextBtn); // 다음 페이지
        Button homeBtn = (Button) findViewById(R.id.homeBtn); // 홈
        Button refreshBtn = (Button) findViewById(R.id.refreshBtn); // 새로고침
        Button etcBtn = (Button) findViewById(R.id.etcBtn);
        Button signInBtn = (Button)findViewById(R.id.signInBtn);
        Button autoSizeBtn = (Button)findViewById(R.id.autoSizeBtn);


        logoutBtn.setOnClickListener(this);
        myPageBtn.setOnClickListener(this);
        joinBtn.setOnClickListener(this);
        btn_menu_down.setOnClickListener(this);
        beforeBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        homeBtn.setOnClickListener(this);
        refreshBtn.setOnClickListener(this);
        etcBtn.setOnClickListener(this);
        signInBtn.setOnClickListener(this);
        autoSizeBtn.setOnClickListener(this);
        // ETC 버튼 누르면 나타날 슬라이딩 페이지 설정
        setSlidingPage();
    }

    private String getParameter(String url, String key){

        UrlQuerySanitizer san = new UrlQuerySanitizer(url);
        String value = null;
        if(san.getValue("action")==null){
            return null;
        }
        if(san.getValue("action").equals("main")){
            Log.d("param action22222", "main");
            value = san.getValue(key);
        }

        if(san.getValue("action").equals("login")){
            Log.d("param action33333", "login");
            value = san.getValue(key);
        }

        return value;
    }


    // ETC 버튼 설정 함수
    void setEtcBtnListener() {
                if(isUserLogin()){ //사용자가 로그인 한 상태이면
                    layoutUserLogout.setVisibility(View.GONE);
                    layoutUserLogin.setVisibility(View.VISIBLE);
                }else{
                    // 테스트를 위해 로그인 했다 가정함
                    layoutUserLogout.setVisibility(View.VISIBLE);
                    layoutUserLogin.setVisibility(View.GONE);
                    //layoutUserLogout.setVisibility(View.GONE);
                    //layoutUserLogin.setVisibility(View.VISIBLE);
                }

                if (isSlidingPageOpen) {// 열려 있으면
                    slidingPage.startAnimation(translateDownAnim); //아래쪽으로 애니메이션
                    layoutfadeBg.startAnimation(fadeOutAnim); // 사라짐
                } else { // 닫혀 있으면
                    slidingPage.setVisibility(View.VISIBLE); // 보이도록 한 후
                    layoutfadeBg.setVisibility(View.VISIBLE);
                    slidingPage.startAnimation(translateUpAnim);  // 위쪽으로 애니메이션
                    layoutfadeBg.startAnimation(fadeInAnim);
                }
            }

    // 슬라이딩 페이지 초기화 함수
    void setSlidingPage() {
        // 슬라이딩 페이지
        slidingPage = (LinearLayout) findViewById(R.id.slidingPage);

        // 애니메이션
        translateUpAnim = AnimationUtils.loadAnimation(this, R.anim.translate_up); // 위쪽으로 이동
        translateDownAnim = AnimationUtils.loadAnimation(this, R.anim.translate_down); // 아래쪽으로 이동
        fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeOutAnim = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        // 슬라이딩 애니메이션을 감시할 리스너
        SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
        translateUpAnim.setAnimationListener(animListener);
        translateDownAnim.setAnimationListener(animListener);

    }

    public void setDownSlidingPage(){
        slidingPage.startAnimation(translateDownAnim);
        layoutfadeBg.startAnimation(fadeOutAnim);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_logout:
                setDownSlidingPage();
                break;
            case R.id.btn_my_page:
                webview.loadUrl("http://www.smartsizeservice.xyz/index.php?action=mypage&id="+userId);
                setDownSlidingPage();
                break;
            case R.id.btn_join:
                webview.loadUrl("http://www.smartsizeservice.xyz/index.php?action=joinform");
                setDownSlidingPage();
                break;
            case R.id.btn_menu_down:
                setDownSlidingPage();
                break;
            case R.id.beforeBtn:
                if(webview.canGoBack()) // 뒤로 가기가 가능한 경우에만
                    webview.goBack();

                break;
            case R.id.nextBtn:
                if(webview.canGoForward()) //앞으로 가기가 가능한 경우에만
                    webview.goForward();

                break;
            case R.id.homeBtn:
                webview.loadUrl("http://www.smartsizeservice.xyz/index.php?action=main"); // 시작 페이지

                break;
            case R.id.refreshBtn:
                String currentUrl = webview.getUrl();
                webview.loadUrl(currentUrl); // 현재 Url을 새로 불러옴
                break;
            case R.id.etcBtn:
                setEtcBtnListener();
                break;
            case R.id.autoSizeBtn:
                Intent intent = new Intent(MainActivity.this, SetImageActivity.class); // 현재 액티비티, 전환할 액티비티
                intent.putExtra("user_id", userId);
                startActivity(intent);
                break;
            case R.id.signInBtn:
                webview.loadUrl("http://www.smartsizeservice.xyz/index.php?action=login");
                setDownSlidingPage();
                break;
        }
    }

    // 애니메이션 리스너
    private class SlidingPageAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        public void onAnimationEnd(Animation animation) {
            if (isSlidingPageOpen) {
                slidingPage.setVisibility(View.INVISIBLE);
                layoutfadeBg.setVisibility(View.INVISIBLE);
                isSlidingPageOpen = false;
            } else {
                isSlidingPageOpen = true;
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        CookieSyncManager.createInstance(this);
    }


    @Override
    protected void onResume()
    {
        super.onResume();
       // CookieSyncManager.getInstance().startSync();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cookieManager != null){
            cookieManager.removeAllCookie();
        }
    }

    public boolean isUserLogin(){
        if(getUserId() == null || getUserId().equals("null"))
            return false;
        else
            return true;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserId(){
        return userId;
    }

}