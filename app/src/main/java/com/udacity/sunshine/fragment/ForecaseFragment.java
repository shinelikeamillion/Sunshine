package com.udacity.sunshine.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.udacity.sunshine.R;
import com.udacity.sunshine.activity.DetailActivity;
import com.udacity.sunshine.task.FetchWeatherTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class ForecaseFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    ArrayAdapter<String> mForecastAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 为istView创建一些虚假的数据
//        String[] date = {
//                "周一 9/3 - 雾霾 - 31/15",
//                "周二 9/4 - 雾霾 - 27/18",
//                "周三 9/5 - 雾霾 - 24/17",
//                "周四 9/6 - 雾霾 - 23/16",
//                "周五 9/7 - 小雨 - 21/8",
//                "周六 9/8 - 晴朗 - 31/17",
//                "周日 9/9 - 晴朗 - 31/18"
//        };
//
//        List<String> weekForecast = new ArrayList<>(Arrays.asList(date));

        mForecastAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecase_textview,
                new ArrayList<String>()
        );

        View rootView = inflater.inflate(R.layout.fragment_forecast, container, false);

        ListView listView= (ListView) rootView.findViewById(R.id.listview_forecast);

        listView.setAdapter(mForecastAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String forecast = mForecastAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 下面这一行的操作是把 menu 的点击事件交给Fragment处理
        setHasOptionsMenu(true);
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
        // 如果在AndroidMainfest.xml给当前页面指定parent Activity的话
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
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity(), mForecastAdapter);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));

        weatherTask.execute(location);
    }

//    private class FetchWeatherTask extends AsyncTask<String, Void, String[]> {
//
//        private final String TAG = this.getClass().getSimpleName();
//
//        @Override
//        protected String[] doInBackground(String... params) {
//
//            if (params.length == 0) {
//                // 没有参数,不做请求
//                return null;
//            }
//
//            return getWeather(params[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String[] result) {
//            if (result != null) {
//                mForecastAdapter.clear();
//                for (String dayForecastStr : result) {
//                    mForecastAdapter.add(dayForecastStr);
//                }
//                // or use the addAll method here :)
//            }
//        }
//
//        // getWeather from Api
//        String[] getWeather (String postCode) {
//
//            final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
//            final String QUERY_PARAM = "q";
//            final String FORMAT_PARAM = "mode";
//            final String UNITS_PARAM = "units";
//            final String DAYS_PARAM = "cnt";
//            final String APPID_PARAM = "APPID";
//
//            String format = "json";
//            String units = "metric";
//            int numDays = 7;
//
//            // 这两个参数定义在try/catch 外面目的是为了之后的关闭操作
//            HttpURLConnection urlConnection = null;
//            BufferedReader reader = null;
//
//            // Api 返回的JSON的字符串
//            String forecastJsonStr = null;
//
//            try {
//                // 定义URL 从API请求数据
//                Uri builUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
//                        .appendQueryParameter(QUERY_PARAM, postCode)
//                        .appendQueryParameter(FORMAT_PARAM, format)
//                        .appendQueryParameter(UNITS_PARAM, units)
//                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
//                        .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
//                        .build();
//
//                URL url = new URL(builUri.toString());
//
//                // 创建请求并打开连接
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("GET");
//                urlConnection.connect();
//
//                // 读取返回的数据流
//                InputStream inputStream = urlConnection.getInputStream();
//                StringBuffer buffer = new StringBuffer();
//                if (inputStream == null) {
//                    // 什么都不用做
//                    forecastJsonStr = null;
//                }
//                reader = new BufferedReader(new InputStreamReader(inputStream));
//
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    // 似乎因为它是json 的字符串所以添加一个换行是没有必要的 (当然添加也并不会影响JSON的解析)
//                    // 但是添加一个"\n"会对我们的调试会非常有利 :)
//                    buffer.append(line + "\n");
//                }
//
//                if(buffer.length() == 0) {
//                    // 数据流为空,不再进行解析
//                    forecastJsonStr = null;
//                }
//
//                forecastJsonStr = buffer.toString();
//            } catch (Exception e) {
//                Log.e(TAG, "出错啦!", e);
//                // 如果我没获取数据失败的话,就没有必要再去解析它
//                forecastJsonStr = null;
//            } finally {
//                // 关闭建立的链接
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (IOException e) {
//                        Log.e(TAG, "关闭Reader流的时候出错啦!", e);
//                    }
//                }
//            }
//
//            try {
//                return getWeatherDataFromJson(forecastJsonStr, numDays);
//            } catch (JSONException e) {
//                Log.e(TAG, e.getMessage(), e);
//                e.printStackTrace();
//            }
//
//            // 这种情况发生在获取或者解析天气的时候
//            return null;
//        }
//    }

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

//    private String[] getWeatherDataFromJson (String forecastJsonStr, int numDays)
//            throws JSONException {
//        // 提取 JSON 对象的键名
//        final String OWM_LIST= "list";
//        final String OWM_WEATHER= "weather";
//        final String OWM_TEMPERATURE = "temp";
//        final String OWM_MAX = "max";
//        final String OWM_MIN = "min";
//        final String OWM_DESCRIPTION = "main";
//
//        JSONObject forecastJson = new JSONObject(forecastJsonStr);
//        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);
//
//        android.text.format.Time dayTime = new android.text.format.Time();
//        dayTime.setToNow();
//
//        // 我们从本地时间开始算第一天
//        int julianStartDay = android.text.format.Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);
//
//        dayTime = new android.text.format.Time();
//
//        String[] resultStrs = new String[numDays];
//
//        SharedPreferences sharedPreferences = PreferenceManager
//                .getDefaultSharedPreferences(getActivity());
//        String unitType = sharedPreferences.getString(
//                getString(R.string.pref_units_key),
//                getString(R.string.pref_units_metric)
//        );
//
//        for (int i = 0; i < weatherArray.length(); i++) {
//            // 现在,使用 " 日期, 描述, 最高/最低 "的格式
//            String day;
//            String description;
//            String highAndLow;
//
//            // 获取代表一天的 JSON 对象
//            JSONObject dayForecast = weatherArray.getJSONObject(i);
//
//            // 将长整形的时间转变为方便理解的格式
//            long dateTime;
//            dateTime = dayTime.setJulianDay(julianStartDay + i);
//            day = getReadableDateString(dateTime);
//
//            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
//            description = weatherObject.getString(OWM_DESCRIPTION);
//
//            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
//            double high = temperatureObject.getDouble(OWM_MAX);
//            double low = temperatureObject.getDouble(OWM_MIN);
//
//            highAndLow = formatHighLows(high, low, unitType);
//            resultStrs[i] = day + " - " + description + " - " + highAndLow;
//        }
//
//        for (String s: resultStrs) {
//            Log.v(TAG, "Forecast entry:" + s);
//        }
//        return  resultStrs;
//    }

}
