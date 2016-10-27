package com.udacity.sunshine.data;

import android.provider.BaseColumns;

/**
 * weather 契约类
 * Inner class that defines the contents of the location table
 */

public class WeatherContract {

    public static final class LocationEntry implements BaseColumns {

        public static final String TABLE_NAME = "location";

        // the location setting string is what be sent to openweathermap as the location query
        public static final String COLUMN_LOCATION_SETTING = "location_setting";

        // Human readable location string
        public static final String COLUMN_CITY_NAME = "city_name";

        // In order to uniquely(精确的) pinpoint(标记) the location on the map
        // when we launch the map intent, we store(存储) the latitude(纬度)
        // and longitude(经度) as returned by openweathermap
        public static final String COLUMN_COORD_LAT = "coord_lat";
        public static final String COLUMN_COORD_LONG = "coord_long";

    }

    public static final class WeatherEntry implements BaseColumns {

        public static final String TABLE_NAME = "weather";

    }

}
