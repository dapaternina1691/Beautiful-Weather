package com.davidpaternina.beautifulweather.layout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.davidpaternina.beautifulweather.R;
import com.davidpaternina.beautifulweather.dialogs.AlertDialogFragment;
import com.davidpaternina.beautifulweather.location.GPSTracker;
import com.davidpaternina.beautifulweather.model.City;
import com.davidpaternina.beautifulweather.model.Current;
import com.davidpaternina.beautifulweather.model.Day;
import com.davidpaternina.beautifulweather.model.Forecast;
import com.davidpaternina.beautifulweather.model.Hour;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    public final static String TAG = MainActivity.class.getSimpleName();
    public final static String DAILY_FORECAST = "Daily forecast";
    public final static String HOURLY_FORECAST = "Hourly forecast";
    public final static String CURRENT_BG = "Current bg";



    @InjectView(R.id.locationLabel) TextView mLocation;
    @InjectView(R.id.dayAndTimeLabel) TextView mDayAndTime;

    @InjectView(R.id.rainChanceImage) protected ImageView rainChanceImage;
    @InjectView(R.id.rainChanceLabel) protected TextView rainChanceText;
    @InjectView(R.id.humidityImage) protected ImageView humidityImage;
    @InjectView(R.id.humidityLabel) protected TextView humidityText;
    @InjectView(R.id.windSpeedImage) protected ImageView windSpeedImage;
    @InjectView(R.id.windSpeedText) protected TextView windSpeedText;
    @InjectView(R.id.progressBar) protected ProgressBar mProgressBar;
    @InjectView(R.id.descriptionText) protected TextView mDescription;
    @InjectView(R.id.temperatureLabel) protected TextView mTemperature;
    @InjectView(R.id.maxTempTextView) protected TextView mMaxTemperature;
    @InjectView(R.id.minTempTextView) protected TextView mMinTemperature;
    @InjectView(R.id.maxTempLabel) protected TextView mMaxTempLabel;
    @InjectView(R.id.minTempLabel) protected TextView mMinTempLabel;
    @InjectView(R.id.degreeImageView) protected ImageView mDegreeImage;

    @InjectView(R.id.next48HoursButton) protected Button mHourlyButton;
    @InjectView(R.id.seeNextWeekButton) protected Button mWeekButton;

    @InjectView(R.id.statsLinearLayout) protected LinearLayout mStatsLayout;
    @InjectView(R.id.buttonsLinearLayout) protected LinearLayout mButtonsLayout;

    @InjectView(R.id.relativeLayout) protected RelativeLayout mLayout;

