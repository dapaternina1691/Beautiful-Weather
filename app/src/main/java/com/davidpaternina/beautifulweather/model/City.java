package com.davidpaternina.beautifulweather.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by David on 14-May-15.
 */
public class City {

    private double mLatitude;
    private double mLongitude;
    private String mName;
    private Forecast mForecast;

    public City() {

    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Forecast getForecast() {
        return mForecast;
    }

    public void setForecast(Forecast forecast) {
        mForecast = forecast;
    }


}
