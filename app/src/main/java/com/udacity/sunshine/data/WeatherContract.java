package com.udacity.sunshine.data;

import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * weather 契约类
 * Defines table and column for the weather database.
 */

public class WeatherContract {

    // To make it easy query for the exact(精确的) date, we normalize(标准化) all dates that
    // go into the database to the start fo the Julian day at UTC
    // TODO: 10/27/16 Take a look at here
    public static long normalizeDate (long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /*
        Inner class that defines the table contents of the location table
     */

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

    /* Inner the class defines the table contents of the weather table */
    public static final class WeatherEntry implements BaseColumns {

        public static final String TABLE_NAME = "weather";

        // Column with the foreign key into the location table.
        public static final String COLUMN_LOC_KEY = "location_id";

        // Date, stored as long in milliseconds(毫秒) since the epoch
        public static final String COLUMN_DATE = "date";

        // Weather id as returned by API, to identity the icon to be used
        public static final String COLUMN_WEATHER_ID = "weather_id";

        // Short description and long description of the weather, as provided by API.
        // e.g 'cleat' vs 'sky is clear'
        public static final String COLUMN_SHORT_DESC = "short_desc";

        // Min and max temperatures for the day (stored as floats)
        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_MAX_TEMP = "max";

        // Humidity(湿度) is stored as a float representing(代表) percentage(百分比)
        public static final String COLUMN_HUMIDITY = "humidity";

        // Pressure is stored as a float
        public static final String COLUMN_PRESSURE = "pressure";

        // Windspeed is stored as a float representing percentage
        public static final String COLUMN_WIND_SPEED = "wind";


        // Degress are meteorological(气象学) degress(角度) (e.g 0 is north, 180 is south) Stored as floats.
        public static final String COLUMN_DEGREES = "degress";

    }

}
