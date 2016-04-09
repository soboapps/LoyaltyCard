package com.soboapps.loyaltycard;

import android.app.Application;
import android.content.Context;

/**
 * This class is used to get the Application context and to be
 * able to use it anywhere in any other class that does not have
 * a Context.  Now every where call   MyAppsContext.getAppContext()
 * to get the application context statically.
 */

public class MyAppsContext extends Application {

    private static Context context;

    public void onCreate(){
        super.onCreate();
        MyAppsContext.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyAppsContext.context;
    }
}

