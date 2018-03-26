package com.example.jekan.fyp_test.view;

import java.io.Serializable;

/**
 * Created by jekan on 2018-02-21.
 */

//Point -> DotPoint변경
public class DotPoint implements Serializable {
    private float x;
    private float y;
    private String caption; //캡션

    //소수 첫번째 자리까지 남기기 (반올림)
    public float setNumberFormat(float number){
        double num = Math.pow(10.0, 1);
        number = Math.round((number*num)/num);
        return number;
    }

    public DotPoint(float x, float y, String caption) {
        this.x = setNumberFormat(x);
        this.y = setNumberFormat(y);
        this.caption = caption;
    }

    public DotPoint(){
        this(0,0,"중간점");
    }

    public void setPointPosition(float x, float y) {
        this.x = setNumberFormat(x);
        this.y = setNumberFormat(y);
    }

    public void setCaption(String caption){
        this.caption = caption;
    }

    public String getCaption(){
        return caption;
    }

    public float getPointX() {
        return this.x;
    }

    public float getPointY() {
        return this.y;
    }
}