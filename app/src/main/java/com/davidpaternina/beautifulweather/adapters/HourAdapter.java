package com.davidpaternina.beautifulweather.adapters;

import android.content.Context;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.davidpaternina.beautifulweather.R;
import com.davidpaternina.beautifulweather.model.Hour;




/**
 * Created by David on 19-Apr-15.
 */
public class HourAdapter extends BaseAdapter {

    private Context mContext;
    private Hour[] mHours;

    public HourAdapter(Context context, Hour[] hours){
        mContext = context;
        mHours = hours;
    }

    @Override
    public int getCount() {
        return mHours.length;
    }

    @Override
    public Object getItem(int position) {
        return mHours[position];
    }

    @Override
    public long getItemId(int position) {
        return 0; //nope
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            //brand new
            convertView = LayoutInflater.from(mContext).inflate(R.layout.hourly_list_item, null);
            holder = new ViewHolder();
            holder.hourLabel = (TextView) convertView.findViewById(R.id.timeLabel);
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.iconImageView);
            holder.summaryLabel = (TextView) convertView.findViewById(R.id.summaryLabel);
            holder.temperatureLabel = (TextView) convertView.findViewById(R.id.temperatureLabel);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        Hour hour = mHours[position];
        holder.hourLabel.setText(hour.getHour());
        holder.iconImageView.setImageResource(hour.getIconId());
        holder.summaryLabel.setText(hour.getSummary());
        holder.temperatureLabel.setText(hour.getTemperature()+"");

        if (position == 0)
            convertView.setSelected(true);

        return convertView;
    }

    private static class ViewHolder {
        TextView hourLabel;
        ImageView iconImageView;
        TextView summaryLabel;
        TextView temperatureLabel;
    }
}
















