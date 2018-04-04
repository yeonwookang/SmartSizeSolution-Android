package com.example.jekan.fyp_test.view;

import java.util.ArrayList;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

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
    private final double PI = 3.141592;

    // 배열 점 저장하는 순서 중요함 (정면: [0]어깨, [1]겨드랑이, [2]가슴, [3]손목, [4]허리, [5]엉덩이, [6]사타구니, [7]발목, [8]목, [9]골반)
    // 배열 점 저장하는 순서 중요함 (측면: [0]머리, [1]옆가슴, [2]등, [3]앞허리, [4]뒷허리, [5]앞엉덩이, [6]뒷엉덩이, [7]발바닥, [8]앞골반, [9]뒷골반)
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

    //1. 상체길이(어깨 - 앞뒷허리(중간)) ....? -> 어깨 - 허리까지의 길이 -->얘참 애매해! 목점을 알아야 할듯...? -> 목점추가! (목-허리까) ->목에서 골반
    public float getTopLength(){/*
        DotPoint middlePoint = getMiddlePoint(sPoints.get(3), sPoints.get(4), "허리중간점");
        return getStraightDistanceHeight(fPoints.get(0), middlePoint)*ratio;*/
        //return getStraightDistanceHeight(fPoints.get(0), fPoints.get(4))*ratio;
        return getFloatFirstNum(getStraightDistanceHeight(fPoints.get(8), fPoints.get(9))*ratio);
    }

    //2. 하체길이(앞뒷허리(중간)-발바닥) ->허리- 발바닥까지의 길이 -> 허리 - 발목까지의 길이(발바닥에서 발목으로 변경) - 골반에서 발목으로 변경 - 다시 허리에서 발목
    public float getLegLength(){/*
        DotPoint middlePoint = getMiddlePoint(sPoints.get(3), sPoints.get(4), "허리중간점");
        return getStraightDistanceHeight(middlePoint, sPoints.get(7))*ratio;*/
        //return getStraightDistanceHeight(fPoints.get(4), fPoints.get(7))*ratio;
        return getFloatFirstNum(getStraightDistanceHeight(fPoints.get(4), fPoints.get(7))*ratio);
    }

    //3. 어깨너비(어깨 - 사타구니의 직선거리)*2
    public float getShoulderWidth() {
        //return getStraightDistanceWidth(fPoints.get(0), fPoints.get(6))*2*ratio;
        return getFloatFirstNum(getStraightDistanceWidth(fPoints.get(0), fPoints.get(6))*2*ratio);
    }

    //4. 가슴너비
    public float getChestWidth(){
        //******* 2018.04.04 강연우 수정 *******//
        // Python 연구 프로그램에서 사용한 가슴둘레 수식 사용 (논문 참고)
        // 총 가슴 둘레의 1/4값만 구한 다음 * 2

        // 정면 가슴 너비의 절반 = 겨드랑이 - 사타구니
        // 측면 가슴 너비의 절반 = 옆가슴 - 등 / 2
        // 정면 목 너비의 절반 = 목점 - 사타구니

        // C = 정면 가슴 너비의 절반 - 정면 목 너비의 절반

        //return getPointDistance(fPoints.get(2), fPoints.get(6))*2;
        //return getStraightDistanceWidth(fPoints.get(2), fPoints.get(6))*2*ratio;
        return getFloatFirstNum(getStraightDistanceWidth(fPoints.get(2), fPoints.get(6))*2*ratio);
    }

    //**************얘 고치기*****************//
    //5. 암홀너비(어깨 - 겨드랑이) -> 직선과 점사이의 거리로 해야할듯...
    public double getArmHoleLength(){
        //******* 2018.04.04 강연우 수정 *******//
        // 원의 둘레 공식 사용
        // 암홀의 둘레 = 2 * PI * 반지름
        // 따라서, 암홀의 단면 너비 = (암홀의 둘레) / 2
        // 최종 암홀의 단면 너비 = (PI * 반지름) * ratio

        double radius = getStraightDistanceHeight(fPoints.get(0),fPoints.get(1)) / 2.0;
        double armholeWidth = (radius * PI) * ratio;

        //return getPointDistance(fPoints.get(0),fPoints.get(1))*ratio;
        //return getFloatFirstNum(getStraightDistanceHeight(fPoints.get(0),fPoints.get(1))*ratio);
        return getDoubleFirstNum(armholeWidth);
    }

    //**************얘 고치기***************** or 가이드 라인 더 자세하게
    //6. 소매길이(어깨 - 손목) *여기가 문제네! 어깨랑 손목은 항상 같은 방향이어야함..대박사건
    public float getArmLength() {
        //return getPointDistance(fPoints.get(0), fPoints.get(3))*ratio;
        return getFloatFirstNum(getPointDistance(fPoints.get(0), fPoints.get(3))*ratio);
    }

    //7. 허리너비(허리 - 사타구니의 직선거리)*2
    public double getWaistWidth(){

        double waist = getStraightDistanceWidth(fPoints.get(4), fPoints.get(6)) + getStraightDistanceWidth(sPoints.get(3), sPoints.get(4));

        //return getDoubleFirstNum(waistWidth);
        return getDoubleFirstNum(waist);
    }

    //8. 엉덩이너비(엉덩이 - 사타구니의 직선거리)*2
    public float getHipWidth(){
        //return getStraightDistanceWidth(fPoints.get(5), fPoints.get(6))*2*ratio;
        return getFloatFirstNum(getStraightDistanceWidth(fPoints.get(5), fPoints.get(6))*2*ratio);
    }

    //9. 허벅지너비(엉덩이 - 사타구니) ***********여기도 문제가 있어 사실...
    public float getThighWidth(){
       // return getStraightDistanceWidth(fPoints.get(5), fPoints.get(6))*ratio;
        //************04월 05일 연우 수정**********//

        double radius = getStraightDistanceWidth(fPoints.get(5),fPoints.get(6)) / 2.0;
        double thighWidth = (radius * PI) * ratio;

        return getDoubleFirstNum(thighWidth);
    }

    //10. 밑위길이(앞뒷허리(중간) - 사타구니) -> 골반과 사타구니 거리
    public float getCrotchLength(){/*
        DotPoint middlePoint = getMiddlePoint(sPoints.get(3), sPoints.get(4),"허리중간점");
        return getStraightDistanceHeight(middlePoint, fPoints.get(6))*ratio;*/
       // return getStraightDistanceHeight(fPoints.get(4), fPoints.get(6))*ratio; //급조
        return getFloatFirstNum(getStraightDistanceHeight(fPoints.get(9), fPoints.get(6))*ratio);
        /*
        DotPoint middlePoint = getMiddlePoint(sPoints.get(3), sPoints.get(4),"허리중간점");
        return getFloatFirstNum(getStraightDistanceHeight(middlePoint, fPoints.get(6))*ratio);*/
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
    public DotPoint getMiddlePoint(DotPoint point1, DotPoint point2, String caption){ //얘가 코드상에 문제가 있나..? 없는거같은데..
        DotPoint middlePoint = new DotPoint();
        float pointX = Math.abs(point1.getPointX() - point2.getPointX())/2;
        float pointY = Math.abs(point1.getPointY() - point2.getPointY())/2;
        middlePoint.setPointPosition(pointX, pointY);
        middlePoint.setCaption(caption);
        return middlePoint;
    }

    //점사이 거리
    public float getPointDistance(DotPoint point1, DotPoint point2){

        double pointX = Math.abs(point1.getPointX()-point2.getPointX());
        double pointY = Math.abs(point1.getPointY()-point2.getPointY());
        double distance = sqrt(pow(pointX, 2)+ pow(pointY, 2));
        // distance = Math.ceil(distance);
        double num = pow(10.0, 1); //소수 첫번째 자리까지 남기기
        distance = Math.round((distance*num)/num);
        return (float)distance;
    }

    public float getFloatFirstNum(float number){
        return (float)(Math.round(number*10)/10.0);
    }

    // ******* 2018.04.04 강연우 수정 *******//
    // double형 변수 반올림 함수
    public float getDoubleFirstNum(double number){
        return (float)(Math.round(number*10)/10.0);
    }

}