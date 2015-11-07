package com.nerdz.stormy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by YagoErnandes on 26/10/15.
 */
public class CurrentWeather {
    private String mIcon;
    private long mTime;
    private double mTemperature;
    private double mHumidity;
    private double mPrecipChance;
    private String mSummary;
    private String mTimeZone;

    //Getters & Setters
    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public int getIconId(){
        int iconId = R.drawable.clear_day;

        if(getIcon().equals("clear-day")){
            iconId = R.drawable.clear_day;
        }else if(getIcon().equals("clear-night")){
            iconId = R.drawable.clear_night;
        }else if(getIcon().equals("cloudy")){
            iconId = R.drawable.cloudy;
        }else if(getIcon().equals("cloudy-night")){
            iconId = R.drawable.cloudy_night;
        }else if(getIcon().equals("partly-cloudy-day")){
            iconId = R.drawable.partly_cloudy;
        }else if(getIcon().equals("partly-cloudy-night")){
            iconId = R.drawable.partly_cloudy;
        }else if(getIcon().equals("rain")){
            iconId = R.drawable.rain;
        }else if(getIcon().equals("snow")){
            iconId = R.drawable.snow;
        }else if(getIcon().equals("sunny")){
            iconId = R.drawable.sunny;
        }else if (mIcon.equals("sleet")) {
            iconId = R.drawable.sleet;
        }else if (mIcon.equals("wind")) {
            iconId = R.drawable.wind;
        }else if (mIcon.equals("fog")) {
            iconId = R.drawable.fog;
        }

        return iconId;
    }

    public long getTime() {
        return mTime;
    }

    public String getFormattedTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date dateTime = new Date(getTime()*1000);
        return formatter.format(dateTime);
    }

    public void setTime(long time) {
        mTime = time;
    }

    public double getTemperature() {
        return mTemperature;
    }

    public int getTemperatureCelcius(){
        return (int) Math.round(((mTemperature - 32) * 5)/9);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public double getPrecipChance() {
        return Math.round((mPrecipChance*100));
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }
}
