package com.example.jekan.fyp_test.view;

import com.example.jekan.fyp_test.view.DotPoint;

import java.util.ArrayList;

/**
 * Created by jekan on 2018-02-21.
 */

//가슴, 허리, 엉덩이, 허벅지 너비 메소드 작성
//Point클래스 -> DotPoint 이름 변경
// 사이즈 계산 클래스
public class CalcSize {
    private float actual_height;
    private float height;
    private ArrayList<DotPoint> fPoints, sPoints;
    private float ratio;

    // 배열 점 저장하는 순서 중요함 (정면: [0]어깨, [1]겨드랑이, [2]가슴, [3]손목, [4]허리, [5]엉덩이, [6]사타구니, [7]발목)
    // 배열 점 저장하는 순서 중요함 (측면: [0]머리, [1]옆가슴, [2]등, [3]앞허리, [4]뒷허리, [5]앞엉덩이, [6]뒷엉덩이, [7]발바닥)
    public CalcSize(ArrayList<DotPoint> fPoints, ArrayList<DotPoint> sPoints, float actual_height) {
        this.fPoints = fPoints;
        this.sPoints = sPoints;
        this.actual_height = actual_height;
    }

    // 키, ratio 계산
    public float clacHeightPixel() {
        height = getStraightDistanceHeight(sPoints.get(0), sPoints.get(7));
        this.ratio = actual_height/height;
        return ratio;
    }

    //1. 상체길이(어깨 - 앞뒷허리(중간)) ....? -> 어깨 - 허리까지의 길이
    public float getTopLength(){/*
        DotPoint middlePoint = getMiddlePoint(sPoints.get(3), sPoints.get(4), "허리중간점");
        return getStraightDistanceHeight(fPoints.get(0), middlePoint)*ratio;*/
        return getStraightDistanceHeight(fPoints.get(0), fPoints.get(4))*ratio;
    }

    //2. 하체길이(앞뒷허리(중간)-발바닥) ->허리- 발바닥까지의 길이
    public float getLegLength(){/*
        DotPoint middlePoint = getMiddlePoint(sPoints.get(3), sPoints.get(4), "허리중간점");
        return getStraightDistanceHeight(middlePoint, sPoints.get(7))*ratio;*/
        return getStraightDistanceHeight(fPoints.get(4), sPoints.get(7))*ratio;
    }
    //상하체 길이는 좀 더 생각해보자...


    //3. 어깨너비(어깨 - 사타구니의 직선거리)*2
    public float getShoulderWidth() {
        return getStraightDistanceWidth(fPoints.get(0), fPoints.get(6))*2*ratio;
    }

    //4. 가슴너비(가슴 - 사타구니의 직선거리)*2
    public float getChestWidth(){
        //return getPointDistance(fPoints.get(2), fPoints.get(6))*2;
        return getStraightDistanceWidth(fPoints.get(2), fPoints.get(6))*2*ratio;
    }

    //5. 암홀너비(어깨 - 겨드랑이)
    public float getArmHoleLength(){
        return getPointDistance(fPoints.get(0),fPoints.get(1))*ratio;
    }

    //6. 소매길이(어깨 - 손목)
    public float getArmLength() {
        return getPointDistance(fPoints.get(0), fPoints.get(3))*ratio;
    }

    //7. 허리너비(허리 - 사타구니의 직선거리)*2
    public float getWaistWidth(){
        //return  getPointDistance(fPoints.get(4), fPoints.get(6))*2*ratio;
        return getStraightDistanceWidth(fPoints.get(4), fPoints.get(6))*2*ratio;
    }
    //8. 엉덩이너비(엉덩이 - 사타구니의 직선거리)*2
    public float getHipWidth(){
        return getStraightDistanceWidth(fPoints.get(5), fPoints.get(6))*2*ratio;
    }

    //9. 허벅지너비(엉덩이 - 사타구니)
    public float getThighWidth(){
        return getStraightDistanceWidth(fPoints.get(5), fPoints.get(6))*ratio;
    }

    //10. 밑위길이(앞뒷허리(중간) - 사타구니)
    public float getCrotchLength(){
        DotPoint middlePoint = getMiddlePoint(sPoints.get(3), sPoints.get(4),"허리중간점");
        return getStraightDistanceHeight(middlePoint, fPoints.get(6))*ratio;
    }

    //직선거리 - 가로
    public float getStraightDistanceWidth(DotPoint point1, DotPoint point2){
        return Math.abs(point1.getPointX() - point2.getPointX());
    }

    //직선거리 - 세로
    public float getStraightDistanceHeight(DotPoint point1, DotPoint point2){
        return Math.abs(point1.getPointY() - point2.getPointY());
    }

    //중간점
    public DotPoint getMiddlePoint(DotPoint point1, DotPoint point2, String caption){
        DotPoint middlePoint = new DotPoint();
        float pointX = Math.abs(point1.getPointX() - point2.getPointX());
        float pointY = Math.abs(point1.getPointY() - point2.getPointY());
        middlePoint.setPointPosition(pointX/2, pointY/2);
        middlePoint.setCaption(caption);
        return middlePoint;
    }

    //점사이 거리
    public float getPointDistance(DotPoint point1, DotPoint point2){

        double pointX = Math.abs(point1.getPointX()-point2.getPointX());
        double pointY = Math.abs(point1.getPointY()-point2.getPointY());
        double distance = Math.sqrt(Math.pow(pointX, 2)+Math.pow(pointY, 2));
        // distance = Math.ceil(distance);
        double num = Math.pow(10.0, 1); //소수 첫번째 자리까지 남기기
        distance = Math.round((distance*num)/num);
        return (float)distance;
    }

}