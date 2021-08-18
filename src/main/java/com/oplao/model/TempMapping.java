package com.oplao.model;

public class TempMapping {

    private int tempC;
    private int tempF;


    public TempMapping(){

    }
    public TempMapping(int tempC, int tempF) {
        this.tempC = tempC;
        this.tempF = tempF;
    }

    public int getTempC() {
        return tempC;
    }

    public void setTempC(int tempC) {
        this.tempC = tempC;
    }

    public int getTempF() {
        return tempF;
    }

    public void setTempF(int tempF) {
        this.tempF = tempF;
    }
}
