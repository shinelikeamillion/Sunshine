package com.udacity.sunshine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PlaceholderFragment extends Fragment {

    ArrayAdapter<String> mForecastAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 为istView创建一些虚假的数据
        String[] date = {
                "周一 9/3 - 雾霾 - 31/15",
                "周二 9/4 - 雾霾 - 27/18",
                "周三 9/5 - 雾霾 - 24/17",
                "周四 9/6 - 雾霾 - 23/16",
                "周五 9/7 - 小雨 - 21/8",
                "周六 9/8 - 晴朗 - 31/17",
                "周日 9/9 - 晴朗 - 31/18"
        };

        List<String> weekForecast = new ArrayList<>(Arrays.asList(date));

        mForecastAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecase_textview,
                weekForecast
        );

        View rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);

        ListView listView= (ListView) rootView.findViewById(R.id.listview_forecast);

        listView.setAdapter(mForecastAdapter);

        return rootView;
    }
}
