package com.davidpaternina.beautifulweather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidpaternina.beautifulweather.R;
import com.davidpaternina.beautifulweather.model.Day;
import com.davidpaternina.beautifulweather.model.Forecast;


/**
 * Created by David on 19-Apr-15.
 */
public class DayAdapter extends BaseAdapter {

    private Context mContext;
    private Day[] mDays;

    public DayAdapter(Context context, Day[] days){
        mContext = context;
        mDays = days;
    }

    @Override
    public int getCount() {
        return mDays.length;
    }

    @Override
    public Object getItem(int position) {
        return mDays[position];
    }

    @Override
    public long getItemId(int position) {
        return 0; // Nope
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            //brand new
            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.iconImageView);
            holder.maxTemperatureLabel = (TextView) convertView.findViewById(R.id.maxTempLabel);
            holder.minTemperatureLabel = (TextView) convertView.findViewById(R.id.minTempLabel);
            holder.dayLabel = (TextView) convertView.findViewById(R.id.dayNameLabel);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Day day = mDays[position];
        holder.iconImageView.setImageResource(day.getIconId());
        holder.maxTemperatureLabel.setText(Forecast.convertToCelsius(day.getTemperatureMax())+"°");
        holder.minTemperatureLabel.setText(Forecast.convertToCelsius(day.getTemperatureMin())+"°");

        if (position == 0){
            holder.dayLabel.setText("Today");
        } else {
            holder.dayLabel.setText(day.getDayOfTheWeek());
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView iconImageView;
        TextView maxTemperatureLabel;
        TextView minTemperatureLabel;
        TextView dayLabel;
    }
}
