package com.soboapps.loyaltycard;
import android.app.Activity;
import android.content.Intent;

public class Utils {
    private static int sTheme;
    public final static int THEME_DEFAULT = 0;
    public final static int THEME_ICE_CREAM = 1;
    public final static int THEME_COFFEE = 2;
    public final static int THEME_SMOOTHIE = 3;
    public final static int THEME_SANDWICH = 4;
    public final static int THEME_MUFFIN = 5;

    public static String defaultTheme;
    public static String icecreamTheme;
    public static String coffeeTheme;
    public static String smoothieTheme;
    public static String sandwichTheme;
    public static String muffinTheme;

    public MainActivity activity;
    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(Activity activity, int theme)
    {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }
    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity) {
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);

        switch (sTheme)
        {
            default:
            case THEME_DEFAULT:
                activity.setTheme(R.style.AppTheme);

                //prefs.edit().putString("theme", defaultTheme).apply();
                break;
            case THEME_ICE_CREAM:
                activity.setTheme(R.style.AppIceCreamTheme);
                //prefs.edit().putString("theme", icecreamTheme).apply();
                break;
            case THEME_COFFEE:
                activity.setTheme(R.style.AppCoffeeTheme);
                //prefs.edit().putString("theme", coffeeTheme).apply();
                break;
            case THEME_SMOOTHIE:
                activity.setTheme(R.style.AppSmoothieTheme);
                //prefs.edit().putString("theme", smoothieTheme).apply();
                break;
            case THEME_SANDWICH:
                activity.setTheme(R.style.AppSandwichTheme);
                //prefs.edit().putString("theme", sandwichTheme).apply();
                break;
            case THEME_MUFFIN:
                activity.setTheme(R.style.AppMuffinTheme);

                //prefs.edit().putString("theme", carTheme).apply();
                break;
        }
    }


}
