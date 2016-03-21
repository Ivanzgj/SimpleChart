package com.ivan.chart.simplechart;

import android.app.Application;

/**
 * app context
 * Created by Ivan on 16/3/19.
 */
public class AppContext extends Application {

    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Application getInstance() {
        return instance;
    }

    public static int dp2px(float dpvalue) {
        return (int) (dpvalue
                * getInstance().getResources().getDisplayMetrics().density + 0.5f);
    }
}
