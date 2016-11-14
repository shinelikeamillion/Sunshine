package com.udacity.sunshine.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.sunshine.R;
import com.udacity.sunshine.Utility;
import com.udacity.sunshine.data.WeatherContract.WeatherEntry;


public class ForecastAdapter extends CursorAdapter {

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /* prepare the weather high/lows for presentation.
     */
    private String formatHighLows (double high, double low) {
        boolean isMetric = Utility.isMetric(mContext);
        String highLowStr = Utility.formatTemperature(high, isMetric) + "/" +Utility.formatTemperature(low, isMetric);
        return highLowStr;
    }

    /* This is ported(移植) from FetchWeatherTask -- but now we go straight from the cursor to the string.
     */
    private String convertCursorRowToUXFormat (Cursor cursor) {
        // get row indices(索引) for our cursor
        int idx_max_temp = cursor.getColumnIndex(WeatherEntry.COLUMN_MAX_TEMP);
        int idx_min_temp = cursor.getColumnIndex(WeatherEntry.COLUMN_MIN_TEMP);
        int idx_date = cursor.getColumnIndex(WeatherEntry.COLUMN_DATE);
        int idx_short_desc = cursor.getColumnIndex(WeatherEntry.COLUMN_SHORT_DESC);

        String highAndLow = formatHighLows(
                cursor.getDouble(idx_max_temp),
                cursor.getDouble(idx_min_temp));

        return Utility.formatDate(cursor.getLong(idx_date)) +
                        " - " +
                        cursor.getString(idx_short_desc) +
                        " - " +
                        highAndLow;
    }

    /* Remember that these views are reused(重复) as need.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_forecast, parent, false);
        return view;
    }

    /* This is where we fill_in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here -- just a TextView
        // we'll keep the UI functional with a simple (and slow!) binding.
        TextView tv = (TextView) view;
        tv.setText(convertCursorRowToUXFormat(cursor));
    }
}