//    private double mLatitude;
//    private double mLongitude;
//    protected String mCurrentCity;
    private City mCurrentCity;
    private Forecast mForecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (ViewConfiguration.get(this).hasPermanentMenuKey()){

            getSupportActionBar().hide();
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        ButterKnife.inject(this);

        rainChanceImage.setOnClickListener(rainChanceListener());
        rainChanceText.setOnClickListener(rainChanceListener());
        humidityImage.setOnClickListener(humidityListener());
        humidityText.setOnClickListener(humidityListener());
        windSpeedImage.setOnClickListener(windSpeedListener());
        windSpeedText.setOnClickListener(windSpeedListener());

        mProgressBar.setVisibility(View.INVISIBLE);

        mCurrentCity = new City();
        getData(true);

        mHourlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HourlyForecastActivity.class);
                intent.putExtra(HOURLY_FORECAST, mForecast.getHourlyForecast());
                intent.putExtra(CURRENT_BG, mForecast.getCurrent().getIcon());
                startActivity(intent);
            }
        });


        mWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DailyForecastActivity.class);
                intent.putExtra(DAILY_FORECAST, mForecast.getDailyForecast());
                intent.putExtra("location", mCurrentCity.getName());
                intent.putExtra(CURRENT_BG, mForecast.getCurrent().getIcon());
                startActivity(intent);
            }
        });

    }

    private void getData(final boolean finishAct) {
        if (isNetworkAvailable()) {

            mLocation.setVisibility(View.INVISIBLE);
            mDayAndTime.setVisibility(View.INVISIBLE);
            mDescription.setVisibility(View.INVISIBLE);
            mTemperature.setVisibility(View.INVISIBLE);
//        mMaxTempLabel.setVisibility(View.INVISIBLE);
//        mMaxTemperature.setVisibility(View.INVISIBLE);
//        mMinTempLabel.setVisibility(View.INVISIBLE);
//        mMinTemperature.setVisibility(View.INVISIBLE);
            mDegreeImage.setVisibility(View.INVISIBLE);
            mStatsLayout.setVisibility(View.INVISIBLE);
            mButtonsLayout.setVisibility(View.INVISIBLE);

            setLocation();
            getForecast(mCurrentCity.getLatitude(), mCurrentCity.getLongitude());
        } else{
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getString(R.string.no_network_title));
            alertDialog.setMessage(getString(R.string.no_network_message));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok_string),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (finishAct)
                                finish();
                        }
                    });
            alertDialog.show();
        }
    }

    private void setLocation() {
        GPSTracker gps = new GPSTracker(this);

        if (gps.canGetLocation()){
            mCurrentCity.setLatitude(gps.getLatitude());
            mCurrentCity.setLongitude(gps.getLongitude());

            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(mCurrentCity.getLatitude(), mCurrentCity.getLongitude(), 1);
            } catch (IOException e) {
                Log.d(TAG, "ERROR", e);
            }
            if (addresses.size() > 0) {
                mCurrentCity.setName(addresses.get(0).getLocality());
                Log.d(TAG, addresses.get(0).getSubLocality() + " locality");
            }
            gps.stopUsingGPS();
        } else {
            gps.showSettingsAlert();
        }
    }

    private void getForecast(double latitude, double longitude) {
        String APIKey = "API_KEY";
        String foreCastURL = "https://api.forecast.io/forecast/"+APIKey+"/"+latitude+","+longitude;

        if(isNetworkAvailable()) {
            toggleRefresh();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(foreCastURL)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mForecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught", e);
                    }
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.network_unavailable_message), Toast.LENGTH_LONG).show();
        }
    }

    private void updateDisplay() {

        mLocation.setVisibility(View.VISIBLE);
        mDayAndTime.setVisibility(View.VISIBLE);
        mDescription.setVisibility(View.VISIBLE);
        mTemperature.setVisibility(View.VISIBLE);
//        mMaxTempLabel.setVisibility(View.VISIBLE);
//        mMaxTemperature.setVisibility(View.VISIBLE);
//        mMinTempLabel.setVisibility(View.VISIBLE);
//        mMinTemperature.setVisibility(View.VISIBLE);
        mDegreeImage.setVisibility(View.VISIBLE);
        mStatsLayout.setVisibility(View.VISIBLE);
        mButtonsLayout.setVisibility(View.VISIBLE);


        mLayout.setBackgroundDrawable(getResources().getDrawable(Forecast.getBgId(mForecast.getCurrent().getIcon())));
        //mLayout.setBackground(getResources().getDrawable(mCurrent.getBgId(mCurrent.getIcon())));
        mLocation.setText(mCurrentCity.getName());
        mDayAndTime.setText(mForecast.getCurrent().getFormattedTime());
        mDescription.setText(mForecast.getCurrent().getSummary());
        mTemperature.setText(Forecast.convertToCelsius(mForecast.getCurrent().getTemperature())+"");

//        mMaxTemperature.setText(Forecast.convertToCelsius(mForecast.getCurrentDayForecast().getTemperatureMax())+"°");
//        mMinTemperature.setText(Forecast.convertToCelsius(mForecast.getCurrentDayForecast().getTemperatureMin())+"°");
        humidityText.setText(mForecast.getCurrent().getHumidity()+"");
        windSpeedText.setText(mForecast.getCurrent().getWindSpeed()+"m/s");
        rainChanceText.setText(mForecast.getCurrent().getPrecipChance()+"%");
    }


    private Forecast parseForecastDetails(String jsonData) throws JSONException {
        Forecast forecast = new Forecast();

        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setHourlyForecast(getHourlyForecast(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));

        return forecast;
    }

    private Day[] getDailyForecast(String jsonData) throws  JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        Day[] days = new Day[data.length()];

        for (int i = 0; i < data.length(); i++){
            JSONObject jsonDay = data.getJSONObject(i);
            Day day = new Day();

            day.setSummary(jsonDay.getString("summary"));
            day.setIcon(jsonDay.getString("icon"));
            day.setTemperatureMax(jsonDay.getDouble("temperatureMax"));
            day.setTemperatureMin(jsonDay.getDouble("temperatureMin"));
            day.setTime(jsonDay.getLong("time"));
            day.setTimezone(timezone);

            days[i] = day;
        }
        return days;
    }

    private Hour[] getHourlyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        Hour[] hours = new Hour[data.length()];

        for (int i = 0; i < data.length(); i++){
            JSONObject jsonHour = data.getJSONObject(i);
            Hour hour = new Hour();

            hour.setSummary(jsonHour.getString("summary"));
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setIcon(jsonHour.getString("icon"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setTimezone(timezone);

            hours[i] = hour;
        }
        return hours;
    }

    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSOM: " + timezone);

        JSONObject currently = forecast.getJSONObject("currently");

        Current current = new Current();
        current.setHumidity(currently.getDouble("humidity"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setWindSpeed(currently.getDouble("windSpeed"));
        current.setTimezone(timezone);

        Log.d(TAG, current.getFormattedTime());

        return current;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(),"error_dialog");
    }

    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE){
            mProgressBar.setVisibility(View.VISIBLE);
            //mRefreshImageView.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            //mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }


    public View.OnClickListener windSpeedListener(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, String.format(getString(R.string.wind_speed_string), windSpeedText.getText().toString()), Toast.LENGTH_SHORT)
                        .show();
            }
        };
        return listener;
    }

    public View.OnClickListener rainChanceListener(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, String.format(getString(R.string.rain_chance_string), rainChanceText.getText().toString()), Toast.LENGTH_SHORT)
                        .show();
            }
        };
                return listener;
    }

    public View.OnClickListener humidityListener(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, String.format(getString(R.string.humidity_string), humidityText.getText().toString()), Toast.LENGTH_SHORT)
                        .show();
            }
        };
        return listener;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_refresh) {
            getData(false);
        }

        return super.onOptionsItemSelected(item);
    }

}
