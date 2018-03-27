package com.example.jekan.fyp_test.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jekan.fyp_test.R;

import java.util.ArrayList;

/**
 * Created by jekan on 2018-02-21.
 */

public class Dot extends View{
    private int RADIUS = 5; // 기본 점 크기 5
    private float x = 100; // 기본 좌표 (0, 0)인 경우 옮기기 힘듦
    private float y = 100;
    private float initialX;
    private float initialY;
    private float offsetX;
    private float offsetY;
    private Paint myPaint;
    private Paint backgroundPaint;
    private Paint captions;
    private CalcSize calcSize;

    // 드래그 플래그
    private boolean isDrag = false;

    // 선택된 좌표점 인덱스
    private int index = 0; // 초기값 0

    // 최대 점 개수
    private final int MAXDOT = 10;

    // 좌표 리스트
    private ArrayList<DotPoint> pointList = new ArrayList<>(); //Point -> DotPoint

    // 점 캡션
    // 배열 점 저장하는 순서 중요함 (정면: [0]어깨, [1]겨드랑이, [2]가슴, [3]손목, [4]허리, [5]엉덩이, [6]사타구니, [7]발목, [8]목, [9]골반)
    // 배열 점 저장하는 순서 중요함 (측면: [0]머리, [1]옆가슴, [2]등, [3]앞허리, [4]뒷허리, [5]앞엉덩이, [6]뒷엉덩이, [7]발바닥, [8]앞골반, [9]뒷골반)
    private String[] frontCaptions = {"어깨", "겨드랑이", "가슴", "손목", "허리", "엉덩이", "사타구니", "발목", "목", "골반"};
    private String[] sideCaptions = {"머리", "옆가슴", "등", "앞허리", "뒷허리", "앞엉덩이", "뒷엉덩이", "발바닥", "앞골반", "뒷골반"};


    // 캡션 플래그
    private boolean isFront = true;

    public Dot(Context context, AttributeSet attrs) {
        super(context, attrs);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLUE);
        backgroundPaint.setAlpha(0);

        myPaint = new Paint();
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "dohyun.ttf");
        myPaint.setTypeface(typeface);
        myPaint.setTextAlign(Paint.Align.CENTER);
        myPaint.setColor(Color.rgb(117, 77, 193));
        myPaint.setAntiAlias(true);
        myPaint.setTextSize(30);


        if(isFront){
            pointList.add(new DotPoint(x + 300, y + 600, frontCaptions[0])); //어깨
            pointList.add(new DotPoint(x + 600, y + 600, frontCaptions[1])); //겨드랑이
            pointList.add(new DotPoint(x + 700, y + 600, frontCaptions[2])); //가슴
            pointList.add(new DotPoint(x + 400, y + 800, frontCaptions[3])); //손목
            pointList.add(new DotPoint(x + 500, y + 750, frontCaptions[4])); //허리
            pointList.add(new DotPoint(x + 300, y + 750, frontCaptions[5])); //엉덩이
            pointList.add(new DotPoint(x + 600, y + 900, frontCaptions[6])); //사타구니
            pointList.add(new DotPoint(x + 400, y + 1200,frontCaptions[7])); //발목
            pointList.add(new DotPoint(x + 400, y + 500, frontCaptions[8])); //목
            pointList.add(new DotPoint(x + 400, y + 950, frontCaptions[9]));//골반

        }else{
            pointList.add(new DotPoint(x + 600, y + 200, sideCaptions[0])); //머리
            pointList.add(new DotPoint(x + 400, y + 500, sideCaptions[1])); //옆가슴
            pointList.add(new DotPoint(x + 700, y + 500, sideCaptions[2])); //등
            pointList.add(new DotPoint(x + 500, y + 800, sideCaptions[3])); //앞허리
            pointList.add(new DotPoint(x + 800, y + 800, sideCaptions[4])); //뒷허리
            pointList.add(new DotPoint(x + 500, y + 850, sideCaptions[5])); //앞엉덩이
            pointList.add(new DotPoint(x + 700, y + 850, sideCaptions[6])); //뒷엉덩이
            pointList.add(new DotPoint(x + 500, y + 1300,sideCaptions[7])); //발바닥
            pointList.add(new DotPoint(x + 700, y + 900, sideCaptions[8])); //앞골반
            pointList.add(new DotPoint(x + 800, y + 900, sideCaptions[9])); //뒷골반
        }

