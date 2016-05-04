package com.davidpaternina.beautifulweather.layout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.davidpaternina.beautifulweather.R;
import com.davidpaternina.beautifulweather.adapters.HourAdapter;
import com.davidpaternina.beautifulweather.model.Forecast;
import com.davidpaternina.beautifulweather.model.Hour;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HourlyForecastActivity extends ActionBarActivity implements AbsListView.OnScrollListener, ListView.OnItemClickListener {

    private final static String TAG = HourlyForecastActivity.class.getSimpleName();

    private Hour[] mHours;

    @InjectView(R.id.hourListView) ListView mListView;
    @InjectView(R.id.chart) LineChart mLineChart;
    @InjectView(R.id.relativeLayoutHourly) RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);

        if (ViewConfiguration.get(this).hasPermanentMenuKey()){

            getSupportActionBar().hide();
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        ButterKnife.inject(this);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.HOURLY_FORECAST);
        String currentBg = intent.getStringExtra(MainActivity.CURRENT_BG);
        mRelativeLayout.setBackground(getResources().getDrawable(Forecast.getBgId(currentBg)));
        mHours = Arrays.copyOf(parcelables, parcelables.length, Hour[].class);

        //------------------------------------------------------------------------------------------

        mLineChart.setDrawGridBackground(false);
        mLineChart.setDrawBorders(false);
        mLineChart.setDrawMarkerViews(false);
//        mLineChart.setDragEnabled(false);
//        mLineChart.setScaleEnabled(false);
        mLineChart.setTouchEnabled(false);
//        mLineChart.setBackgroundColor(getResources().getColor(R.color.semi_transparent));
        mLineChart.setDescription("");

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);

        YAxis leftAxis = mLineChart.getAxisLeft();
        YAxis rightAxis = mLineChart.getAxisRight();
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);
        mLineChart.getLegend().setEnabled(false);
//        mLineChart.setScaleMinima(1f,3f);
//        mLineChart.centerViewTo(10,10, YAxis.AxisDependency.LEFT);

        ArrayList<Entry> valsComp1 = new ArrayList<Entry>();
        //ArrayList<Entry> valsComp2 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < 49; i++){
//            Random rand = new Random();

            // nextInt is normally exclusive of the top value,
            // so add 1 to make it inclusive
            //int randomNum = rand.nextInt((24 - 5) + 1) + 5;
            double temp = mHours[i].getTemperature();
            Entry e = new Entry((float) temp,i);
            valsComp1.add(e);
            xVals.add(i+"");
        }

//        Entry c1e1 = new Entry(100.000f, 0); // 0 == quarter 1
//        valsComp1.add(c1e1);
//        Entry c1e2 = new Entry(50.000f, 1); // 1 == quarter 2 ...
//        valsComp1.add(c1e2);
//        Entry c1e3 = new Entry(60.000f, 2); // 1 == quarter 2 ...
//        valsComp1.add(c1e3);
//        Entry c1e4 = new Entry(80.000f, 3); // 1 == quarter 2 ...
//        valsComp1.add(c1e4);
        // and so on ...

//        Entry c2e1 = new Entry(120.000f, 0); // 0 == quarter 1
//        valsComp2.add(c2e1);
//        Entry c2e2 = new Entry(110.000f, 1); // 1 == quarter 2 ...
//        valsComp2.add(c2e2);

        LineDataSet set1 = new LineDataSet(valsComp1, "Company 1");
        set1.setDrawCubic(true);
//        set1.enableDashedLine(10f, 5f, 0f);
        set1.setColor(Color.WHITE);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setDrawCircles(false);

//        set1.setCircleSize(3f);
//        set1.setDrawCircleHole(false);
        set1.setDrawValues(false);
//        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);
        set1.setDrawFilled(true);
        set1.setFillColor(Color.BLACK);
        set1.setHighLightColor(Color.WHITE);

        //LineDataSet setComp2 = new LineDataSet(valsComp2, "Company 2");

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1);

        //dataSets.add(setComp2);



        //xVals.add("1.Q"); xVals.add("2.Q"); xVals.add("3.Q"); xVals.add("4.Q");

        LineData data = new LineData(xVals, dataSets);
        mLineChart.setData(data);
        mLineChart.setVisibleXRange(mLineChart.getXChartMax());
        mLineChart.setVisibleYRange(mLineChart.getYMax() - mLineChart.getYMin() + 0.5f, YAxis.AxisDependency.LEFT);
        mLineChart.centerViewTo(12, mLineChart.getYMin()+((mLineChart.getYMax()-mLineChart.getYMin())/2), YAxis.AxisDependency.LEFT);
        mLineChart.setHighlightIndicatorEnabled(true);
        mLineChart.animateY(1000);
//        mLineChart.invalidate(); // refresh





        //------------------------------------------------------------------------------------------


        HourAdapter adapter = new HourAdapter(this, mHours);

        mListView.setAdapter(adapter);
        mListView.setDividerHeight(0);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Toast.makeText(HourlyForecastActivity.this, "plop", Toast.LENGTH_LONG).show();
            }
        });
        mListView.setFriction(ViewConfiguration.getScrollFriction() * 1.5f);
        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mListView.setSelector(getResources().getDrawable(R.drawable.bg_list_key));

        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);

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
    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        mListView.smoothScrollToPosition(mListView.getFirstVisiblePosition());

//        mListView.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
        Hour h = (Hour) mListView.getItemAtPosition(mListView.getFirstVisiblePosition()+4);
        Log.d(TAG, h.getHour());
//        mLineChart.highlightValue(mListView.getFirstVisiblePosition(),0);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mLineChart.highlightValue(firstVisibleItem, 0);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.setSelected(true);
        Hour h = mHours[position];
        mRelativeLayout.setBackground(getResources().getDrawable(Forecast.getBgId(h.getIcon())));
        int visible = mListView.getLastVisiblePosition() - mListView.getFirstVisiblePosition();


        mListView.smoothScrollToPositionFromTop(position, 5, 500);


//        mListView.smoothScrollToPosition(mListView.getLastVisiblePosition()+(position-mListView.getFirstVisiblePosition()));
        mLineChart.highlightValue(position,0);
    }
}














