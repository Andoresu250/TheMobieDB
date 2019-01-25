package com.andoresu.themoviedb.utils;

public class Constants {
    public static long LOCATION_INTERVAL        = 5 * 1000;
    public static long FAST_LOCATION_INTERVAL   = 2 * 1000;

    public static final int REQUEST_PERMISSIONS = 1;
    public static String[] permissions = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.INTERNET
    };
}