/*
        점위치 임의 지정으로 변경
        // 처음 생성될 때 임의의 좌표를 점 최대치 개수 만큼 배열에 저장
        // 점을 조금씩 띄워서 배치해두었음
        for(int i = 0; i < MAXDOT; i++) {
            if(isFront){
                pointList.add(new DotPoint(x + (i * 100), y + (i * 100), frontCaptions[i])); //정면일때
            }else{
                pointList.add(new DotPoint(x + (i * 100), y + (i * 100), sideCaptions[i])); //측면일때
            }
       }
*/


    }

    public boolean getIsFront(){
        return isFront;
    }


    // 화면이 터치 되었을 때 처리
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {

            case MotionEvent.ACTION_DOWN:
                // 점 안이 클릭되었을 때만 드래그 시작
                // 모든 좌표 확인 하고 해당되는 점만 옮기도록
                for(int i = 0; i < MAXDOT; i++) {
                    if(event.getX()>= pointList.get(i).getPointX() - RADIUS && event.getX() <= pointList.get(i).getPointX() + RADIUS) {
                        if(event.getY() >= pointList.get(i).getPointY() - RADIUS && event.getY() <= pointList.get(i).getPointY() + RADIUS) {

                            if(isFront){
                                pointList.get(i).setCaption(frontCaptions[i]);
                            }else{
                                pointList.get(i).setCaption(sideCaptions[i]);
                            } //추가

                            isDrag = true; // 드래그 시작
                            index = i; // 선택된 점 번호
                            initialX = pointList.get(i).getPointX();
                            initialY = pointList.get(i).getPointY();
                            offsetX = event.getX();
                            offsetY = event.getY();
                            break;

                        } else {
                            isDrag = false; // 드래그 끝
                        }
                    } else {
                        isDrag = false; // 드래그 끝
                    }

                }

                break;

            case MotionEvent.ACTION_MOVE:
                // 점 옮기기
                if(isDrag) {
                    pointList.get(index).setPointPosition(initialX + event.getX() - offsetX, initialY + event.getY() - offsetY);
                    Log.d("Dot(" + index + ")의 X", pointList.get(index).getPointX()+ ""); // 로그에 점 번호, 좌표 출력
                    Log.d("Dot(" + index + ")의 Y", pointList.get(index).getPointY()+ "");
                }

                break;

            // 스크린에서 클릭이 끝났을 경우엔 드래그도 끝남
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isDrag = false; // 드래그 끝
                break;
        }
        return (true);
    }

    // 새로운 점을 그리는 메소드
    public void onDraw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        // 캔버스의 가로 세로만큼의 투명 배경을 다시 계속 그려준다.
        canvas.drawRect(0, 0, width, height, backgroundPaint);

        // 점들을 다시 그려줌
        // 선택된 점은 표시
        for(int i = 0; i < MAXDOT; i++) {

            if(i == index) {
                float selected = (float) (RADIUS / 2.5); // 선택된 점 테두리 굵기 조정
                // 선택된 점에 해당하면 이중 점
                myPaint.setColor(Color.rgb(117, 77, 193));
                canvas.drawCircle(pointList.get(i).getPointX(), pointList.get(i).getPointY(), RADIUS, myPaint); // 점 그리기

                myPaint.setColor(Color.WHITE);
                canvas.drawCircle(pointList.get(i).getPointX(), pointList.get(i).getPointY(), RADIUS - selected, myPaint); // 점 그리기
            }  else {
                myPaint.setColor(Color.rgb(117, 77, 193)); // 나머진 보라색
                canvas.drawCircle(pointList.get(i).getPointX(), pointList.get(i).getPointY(), RADIUS, myPaint); // 점 그리기
            }

            if(isFront){
                pointList.get(i).setCaption(frontCaptions[i]);
                canvas.drawText(pointList.get(i).getCaption(), pointList.get(i).getPointX() - 25, (pointList.get(i).getPointY() - 20) - (this.RADIUS ), myPaint);
            }else{
                pointList.get(i).setCaption(sideCaptions[i]);
                canvas.drawText(pointList.get(i).getCaption(), pointList.get(i).getPointX() - 25, (pointList.get(i).getPointY() - 20) - (this.RADIUS ), myPaint);
            }


           /* if(isFront)
                canvas.drawText(frontCaptions[i], pointList.get(i).getPointX() - 25, (pointList.get(i).getPointY() - 20) - (this.RADIUS ), captions); // 점 캡션
            else
                canvas.drawText(sideCaptions[i], pointList.get(i).getPointX() - 25, (pointList.get(i).getPointY() - 20) - (this.RADIUS ), captions); // 점 캡션*/
        }

        // 새로고침
        invalidate();
    }

    public ArrayList<DotPoint> dotState(){

        ArrayList<DotPoint> dotPointsFront = new ArrayList<>();
        ArrayList<DotPoint> dotPointsSide = new ArrayList<>();

        if(isFront){
            for(int i=0; i<MAXDOT; i++){
                dotPointsFront.add(i, new DotPoint(pointList.get(i).getPointX(), pointList.get(i).getPointY(), frontCaptions[i]));
            }
            return dotPointsFront;
        }else{
            for(int i=0; i<MAXDOT; i++){
                dotPointsSide.add(i, new DotPoint(pointList.get(i).getPointX(), pointList.get(i).getPointY(), sideCaptions[i]));
            }
            return dotPointsSide;
        }
    }



    // 점 크기 지정
    public void setRadius(int radius) {
        this.RADIUS = radius;
        invalidate(); // 새로고침
    }

    // 점 캡션 지정
    public void setCaption(boolean isFront) {
        this.isFront = isFront;
        invalidate();
    }


    // 점 좌표 반환
    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    // 점 좌표 설정
    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

}
