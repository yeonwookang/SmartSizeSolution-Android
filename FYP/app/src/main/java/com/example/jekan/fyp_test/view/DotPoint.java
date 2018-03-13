package com.example.jekan.fyp_test.view;

/**
 * Created by jekan on 2018-02-21.
 */

//Point -> DotPoint변경
public class DotPoint {
    private float x;
    private float y;

    //소수 첫번째 자리까지 남기기 (반올림)
    public float setNumberFormat(float number){
        double num = Math.pow(10.0, 1);
        number = Math.round((number*num)/num);
        return number;
    }

    public DotPoint(float x, float y) {
        this.x = setNumberFormat(x);
        this.y = setNumberFormat(y);
    }

    public void setPointPosition(float x, float y) {
        this.x = setNumberFormat(x);
        this.y = setNumberFormat(y);
    }

    public float getPointX() {
        return this.x;
    }

    public float getPointY() {
        return this.y;
    }
}