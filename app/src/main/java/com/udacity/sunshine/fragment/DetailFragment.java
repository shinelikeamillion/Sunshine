package com.udacity.sunshine.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.sunshine.R;


/**
 * 天气详情
 */
public class DetailFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    private static final String FORECAST_SHARE_HASHTAG = "#SunshineApp";

    private String mForecastStr;

    public DetailFragment () {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // The detail Activity called via intent.  Inspect(检查) the intent for forecast data.
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            mForecastStr = intent.getDataString();
        }

        if (null != mForecastStr) {
            ((TextView) rootView.findViewById(R.id.detail_text)).setText(mForecastStr);
        }

        return rootView;
    }
}
