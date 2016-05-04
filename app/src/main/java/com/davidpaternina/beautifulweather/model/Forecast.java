package com.davidpaternina.beautifulweather.model;

import android.util.Log;

import com.davidpaternina.beautifulweather.R;

import java.util.Calendar;

/**
 * Created by David on 30-Apr-15.
 */
public class Forecast {

    private Current mCurrent;
    private Hour[] mHourlyForecast;
    private Day[] mDailyForecast;

    public Current getCurrent() {
        return mCurrent;
    }

    public void setCurrent(Current current) {
        mCurrent = current;
    }

    public Hour[] getHourlyForecast() {
        return mHourlyForecast;
    }

    public void setHourlyForecast(Hour[] hourlyForecast) {
        mHourlyForecast = hourlyForecast;
    }

    public Day[] getDailyForecast() {
        return mDailyForecast;
    }

    public void setDailyForecast(Day[] dailyForecast) {
        mDailyForecast = dailyForecast;
    }

    public static int convertToCelsius(int fahrenTemp){
        double temp = ((fahrenTemp - 32)*5)/9;
        return (int) Math.round(temp);
    }

    public static int getBgId(String iconString) {
        int iconId = R.drawable.bg;

        if (iconString.equals("clear-day")) {
            iconId = R.drawable.bg_clear_day;
        }
        else if (iconString.equals("clear-night")) {
            iconId = R.drawable.bg_clear_night;
        }
        else if (iconString.equals("rain")) {
            iconId = R.drawable.bg_rain;
        }
        else if (iconString.equals("snow")) {
            iconId = R.drawable.bg_snow;
        }
        else if (iconString.equals("sleet")) {
            iconId = R.drawable.bg_sleet;
        }
        else if (iconString.equals("wind")) {
            iconId = R.drawable.bg_wind;
        }
        else if (iconString.equals("fog")) {
            iconId = R.drawable.bg_fog;
        }
        else if (iconString.equals("cloudy")) {
            iconId = R.drawable.bg_cloudy;
        }
        else if (iconString.equals("partly-cloudy-day")) {
            iconId = R.drawable.bg_partly_cloudy_day;
        }
        else if (iconString.equals("partly-cloudy-night")) {
            iconId = R.drawable.bg_partly_cloudy_night;
        }
//
        return iconId;
    }

    public static int getIconId(String iconString) {
        int iconId = R.drawable.clear_day;

        if (iconString.equals("clear-day")) {
            iconId = R.drawable.clear_day;
        }
        else if (iconString.equals("clear-night")) {
            iconId = R.drawable.clear_night;
        }
        else if (iconString.equals("rain")) {
            iconId = R.drawable.rain;
        }
        else if (iconString.equals("snow")) {
            iconId = R.drawable.snow;
        }
        else if (iconString.equals("sleet")) {
            iconId = R.drawable.sleet;
        }
        else if (iconString.equals("wind")) {
            iconId = R.drawable.wind;
        }
        else if (iconString.equals("fog")) {
            iconId = R.drawable.fog;
        }
        else if (iconString.equals("cloudy")) {
            iconId = R.drawable.cloudy;
        }
        else if (iconString.equals("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy;
        }
        else if (iconString.equals("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night;
        }

        return iconId;
    }

}
