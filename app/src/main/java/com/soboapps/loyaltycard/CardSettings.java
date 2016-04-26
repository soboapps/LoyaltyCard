package com.soboapps.loyaltycard;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by sdmei on 3/31/2016.
 */
public class CardSettings extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private boolean cardPrefChanged = true;

    public static final String TAG = "tag";
    public static SharedPreferences prefs;

    //Preference cn;
    Preference reset;
    PreferenceCategory n;
    PreferenceCategory sn;

    public String mLogoTitle;
    EditTextPreference cardNamePref;
    String cardNamePrefSummary;
    String cardNameSummary;
    //EditTextPreference cn;

    ImageView mStar1ImageView;
    ImageView mStar2ImageView;
    ImageView mStar3ImageView;
    ImageView mStar4ImageView;
    ImageView mStar5ImageView;
    ImageView mStar6ImageView;
    ImageView mStar7ImageView;
    ImageView mStar8ImageView;
    ImageView mStar9ImageView;

    private AlertDialog mDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.card_settings);

        mStar1ImageView = (ImageView) findViewById(R.id.img_star_1);
        mStar2ImageView = (ImageView) findViewById(R.id.img_star_2);
        mStar3ImageView = (ImageView) findViewById(R.id.img_star_3);
        mStar4ImageView = (ImageView) findViewById(R.id.img_star_4);
        mStar5ImageView = (ImageView) findViewById(R.id.img_star_5);
        mStar6ImageView = (ImageView) findViewById(R.id.img_star_6);
        mStar7ImageView = (ImageView) findViewById(R.id.img_star_7);
        mStar8ImageView = (ImageView) findViewById(R.id.img_star_8);
        mStar9ImageView = (ImageView) findViewById(R.id.img_star_9);

        //prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs = this.getSharedPreferences("com.soboapps.punchcard", Context.MODE_PRIVATE);


        cardNamePref = (EditTextPreference)findPreference("loyaltyCardOneNamePref");
        cardNamePrefSummary = prefs.getString("c", cardNameSummary);
        cardNamePref.setSummary(cardNamePrefSummary);
        //cardNamePref.setSummary(prefs.getString("loyaltyCardOneNamePref", "Serial Number:  " + MainActivity.sNum));

        mLogoTitle = String.valueOf(cardNamePref);

        //cn  = (EditTextPreference)findPreference("loyaltyCardOneNamePref");
        reset = findPreference("resetPrefs");

        //cn.setText(prefs.getString("loyaltyCardOneNamePref", "Loyalty Card"));



        n = (PreferenceCategory)findPreference("NamePref");




        mDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", null).create();

        //cn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

        //    @Override
        //    public boolean onPreferenceClick(Preference preference) {
        //        //startActivity(new Intent(Options.this, PlayerSetup.class));
        //        return true;
        //    }
        //});

        //sTagNum = prefs.getString("nfctagsn", null);

        //s1flag = prefs.getBoolean("s1selected", false);
        // Reset All the Options back to Default
        reset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //prefs = getPreferenceManager().getSharedPreferences();
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.clear();
                                    editor.apply();
                                    Toast t = Toast.makeText(CardSettings.this.getApplicationContext(), "Loyalty Card Has Been Reset", Toast.LENGTH_SHORT);
                                    t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                                    t.show();

                                    MainActivity.resetView();


                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //TODO No button clicked
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(CardSettings.this);
                    builder.setMessage("Clear All Credits and Start Over?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener)
                            .show();
                //reload();
                //restartActivity();

                return false;
            }
        });

        n.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference,
                                              Object newValue) {

                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putString(mLogoTitle, "Loyalty Card");
                prefsEditor.apply();

                //numOfPlayers = Integer.valueOf((String) newValue);

                //enablePrefs();
                //setUpListeners(true);

                return true;
            }
        });

    }

    public void restart(){
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.finishAffinity();
        }
    }

    public void restartActivity(){
        MagicAppRestart.doRestart(this);
    }

    public void reload() {
        Intent mStartActivity = new Intent(this, MainActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);

        System.exit(0);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(getClass().getName(),"cardPrefChanged= " + cardPrefChanged);
        outState.putBoolean("cardPrefChanged", cardPrefChanged);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //if(shaker != null)
        //  shaker.close();
    }

    @Override
    protected void onResume(){
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        updatePreference("loyaltyCardOneNamePref");
        updatePreference("c");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePreference(key);

    }

    private void updatePreference(String key) {
        if (key.equals("loyaltyCardOneNamePref")){
            Preference preference = findPreference(key);
            if (preference instanceof EditTextPreference){
                EditTextPreference editTextPreference =  (EditTextPreference)preference;
                if (editTextPreference.getText().trim().length() > 0){
                    editTextPreference.setTitle(editTextPreference.getText());
                    editTextPreference.setSummary(cardNamePrefSummary);
                }else{
                    editTextPreference.setSummary("Enter Current Name");
                }
            }
        }
    }
}