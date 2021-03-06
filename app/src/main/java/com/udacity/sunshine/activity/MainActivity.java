package com.udacity.sunshine.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.udacity.sunshine.R;
import com.udacity.sunshine.Utility;
import com.udacity.sunshine.fragment.DetailFragment;
import com.udacity.sunshine.fragment.ForecastFragment;
import com.udacity.sunshine.fragment.ForecastFragment.Callback;
import com.udacity.sunshine.sync.SunshineSyncAdapter;

public class MainActivity extends AppCompatActivity implements Callback{

    private final String TAG = this.getClass().getSimpleName();
    private String mLocation;
    private boolean mIsMetric;
    private boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocation = Utility.getPreferredLocation(this);
        mIsMetric = Utility.isMetric(this);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.weather_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-land). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        ForecastFragment forecastFragment = (ForecastFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_forecast);
        forecastFragment.setUseTodayLayout(!mTwoPane);

        SunshineSyncAdapter.initializeSyncAdapter(this);

        getSupportActionBar().setLogo(R.drawable.ic_logo);
        getSupportActionBar().setDisplayOptions(
                ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_USE_LOGO);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String location = Utility.getPreferredLocation(this);
        boolean isMetric = Utility.isMetric(this);
        // update the location in our second pane using the fragment manager
        ForecastFragment ff = (ForecastFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_forecast);
        // on big screen
        DetailFragment df = (DetailFragment) getSupportFragmentManager()
                .findFragmentByTag(DETAILFRAGMENT_TAG);
        if (location != null && !location.equals(mLocation)) {
            if (null != ff) {
                ff.onLocationChanged();
            }

            if (null != df) {
                df.onLocationChanged(location);
            }
            mLocation = location;
        }
        if (isMetric != mIsMetric) {
            if (null != ff) {
                ff.onIsMetricChanged();
            }

            if (null != df) {
                df.onLocationChanged(location);
            }
            mIsMetric = isMetric;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Uri dateUri) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, dateUri);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.weather_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .setData(dateUri);
            startActivity(intent);
        }
    }
}
