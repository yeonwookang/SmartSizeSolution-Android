package com.example.jekan.fyp_test.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.jekan.fyp_test.R;
import com.example.jekan.fyp_test.view.CalcSize;
import com.example.jekan.fyp_test.view.DotPoint;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jekan on 2018-02-20.
 */

public class MainActivity extends AppCompatActivity {
    // 액티비티 플래그
    private static final int GET_ALBUM = 1; // 앨범
    private static final int GET_LOGIN = 0; // 로그인

    // 컴포넌트
    private WebView webview; // 웹뷰
    public HttpClient httpclient = new DefaultHttpClient(); //멤버 변수로 선언
    public android.webkit.CookieManager cookieManager;
    public String sss_domain="http://52.79.137.54";

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

        CookieSyncManager.createInstance(this);
        cookieManager = android.webkit.CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        /*CookieSyncManager.getInstance().startSync();*/
        new Thread(){
            public void run(){
                setSyncCookie();
            }
        }.start();

        // 웹뷰 설정
        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true); // 자바스크립트 허용
        webview.getSettings().setDomStorageEnabled(true);
        webview.loadUrl("http://www.smartsizeservice.xyz/index.php?action=main"); // url
        webview.setWebViewClient(new WebViewClient() {
            // 페이지 읽기가 시작되었을 때의 동작을 설정한다


            @Override
            public void onPageStarted(WebView view, String url, Bitmap b) {
                Toast.makeText(getApplicationContext(), url+"--> onPageStarted", Toast.LENGTH_SHORT).show();
             //   String cookies = cookieM
              //  String cookies = android.webkit.CookieManager.getInstance().getCookie(url);
               // Toast.makeText(getApplicationContext(),"All Cookies 2222" + cookies , Toast.LENGTH_LONG).show();

            }

            // 페이지 읽기가 끝났을 때의 동작을 설정한다
            @Override
            public void onPageFinished(WebView view, String url) {
                Toast.makeText(getApplicationContext(), url+"-->onPageFinished", Toast.LENGTH_SHORT).show();


              /*  CookieSyncManager.getInstance().sync();
                URL url1 = null;
                try {
                    url1 = new URL(url);
                }
                catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("C sharp corner user Ref " + url1.getRef());
                System.out.println(" C sharp corner user host " + url1.getHost());
                System.out.println("C sharp corner user authority " + url1.getAuthority());
                String cookies = android.webkit.CookieManager.getInstance().getCookie(url);
                System.out.println("All COOKIES " + cookies);
                Toast.makeText(getApplicationContext(),"All Cookies " + cookies , Toast.LENGTH_LONG).show();

                *//*CookieSyncManager.getInstance().sync();
                super.onPageFinished(view, url);
                //  String cookies = android.webkit.CookieManager.getInstance().getCookie(url);
                String cookies = android.webkit.CookieManager.getInstance().getCookie(url);

                Toast.makeText(getApplicationContext(), "어떤 값이 나올까여ㅕㅕ"+cookies, Toast.LENGTH_SHORT).show();*/

            }

            @Override
            public void onLoadResource(WebView view, String url) {

            }
        });


        Button btn_menu_down = (Button)findViewById(R.id.btn_menu_down);
        btn_menu_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingPage.startAnimation(translateDownAnim);
            }
        }); //btn_down 버튼 추가된거
        
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
                webview.loadUrl("http://www.smartsizeservice.xyz/index.php?action=main"); // 시작 페이지

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
        etcBtn = (Button) findViewById(R.id.etcBtn); // ETC 버튼 설정
        // ETC 버튼 설정
        setEtcBtnListener();
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
                webview.loadUrl("http://www.smartsizeservice.xyz/index.php?action=login");
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
       /* if(CookieSyncManager.getInstance() !=  null){
            CookieSyncManager.getInstance().stopSync();
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cookieManager != null){
            cookieManager.removeAllCookie();
        }
    }
                                    //, String CookieName
    public String getCookie(String domain){
        String CookieValue = null;

        android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
        String cookies = cookieManager.getCookie(domain);
       /* String[] temp=cookies.split(";");
        for (String ar1 : temp ){
            if(ar1.indexOf(CookieName) == 0){
                String[] temp1=ar1.split("=");
                CookieValue = temp1[1];
                break;
            }
        }*/
       CookieValue = cookies;
        return CookieValue;
    }

    public void setSyncCookie() {
        Log.e("surosuro", "token transfer start ---------------------------");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<>(2);
            nameValuePairs.add(new BasicNameValuePair("token", "TEST"));// 넘길 파라메터 값셋팅token=TEST
            HttpParams params = new BasicHttpParams();
            HttpPost post = new HttpPost(sss_domain);
            post.setParams(params);
            HttpResponse response = null;
            BasicResponseHandler myHandler = new BasicResponseHandler();
            String endResult = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                response = httpclient.execute(post);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                endResult = myHandler.handleResponse(response);
            } catch (HttpResponseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            List<Cookie> cookies = ((DefaultHttpClient)httpclient).getCookieStore().getCookies();
            String cookieString="null";

            if (!cookies.isEmpty()) {
                for (int i = 0; i < cookies.size(); i++) {
                    // cookie = cookies.get(i);
                     cookieString = cookies.get(i).getName() + "="
                            + cookies.get(i).getValue();
                    Log.e("넌 뭐나오니 여기서", cookieString);
                    cookieManager.setCookie(sss_domain, cookieString);
                }
            }else{
                Log.e("넌 뭐나오니 여기서2222", cookieString);
            }
            Thread.sleep(500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}