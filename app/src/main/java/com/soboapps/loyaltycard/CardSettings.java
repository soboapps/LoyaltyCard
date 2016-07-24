package com.soboapps.loyaltycard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;

public class CardSettings extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private boolean cardPrefChanged = true;

    public static final String TAG = "tag";
    public static SharedPreferences prefs;

    Preference reset;
    Preference t;
    PreferenceCategory n;
    PreferenceCategory sn;

    public String mLogoTitle;
    Preference cardNamePref;
    String cardNamePrefSummary;
    String themeNamePref;
    String themeName;
    String cardNameSummary;

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

    public static SharedPreferences settings;
    public static String themePref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the Current theme that is set
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        themePref = (settings.getString("theme", "defaultTheme"));
        if (themePref.equals("defaultTheme")) {
            setTheme(R.style.AppTheme);
        } else if (themePref.equals("icecreamTheme")) {
            setTheme(R.style.AppIceCreamTheme);
        } else if (themePref.equals("coffeeTheme")) {
            setTheme(R.style.AppCoffeeTheme);
        } else if (themePref.equals("smoothieTheme")) {
            setTheme(R.style.AppSmoothieTheme);
        } else if (themePref.equals("sandwichTheme")) {
            setTheme(R.style.AppSandwichTheme);
        } else if (themePref.equals("muffinTheme")) {
            setTheme(R.style.AppMuffinTheme);
        }
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

        prefs = this.getSharedPreferences("com.soboapps.punchcard", Context.MODE_PRIVATE);
        settings = PreferenceManager.getDefaultSharedPreferences(this);


        cardNamePref = (Preference)findPreference("loyaltyCardOneNamePref");
        cardNamePrefSummary = prefs.getString("c", cardNameSummary);
        themeNamePref = prefs.getString("themePref", themeNamePref);
        cardNamePref.setSummary(cardNamePrefSummary);

        mLogoTitle = String.valueOf(cardNamePref);
        themeName = String.valueOf(themeNamePref);
        //cardNamePref.setText(prefs.getString("loyaltyCardOneNamePref", "My uKoo"));

        reset = findPreference("resetPrefs");

        //n = (PreferenceCategory)findPreference("NamePref");
        t = findPreference("ThemePref");

        mDialog = new AlertDialog.Builder(this).setNeutralButton(getString(R.string.ok), null).create();
        reset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Reset all of the Preferences
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.clear();
                                    editor.apply();
                                    SharedPreferences.Editor editor2 = settings.edit();
                                    editor2.clear();
                                    editor2.apply();

                                    Toast t = Toast.makeText(CardSettings.this.getApplicationContext(), getString(R.string.card_rest), Toast.LENGTH_SHORT);
                                    t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                                    t.show();

                                    Intent intent = new Intent("finish_activity");
                                    sendBroadcast(intent);

                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //Don't do anything, they said No, just close the Dialog Box
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(CardSettings.this);
                    builder.setMessage(getString(R.string.clear_all))
                            .setPositiveButton(getString(R.string.yes), dialogClickListener)
                            .setNegativeButton(getString(R.string.no), dialogClickListener)
                            .show();

                return false;
            }
        });

        /*
        n.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference,
                                              Object newValue) {

                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putString(mLogoTitle, "My uKoo");
                prefsEditor.apply();

                return true;
            }
        });
        */
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
                    editTextPreference.setSummary(getString(R.string.no_card));
                }
            }
        }
    }
}