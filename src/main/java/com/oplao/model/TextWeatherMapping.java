package com.oplao.model;

public class TextWeatherMapping {

    int maxDayTempC;
    int maxDayTempF;
    int minDayTempC;
    int minDayTempF;
    double maxRainfallMM;
    double minRainfallMM;
    double maxWindMS;
    double maxWindKmPh;
    int hourMax;
    int hourMin;
    boolean active;




    public static TextWeatherMapping create(int maxDayTempC, int maxDayTempF,
                                            int minDayTempC, int minDayTempF,
                                            double maxRainfallMM, double minRainfallMM, int maxHour, int minHour,
                                            double maxWindMS,
                                            double maxWindKmPh,
                                            boolean active){
        return new TextWeatherMapping(maxDayTempC, maxDayTempF, minDayTempC,
                minDayTempF, maxRainfallMM, minRainfallMM, maxHour, minHour, maxWindMS, maxWindKmPh, active);
    }
    public TextWeatherMapping() {
    }

    public TextWeatherMapping(int maxDayTempC, int maxDayTempF, int minDayTempC, int minDayTempF, double maxRainfallMM, double minRainfallMM, int hourMax, int hourMin, double maxWindMS, double maxWindKmPh, boolean active) {
        this.maxDayTempC = maxDayTempC;
        this.maxDayTempF = maxDayTempF;
        this.minDayTempC = minDayTempC;
        this.minDayTempF = minDayTempF;
        this.maxRainfallMM = maxRainfallMM;
        this.minRainfallMM = minRainfallMM;
        this.hourMax = hourMax;
        this.hourMin = hourMin;
        this.maxWindMS = maxWindMS;
        this.maxWindKmPh = maxWindKmPh;
        this.active = active;
    }

    public double getMaxRainfallMM() {
        return maxRainfallMM;
    }

    public void setMaxRainfallMM(double maxRainfallMM) {
        this.maxRainfallMM = maxRainfallMM;
    }

    public double getMinRainfallMM() {
        return minRainfallMM;
    }

    public void setMinRainfallMM(double minRainfallMM) {
        this.minRainfallMM = minRainfallMM;
    }

    public double getMaxWindMS() {
        return maxWindMS;
    }

    public void setMaxWindMS(double maxWindMS) {
        this.maxWindMS = maxWindMS;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getMaxDayTempC() {
        return maxDayTempC;
    }

    public void setMaxDayTempC(int maxDayTempC) {
        this.maxDayTempC = maxDayTempC;
    }

    public int getHourMax() {
        return hourMax;
    }

    public void setHourMax(int hourMax) {
        this.hourMax = hourMax;
    }

    public int getHourMin() {
        return hourMin;
    }

    public void setHourMin(int hourMin) {
        this.hourMin = hourMin;
    }

    public int getMaxDayTempF() {
        return maxDayTempF;
    }

    public void setMaxDayTempF(int maxDayTempF) {
        this.maxDayTempF = maxDayTempF;
    }

    public int getMinDayTempC() {
        return minDayTempC;
    }

    public void setMinDayTempC(int minDayTempC) {
        this.minDayTempC = minDayTempC;
    }

    public int getMinDayTempF() {
        return minDayTempF;
    }

    public void setMinDayTempF(int minDayTempF) {
        this.minDayTempF = minDayTempF;
    }

    public double getmaxRainfallMM() {
        return maxRainfallMM;
    }

    public void setmaxRainfallMM(double maxRainfallMM) {
        this.maxRainfallMM = maxRainfallMM;
    }

    public double getMaxWindKmPh() {
        return maxWindKmPh;
    }

    public void setMaxWindKmPh(double maxWindKmPh) {
        this.maxWindKmPh = maxWindKmPh;
    }
}
