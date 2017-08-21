package ar.com.netmefy.netmefy.services;

import android.app.Application;
import android.content.Context;

/**
 * Created by ignac on 21/8/2017.
 */

public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}
