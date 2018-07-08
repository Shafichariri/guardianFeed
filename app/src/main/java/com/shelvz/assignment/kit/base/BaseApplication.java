package com.shelvz.assignment.kit.base;

import android.app.Application;

/**
 * Created by shafic on 1/5/17.
 */

public class BaseApplication extends Application {
    private static BaseApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    //region STATIC METHODS
    public static <T extends BaseApplication> T getInstance() {
        return (T) sInstance;
    }
    //endregion
}
