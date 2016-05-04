package com.davidpaternina.beautifulweather.layout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Parcelable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.davidpaternina.beautifulweather.R;
import com.davidpaternina.beautifulweather.adapters.DayAdapter;
import com.davidpaternina.beautifulweather.model.Day;
import com.davidpaternina.beautifulweather.model.Forecast;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class DailyForecastActivity extends ActionBarActivity implements ListView.OnItemClickListener{

    private Day[] mDays;

    private TextView mLocationLabel;

    @InjectView(R.id.dailyListView) ListView mListView;
//    @InjectView(android.R.id.empty) TextView mEmptyTextView;
    @InjectView(R.id.relativeLayoutDaily) RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);
        ButterKnife.inject(this);

        if (ViewConfiguration.get(this).hasPermanentMenuKey()){
            getSupportActionBar().hide();
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        mLocationLabel = (TextView) findViewById(R.id.locationLabel);

        Intent intent = getIntent();
        mLocationLabel.setText(intent.getStringExtra("location"));
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        String currentBg = intent.getStringExtra(MainActivity.CURRENT_BG);
        mRelativeLayout.setBackground(getResources().getDrawable(Forecast.getBgId(currentBg)));
        mDays = Arrays.copyOf(parcelables, parcelables.length,Day[].class);

        DayAdapter adapter = new DayAdapter(this, mDays);
        mListView.setAdapter(adapter);
//        mListView.setEmptyView(mEmptyTextView);
        mListView.setSelector(getResources().getDrawable(R.drawable.bg_list_key));
        mListView.setDividerHeight(0);
        mListView.setOnItemClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Log.d("This", "plop");
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.setSelected(true);
        Day d = mDays[position];
        mRelativeLayout.setBackground(getResources().getDrawable(Forecast.getBgId(d.getIcon())));
        Toast.makeText(this, d.getSummary(), Toast.LENGTH_SHORT).show();
    }
}
