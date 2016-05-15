package com.soboapps.loyaltycard;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
public class ChangeThemeActivity extends Activity implements OnClickListener {

    //public static SharedPreferences prefs;
    public SharedPreferences settings;
    //SharedPreferences preferences = getSharedPreferences("user_preferences.xml", 0);
    public SharedPreferences.Editor editor;

    public static String defaultTheme;
    public static String icecreamTheme;
    public static String coffeeTheme;
    public static String smoothieTheme;
    public static String sandwichTheme;
    public static String muffinTheme;

    MainActivity mainActivity;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        //prefs = PreferenceManager.getDefaultSharedPreferences(this);
        settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.change_theme);

        findViewById(R.id.imageButton0).setOnClickListener(this);
        findViewById(R.id.imageButton1).setOnClickListener(this);
        findViewById(R.id.imageButton2).setOnClickListener(this);
        findViewById(R.id.imageButton3).setOnClickListener(this);
        findViewById(R.id.imageButton4).setOnClickListener(this);
        findViewById(R.id.imageButton5).setOnClickListener(this);
    }
    @Override
    public void onClick(View v)
    {
        Intent i = new Intent(this, MainActivity.class);
        final Context context = this;
        // TODO Auto-generated method stub
        switch (v.getId())
        {

            case R.id.imageButton0:
                Utils.changeToTheme(this, Utils.THEME_DEFAULT);
                settings.edit().putString("theme", "defaultTheme").apply();
                //startActivity(new Intent(this, MainActivity.class));
                //i=new Intent(this, MainActivity.class);

                //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(i);

                //i.addCategory(Intent.CATEGORY_HOME);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //startActivity(i);
                //ChangeThemeActivity.this.finish();
                //Intent myIntent2 = new Intent(getApplicationContext(), ChangeThemeActivity.class);
                //ChangeThemeActivity.this.finish(myIntent2);
                //finish();

                //Intent intent = new Intent(context, MainActivity.class);
                //startActivity(intent);
                //finish();

                break;
            case R.id.imageButton1:
                Utils.changeToTheme(this, Utils.THEME_ICE_CREAM);
                editor = settings.edit();
                settings.edit().putString("theme", "icecreamTheme").apply();
                //startActivity(new Intent(this, MainActivity.class));
                //i=new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                break;
            case R.id.imageButton2:
                Utils.changeToTheme(this, Utils.THEME_COFFEE);
                settings.edit().putString("theme", "coffeeTheme").apply();
                //startActivity(new Intent(this, MainActivity.class));
                //i=new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                break;
            case R.id.imageButton3:
                Utils.changeToTheme(this, Utils.THEME_SMOOTHIE);
                settings.edit().putString("theme", "smoothieTheme").apply();
                startActivity(new Intent(this, MainActivity.class));
                //i=new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                break;
            case R.id.imageButton4:
                Utils.changeToTheme(this, Utils.THEME_SANDWICH);
                settings.edit().putString("theme", "sandwichTheme").apply();
                startActivity(new Intent(this, MainActivity.class));
                //i=new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                break;
            case R.id.imageButton5:
                Utils.changeToTheme(this, Utils.THEME_MUFFIN);
                settings.edit().putString("theme", "muffinTheme").apply();
                startActivity(new Intent(this, MainActivity.class));
                //i=new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                break;
            //case R.id.imageButton5:
            //    Utils.changeToTheme(this, Utils.THEME_CAR);
            //    settings.edit().putString("theme", "carTheme").apply();
            //    break;

        }

        //Intent setIntent = new Intent(Intent.ACTION_MAIN);
        //setIntent.addCategory(Intent.CATEGORY_HOME);
        //setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(setIntent);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        ChangeThemeActivity.this.finish();

        //AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, PendingIntent.getActivity(this.getBaseContext(), 0, new Intent(getIntent()), getIntent().getFlags()));
        //android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void restartActivity(){
        MagicAppRestart.doRestart(mainActivity);
    }

    public void reload() {
        Intent mStartActivity = new Intent(this, MainActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);

        System.exit(0);
    }
}