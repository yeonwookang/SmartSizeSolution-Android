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
    public float getTopLength(){
        return getFloatFirstNum(getStraightDistanceHeight(fPoints.get(8), fPoints.get(9))*ratio);
    }

    //2. 하체길이(앞뒷허리(중간)-발바닥) ->허리- 발바닥까지의 길이 -> 허리 - 발목까지의 길이(발바닥에서 발목으로 변경) - 골반에서 발목으로 변경 - 다시 허리에서 발목
    public float getLegLength(){return getFloatFirstNum(getStraightDistanceHeight(fPoints.get(4), fPoints.get(7))*ratio);
    }

    //3. 어깨너비(어깨 - 사타구니의 직선거리)*2
    public float getShoulderWidth() {
        return getFloatFirstNum(getStraightDistanceWidth(fPoints.get(0), fPoints.get(6))*2*ratio);
    }

    //4. 가슴너비 (가슴-사타구니&목-사타구니&옆가슴-등 논문 공식)
    public double getChestWidth(){
        // Python 연구 프로그램에서 사용한 가슴둘레 수식 사용 (논문 참고)
        // 정면 가슴 너비
        double frontChestWidth = getStraightDistanceWidth(fPoints.get(2), fPoints.get(6)) * 2;
        // 정면 가슴 너비 보정된 값 = 정면 목너비 * 1.2
        double frontNeckWidth_corrector = getStraightDistanceWidth(fPoints.get(8), fPoints.get(6)) * 2 * 1.2;
        // C의 밑변 길이 = (정면 가슴 너비 - (정면 목너비 * 1.2)) / 2
        double c = (frontChestWidth - frontNeckWidth_corrector) / 2.0;
        // 측면 가슴 너비 = 측면 가슴 - (c * 2)
        double sideChestWidth = getStraightDistanceWidth(sPoints.get(1), sPoints.get(2)) - (c * 2);
        // C의 빗변 길이 (피타고라스)
        double c_rainside = sqrt(pow(c, 2) + pow(c, 2));

        // 최종 가슴 둘레
        double finalChestCircum = (frontNeckWidth_corrector * 2) + (sideChestWidth * 2) + (c_rainside * 4);
        // 최종 가슴 너비
        double finalChestWidth = (finalChestCircum / 2.0) * ratio;

        return getDoubleFirstNum(finalChestWidth);
    }

    //5. 암홀너비 (어깨-겨드랑이 원의 둘레 공식)
    public double getArmHoleLength(){
        // 암홀의 둘레 = 2 * PI * 반지름
        // 따라서, 암홀의 단면 너비 = (암홀의 둘레) / 2
        double radius = getStraightDistanceHeight(fPoints.get(0),fPoints.get(1)) / 2.0;
        double armholeWidth = (radius * PI) * ratio;

        return getDoubleFirstNum(armholeWidth);
    }

    //6. 소매길이(어깨 - 손목) *여기가 문제네! 어깨랑 손목은 항상 같은 방향이어야함..대박사건 + 가이드 필요
    public float getArmLength() {
        return getFloatFirstNum(getPointDistance(fPoints.get(0), fPoints.get(3))*ratio);
    }

    //7. 허리너비 (허리-사타구니&목-사타구니&앞허리-뒷허리 논문 공식)
    public double getWaistWidth(){
        // 정면 가슴 너비
        double frontWaistWidth = getStraightDistanceWidth(fPoints.get(4), fPoints.get(6)) * 2;
        // 정면 허리 너비 보정된 값 = 정면 목너비 * 1.2
        double frontNeckWidth_corrector = getStraightDistanceWidth(fPoints.get(8), fPoints.get(6)) * 2 * 1.2;
        // C의 밑변 길이 = (정면 허리 너비 - (정면 목너비 * 1.2)) / 2
        double c = (frontWaistWidth - frontNeckWidth_corrector) / 2.0;
        // 측면 허리 너비 = 측면 허리 - (c * 2)
        double sideChestWidth = getStraightDistanceWidth(sPoints.get(3), sPoints.get(4)) - (c * 2);
        // C의 빗변 길이 (피타고라스)
        double c_rainside = sqrt(pow(c, 2) + pow(c, 2));

        // 최종 허리 둘레
        double finalWaistCircum = (frontNeckWidth_corrector * 2) + (sideChestWidth * 2) + (c_rainside * 4);
        // 최종 허리 너비
        double finalWaistWidth = (finalWaistCircum / 2.0) * ratio;

        return getDoubleFirstNum(finalWaistWidth);
    }

    //8. 엉덩이너비(엉덩이-사타구니&목-사타구니&앞엉덩이-뒷엉덩이 논문 공식)
    public double getHipWidth(){
        // 정면 엉덩이 너비
        double frontHipWidth = getStraightDistanceWidth(fPoints.get(5), fPoints.get(6)) * 2;
        // 정면 엉덩이 너비 보정된 값 = 정면 목너비 * 1.2
        double frontNeckWidth_corrector = getStraightDistanceWidth(fPoints.get(8), fPoints.get(6)) * 2 * 1.2;
        // C의 밑변 길이 = (정면 엉덩이 너비 - (정면 목너비 * 1.2)) / 2
        double c = (frontHipWidth - frontNeckWidth_corrector) / 2.0;
        // 측면 엉덩이 너비 = 측면 엉덩이 - (c * 2)
        double sideHipWidth = getStraightDistanceWidth(sPoints.get(5), sPoints.get(6)) - (c * 2);
        // C의 빗변 길이 (피타고라스)
        double c_rainside = sqrt(pow(c, 2) + pow(c, 2));

        // 최종 엉덩이 둘레
        double finalHipCircum = (frontNeckWidth_corrector * 2) + (sideHipWidth * 2) + (c_rainside * 4);
        // 최종 엉덩이 너비
        double finalHipWidth = (finalHipCircum / 2.0) * ratio;

        return getDoubleFirstNum(finalHipWidth);
    }

    //9. 허벅지너비(엉덩이-사타구니 원의 둘레 공식)
    public float getThighWidth(){
        double radius = getStraightDistanceWidth(fPoints.get(5),fPoints.get(6)) / 2.0;
        double thighWidth = (radius * PI) * ratio;

        return getDoubleFirstNum(thighWidth);
    }

    //10. 밑위길이(앞뒷허리(중간) - 사타구니) -> 골반과 사타구니 거리
    public float getCrotchLength(){
        return getFloatFirstNum(getStraightDistanceHeight(fPoints.get(9), fPoints.get(6))*ratio);
    }

    //11. 목너비 (계산에 필요)
    public float getNeckLength() {
        return getFloatFirstNum(getStraightDistanceWidth(fPoints.get(8), fPoints.get(6)) * 2 * ratio);
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