/*		 /*
 * Copyright (C) 2014 The Android Open Source Project		  * Copyright (C) 2014 The Android Open Source Project
 *		  *
 * Licensed under the Apache License, Version 2.0 (the "License");		  * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.		  * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at		  * You may obtain a copy of the License at
 *		  *
 *      http://www.apache.org/licenses/LICENSE-2.0		  *      http://www.apache.org/licenses/LICENSE-2.0
 *		  *
 * Unless required by applicable law or agreed to in writing, software		  * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,		  * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.		  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and		  * See the License for the specific language governing permissions and
 * limitations under the License.		  * limitations under the License.
 */
package com.udacity.sunshine.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.udacity.sunshine.R;
import com.udacity.sunshine.Utility;
import com.udacity.sunshine.adapter.ForecastAdapter;
import com.udacity.sunshine.data.WeatherContract.WeatherEntry;
import com.udacity.sunshine.task.FetchWeatherTask;

import java.text.SimpleDateFormat;


public class ForecastFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String TAG = this.getClass().getSimpleName();

    private ForecastAdapter mForecastAdapter;

    private static final int FORECAST_LOADER_ID = 0x001;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 下面这一行的操作是把 menu 的点击事件交给Fragment处理
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // The CursorAdapter will take data from our cursor and populate the listView.
        mForecastAdapter = new ForecastAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_forecast, container, false);

        ListView listView= (ListView) rootView.findViewById(R.id.listview_forecast);

        listView.setAdapter(mForecastAdapter);
        // TODO: 11/15/16 need to do some refactory 
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                String forecast = mForecastAdapter.getItem(position);
//                Intent intent = new Intent(getActivity(), DetailActivity.class)
//                        .putExtra(Intent.EXTRA_TEXT, forecast);
//                startActivity(intent);
//                }
//            });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(FORECAST_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 如果在AndroidManifest.xml给当前页面指定parent Activity的话
        // 系统则会自动处理Home Action的点击事件
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 更新天气
    private void updateWeather() {
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity());

        String location = Utility.getPreferredLocation(getActivity());

        weatherTask.execute(location);
    }

    /**
     * 格式化时间
     * @param time
     * @return
     */
    private String getReadableDateString (long time) {
        // 因为API返回的是一个unix 的时间戳( 以秒为单位 )
        // 为了将其转换为有效时间,我们需要将它转换为毫秒
        SimpleDateFormat shortenedDareFormat = new SimpleDateFormat("EEE MMMM dd");
        return shortenedDareFormat.format(time);
    }

    /**
     * 显示昼夜温差
     * @param high
     * @param low
     * @return
     */
    private String formatHighLows (double high, double low, String unitType) {

        String unit = " ˚C";
        if (unitType.endsWith(getString(R.string.pref_units_imperial))) {
            unit = "˚F";
            high = (high * 1.8) + 32;
            low = (low * 1.8) + 32;
        } else if (!unitType.endsWith(getString(R.string.pref_units_metric))) {
            Log.d(TAG, "Unit type not found: " + unitType);
        }
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" +roundedLow;
        return highLowStr + unit;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String locationSetting = Utility.getPreferredLocation(getActivity());

        // sort order : Ascending, by date.
        String sortOrder = WeatherEntry.COLUMN_DATE + " ASC";
        Uri weatherForLocationUri = WeatherEntry.buildWeatherLocationWithStartDate(locationSetting, System.currentTimeMillis());
        return new CursorLoader(
                getActivity(),
                weatherForLocationUri,
                null,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mForecastAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mForecastAdapter.swapCursor(null);
    }
}
