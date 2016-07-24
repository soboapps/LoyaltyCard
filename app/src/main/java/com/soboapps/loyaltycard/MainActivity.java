package com.soboapps.loyaltycard;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.result.ResultParser;
import com.jinlin.zxing.CaptureActivity;

import com.soboapps.loyaltycard.record.ParsedNdefRecord;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private static final DateFormat TIME_FORMAT = SimpleDateFormat.getDateTimeInstance();
    private LinearLayout mTagContent;
    private RelativeLayout mainRlayout;

    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private NdefMessage mNdefPushMessage;

    private AlertDialog mDialog;

    public static String myTagId;
    public static String mySTagUrl;
    public static String cardOneTitle;
    public static TextView logoTitleName;
    public static String themePref;

    public static ImageView mStar1ImageView;
    public static ImageView mStar2ImageView;
    public static ImageView mStar3ImageView;
    public static ImageView mStar4ImageView;
    public static ImageView mStar5ImageView;
    public static ImageView mStar6ImageView;
    public static ImageView mStar7ImageView;
    public static ImageView mStar8ImageView;
    public static ImageView mStar9ImageView;

    public static SharedPreferences prefs;

    public static boolean s1flag = false;
    public static boolean s2flag = false;
    public static boolean s3flag = false;
    public static boolean s4flag = false;
    public static boolean s5flag = false;
    public static boolean s6flag = false;
    public static boolean s7flag = false;
    public static boolean s8flag = false;
    public static boolean s9flag = false;

    public static String sNum;
    public static String sTagNum = null;
    public static String nfcTagSerialNum;
    public static String qrCodeText;

    public FloatingActionButton fab;

    private static final int REQUEST_CAMERA = 0x00000011;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the Current theme that is set
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        themePref = (prefs.getString("theme", "defaultTheme"));
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

        setContentView(R.layout.activity_main);

        mainRlayout = (RelativeLayout) findViewById(R.id.rLayout);
        View vLogo = findViewById(R.id.logo);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        mStar1ImageView = (ImageView) findViewById(R.id.img_star_1);
        mStar2ImageView = (ImageView) findViewById(R.id.img_star_2);
        mStar3ImageView = (ImageView) findViewById(R.id.img_star_3);
        mStar4ImageView = (ImageView) findViewById(R.id.img_star_4);
        mStar5ImageView = (ImageView) findViewById(R.id.img_star_5);
        mStar6ImageView = (ImageView) findViewById(R.id.img_star_6);
        mStar7ImageView = (ImageView) findViewById(R.id.img_star_7);
        mStar8ImageView = (ImageView) findViewById(R.id.img_star_8);
        mStar9ImageView = (ImageView) findViewById(R.id.img_star_9);

        if (themePref.equals("defaultTheme")) {
            ColorStateList rippleColor = ContextCompat.getColorStateList((MainActivity.this.getApplicationContext()), R.color.colorAccent);
            fab.setBackgroundTintList(rippleColor);

        } else if (themePref.equals("icecreamTheme")) {
            mainRlayout.setBackgroundColor(Color.parseColor("#F8BBD0"));
            GradientDrawable shape = (GradientDrawable) vLogo.getBackground().mutate();
            shape.setColor(Color.parseColor("#E91E63"));
            shape.setStroke(15, Color.parseColor("#795548"));
            shape.invalidateSelf();
            fab.setRippleColor(Color.parseColor("#795548"));
            mStar1ImageView.setImageResource(R.drawable.icecream);
        } else if (themePref.equals("coffeeTheme")) {
            mainRlayout.setBackgroundColor(Color.parseColor("#D7CCC8"));
            GradientDrawable shape = (GradientDrawable) vLogo.getBackground().mutate();
            shape.setColor(Color.parseColor("#795548"));
            shape.setStroke(15, Color.parseColor("#FF5722"));
            shape.invalidateSelf();
            fab.setRippleColor(Color.parseColor("#FF5722"));
        } else if (themePref.equals("smoothieTheme")) {
            mainRlayout.setBackgroundColor(Color.parseColor("#FFCDD2"));
            GradientDrawable shape = (GradientDrawable) vLogo.getBackground().mutate();
            shape.setColor(Color.parseColor("#F44336"));
            shape.setStroke(15, Color.parseColor("#FFC107"));
            shape.invalidateSelf();
            fab.setRippleColor(Color.parseColor("#FFC107"));
        } else if (themePref.equals("sandwichTheme")) {
            mainRlayout.setBackgroundColor(Color.parseColor("#C8E6C9"));
            GradientDrawable shape = (GradientDrawable) vLogo.getBackground().mutate();
            shape.setColor(Color.parseColor("#4CAF50"));
            shape.setStroke(15, Color.parseColor("#FFC107"));
            shape.invalidateSelf();
            fab.setRippleColor(Color.parseColor("#FFC107"));
        } else if (themePref.equals("muffinTheme")) {
            mainRlayout.setBackgroundColor(Color.parseColor("#D7CCC8"));
            GradientDrawable shape = (GradientDrawable) vLogo.getBackground().mutate();
            shape.setColor(Color.parseColor("#795548"));
            shape.setStroke(15, Color.parseColor("#512DA8"));
            shape.invalidateSelf();
            fab.setRippleColor(Color.parseColor("#512DA8"));

        }

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Click action to Scan QR Code
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 1);
            }
        });


        // This is used to test Logic and Flow - Remarked out when not debugging
        //Toast sn = Toast.makeText(MainActivity.this.getApplicationContext(), themePref, Toast.LENGTH_SHORT);
        //sn.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        //sn.show();

        mTagContent = (LinearLayout) findViewById(R.id.list);
        resolveIntent(getIntent());

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        registerReceiver(broadcast_reciever, new IntentFilter("finish_activity"));

        mDialog = new AlertDialog.Builder(this).setNeutralButton(getString(R.string.ok), null).create();

        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            showMessage(R.string.error, R.string.no_nfc);
            finish();
            return;
        }

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mNdefPushMessage = new NdefMessage(new NdefRecord[] { newTextRecord(
                getString(R.string.nfc_message), Locale.ENGLISH, true) });

        prefs = this.getSharedPreferences("com.soboapps.punchcard", Context.MODE_PRIVATE);

        sTagNum = prefs.getString("c", null);

        Typeface tf = Typeface.createFromAsset(getAssets(),"rumraisin.ttf");

        logoTitleName = (TextView)findViewById(R.id.logo_text);
        logoTitleName.setTypeface(tf,Typeface.BOLD);
        cardOneTitle = (prefs.getString("loyaltyCardOneNamePref", "My uKoo"));

        if (sTagNum == null){
            logoTitleName.setText("My uKoo");
        } else {
            logoTitleName.setText(sTagNum);
        }
        // This is used to test Logic and Flow - Remarked out when not debugging
        //Toast sn = Toast.makeText(MainActivity.this.getApplicationContext(), cardOneTitle, Toast.LENGTH_SHORT);
        //sn.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        //sn.show();

        s1flag = prefs.getBoolean("s1selected", false);
        s2flag = prefs.getBoolean("s2selected", false);
        s3flag = prefs.getBoolean("s3selected", false);
        s4flag = prefs.getBoolean("s4selected", false);
        s5flag = prefs.getBoolean("s5selected", false);
        s6flag = prefs.getBoolean("s6selected", false);
        s7flag = prefs.getBoolean("s7selected", false);
        s8flag = prefs.getBoolean("s8selected", false);
        s9flag = prefs.getBoolean("s9selected", false);

        //Check to see if the star is punched or not
        checkFlag();

        // Star Longpress
        mStar1ImageView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (s1flag == true && s2flag==false) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    s1flag = false;
                                    prefs.edit().putBoolean("s1selected", false).apply();
                                    checkFlag();
                                    onRestart();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(getString(R.string.remove_credit))
                            .setPositiveButton(getString(R.string.yes), dialogClickListener)
                            .setNegativeButton(getString(R.string.no), dialogClickListener)
                            .show();
                }
                return true;
            }
        });
        mStar2ImageView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (s2flag == true && s3flag==false) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    s2flag = false;
                                    prefs.edit().putBoolean("s2selected", false).apply();
                                    checkFlag();
                                    onRestart();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(getString(R.string.remove_credit))
                            .setPositiveButton(getString(R.string.yes), dialogClickListener)
                            .setNegativeButton(getString(R.string.no), dialogClickListener)
                            .show();
                }
                return true;
            }
        });
        mStar3ImageView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (s3flag == true && s4flag==false) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    s3flag = false;
                                    prefs.edit().putBoolean("s3selected", false).apply();
                                    checkFlag();
                                    onRestart();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(getString(R.string.remove_credit))
                            .setPositiveButton(getString(R.string.yes), dialogClickListener)
                            .setNegativeButton(getString(R.string.no), dialogClickListener)
                            .show();
                }
                return true;
            }
        });
        mStar4ImageView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (s4flag == true && s5flag==false) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    s4flag = false;
                                    prefs.edit().putBoolean("s4selected", false).apply();
                                    checkFlag();
                                    onRestart();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(getString(R.string.remove_credit))
                            .setPositiveButton(getString(R.string.yes), dialogClickListener)
                            .setNegativeButton(getString(R.string.no), dialogClickListener)
                            .show();
                }
                return true;
            }
        });
        mStar5ImageView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (s5flag == true && s6flag==false) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    s5flag = false;
                                    prefs.edit().putBoolean("s5selected", false).apply();
                                    checkFlag();
                                    onRestart();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(getString(R.string.remove_credit))
                            .setPositiveButton(getString(R.string.yes), dialogClickListener)
                            .setNegativeButton(getString(R.string.no), dialogClickListener)
                            .show();
                }
                return true;
            }
        });
        mStar6ImageView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (s6flag == true && s7flag==false) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    s6flag = false;
                                    prefs.edit().putBoolean("s6selected", false).apply();
                                    checkFlag();
                                    onRestart();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(getString(R.string.remove_credit))
                            .setPositiveButton(getString(R.string.yes), dialogClickListener)
                            .setNegativeButton(getString(R.string.no), dialogClickListener)
                            .show();
                }
                return true;
            }
        });
        mStar7ImageView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (s7flag == true && s8flag==false) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    s7flag = false;
                                    prefs.edit().putBoolean("s7selected", false).apply();
                                    checkFlag();
                                    onRestart();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(getString(R.string.remove_credit))
                            .setPositiveButton(getString(R.string.yes), dialogClickListener)
                            .setNegativeButton(getString(R.string.no), dialogClickListener)
                            .show();
                }
                return true;
            }
        });
        mStar8ImageView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (s8flag == true && s9flag==false) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    s8flag = false;
                                    prefs.edit().putBoolean("s8selected", false).apply();
                                    checkFlag();
                                    onRestart();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(getString(R.string.remove_credit))
                            .setPositiveButton(getString(R.string.yes), dialogClickListener)
                            .setNegativeButton(getString(R.string.no), dialogClickListener)
                            .show();
                }
                return true;
            }
        });
        mStar9ImageView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (s8flag == true && s9flag==true) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    s9flag = false;
                                    prefs.edit().putBoolean("s9selected", false).apply();
                                    checkFlag();
                                    onRestart();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(getString(R.string.remove_credit))
                            .setPositiveButton(getString(R.string.yes), dialogClickListener)
                            .setNegativeButton(getString(R.string.no), dialogClickListener)
                            .show();
                }
                return true;
            }
        });

        // Check and see if we need permission to use the camera
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
        }

    }

    private void requestCameraPermission() {
        Log.i(TAG, "CAMERA permission has NOT been granted. Requesting permission.");

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            Log.i(TAG,
                    "Displaying camera permission rationale to provide additional context.");
            Snackbar.make(findViewById(R.id.rules), R.string.permission_camera_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA);
                        }
                    })
                    .show();
        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }
    }

    //Results of the QR Code scan from CaptureActivity.class
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){

                sTagNum = prefs.getString("c", sNum);
                qrCodeText = data.getStringExtra("qrcodevalue");

                if(sTagNum == null && qrCodeText != null) {
                    checkFlag();
                    prefs.edit().putString("c", qrCodeText).apply();
                    sTagNum = prefs.getString("c", qrCodeText);
                    Toast t = Toast.makeText(MainActivity.this.getApplicationContext(), qrCodeText + " " + getString(R.string.is_registered), Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    t.show();

                    logoTitleName = (TextView)findViewById(R.id.logo_text);
                    cardOneTitle = (prefs.getString("loyaltyCardOneNamePref", qrCodeText));
                    if (sTagNum == null){
                        logoTitleName.setText("My uKoo");
                    } else {
                        logoTitleName.setText(sTagNum);
                    }

                    SharedPreferences.Editor prefsEditor = prefs.edit();
                    prefsEditor.putString("loyaltyCardOneNamePref", myTagId);
                    prefsEditor.apply();

                    onRestart();
                    creditPurchase();
                } else if (qrCodeText.equals(sTagNum.toString())){
                    // Toast used for Dewbug
                    //Toast t = Toast.makeText(MainActivity.this.getApplicationContext(), qrCodeText + " " + sTagNum, Toast.LENGTH_SHORT);
                    //t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    //t.show();
                    creditPurchase();
                } else {

                    Toast t = Toast.makeText(MainActivity.this.getApplicationContext(), getString(R.string.incorrect_tag), Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    t.show();
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //new ZxingOrient(MainActivity.this).initiateScan();

                } else {

                    finish();
                }
            }
            break;
        }
    }



    // The Pop-out menu
    public void displayView(int viewId) {

        String title = getString(R.string.app_name);

        switch (viewId) {
            case R.id.theme:
                Intent a = new Intent(MainActivity.this, ChangeThemeActivity.class);
                startActivity(a);
                title  = getString(R.string.app_name);
                break;
            case R.id.settings:
                //ActionBar bar = this.getActionBar();
                Intent b = new Intent(MainActivity.this, CardSettings.class);
                startActivity(b);
                title = getString(R.string.settings);
                break;

            //case R.id.about:
            //Intent a = new Intent(MainActivity.this, AboutActivity.class);
            //startActivity(a);
            //    title = getString(R.string.about);
            //    break;

        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void showMessage(int title, int message) {
        mDialog.setTitle(title);
        mDialog.setMessage(getText(message));
        mDialog.show();
    }

    public static void checkFlag(){
        //Check Star punch - Need to make sure images match the Theme
        if(s1flag) {
            if (themePref.equals("defaultTheme")) {
                mStar1ImageView.setImageResource(R.drawable.star_punch);
            } else if (themePref.equals("icecreamTheme")) {
                mStar1ImageView.setImageResource(R.drawable.icecream_punch);
            } else if (themePref.equals("coffeeTheme")) {
                mStar1ImageView.setImageResource(R.drawable.coffee_punch);
            } else if (themePref.equals("smoothieTheme")) {
                mStar1ImageView.setImageResource(R.drawable.smoothie_punch);
            } else if (themePref.equals("sandwichTheme")) {
                mStar1ImageView.setImageResource(R.drawable.sandwich_punch);
            } else if (themePref.equals("muffinTheme")) {
                mStar1ImageView.setImageResource(R.drawable.muffin_punch);
            }
        } else {
            if (themePref.equals("defaultTheme")) {
                mStar1ImageView.setImageResource(R.drawable.star);
            } else if (themePref.equals("icecreamTheme")) {
                mStar1ImageView.setImageResource(R.drawable.icecream);
            } else if (themePref.equals("coffeeTheme")) {
                mStar1ImageView.setImageResource(R.drawable.coffee);
            } else if (themePref.equals("smoothieTheme")) {
                mStar1ImageView.setImageResource(R.drawable.smoothie);
            } else if (themePref.equals("sandwichTheme")) {
                mStar1ImageView.setImageResource(R.drawable.sandwich);
            } else if (themePref.equals("muffinTheme")) {
                mStar1ImageView.setImageResource(R.drawable.muffin);
            }
        }

        if(s2flag) {
            if (themePref.equals("defaultTheme")) {
                mStar2ImageView.setImageResource(R.drawable.star_punch);
            } else if (themePref.equals("icecreamTheme")) {
                mStar2ImageView.setImageResource(R.drawable.icecream_punch);
            } else if (themePref.equals("coffeeTheme")) {
                mStar2ImageView.setImageResource(R.drawable.coffee_punch);
            } else if (themePref.equals("smoothieTheme")) {
                mStar2ImageView.setImageResource(R.drawable.smoothie_punch);
            } else if (themePref.equals("sandwichTheme")) {
                mStar2ImageView.setImageResource(R.drawable.sandwich_punch);
            } else if (themePref.equals("muffinTheme")) {
                mStar2ImageView.setImageResource(R.drawable.muffin_punch);
            }
        } else {
            if (themePref.equals("defaultTheme")) {
                mStar2ImageView.setImageResource(R.drawable.star);
            } else if (themePref.equals("icecreamTheme")) {
                mStar2ImageView.setImageResource(R.drawable.icecream);
            } else if (themePref.equals("coffeeTheme")) {
                mStar2ImageView.setImageResource(R.drawable.coffee);
            } else if (themePref.equals("smoothieTheme")) {
                mStar2ImageView.setImageResource(R.drawable.smoothie);
            } else if (themePref.equals("sandwichTheme")) {
                mStar2ImageView.setImageResource(R.drawable.sandwich);
            } else if (themePref.equals("muffinTheme")) {
                mStar2ImageView.setImageResource(R.drawable.muffin);
            }
        }

        if(s3flag) {
            if (themePref.equals("defaultTheme")) {
                mStar3ImageView.setImageResource(R.drawable.star_punch);
            } else if (themePref.equals("icecreamTheme")) {
                mStar3ImageView.setImageResource(R.drawable.icecream_punch);
            } else if (themePref.equals("coffeeTheme")) {
                mStar3ImageView.setImageResource(R.drawable.coffee_punch);
            } else if (themePref.equals("smoothieTheme")) {
                mStar3ImageView.setImageResource(R.drawable.smoothie_punch);
            } else if (themePref.equals("sandwichTheme")) {
                mStar3ImageView.setImageResource(R.drawable.sandwich_punch);
            } else if (themePref.equals("muffinTheme")) {
                mStar3ImageView.setImageResource(R.drawable.muffin_punch);
            }
        } else {
            if (themePref.equals("defaultTheme")) {
                mStar3ImageView.setImageResource(R.drawable.star);
            } else if (themePref.equals("icecreamTheme")) {
                mStar3ImageView.setImageResource(R.drawable.icecream);
            } else if (themePref.equals("coffeeTheme")) {
                mStar3ImageView.setImageResource(R.drawable.coffee);
            } else if (themePref.equals("smoothieTheme")) {
                mStar3ImageView.setImageResource(R.drawable.smoothie);
            } else if (themePref.equals("sandwichTheme")) {
                mStar3ImageView.setImageResource(R.drawable.sandwich);
            } else if (themePref.equals("muffinTheme")) {
                mStar3ImageView.setImageResource(R.drawable.muffin);
            }
        }

        if(s4flag) {
            if (themePref.equals("defaultTheme")) {
                mStar4ImageView.setImageResource(R.drawable.star_punch);
            } else if (themePref.equals("icecreamTheme")) {
                mStar4ImageView.setImageResource(R.drawable.icecream_punch);
            } else if (themePref.equals("coffeeTheme")) {
                mStar4ImageView.setImageResource(R.drawable.coffee_punch);
            } else if (themePref.equals("smoothieTheme")) {
                mStar4ImageView.setImageResource(R.drawable.smoothie_punch);
            } else if (themePref.equals("sandwichTheme")) {
                mStar4ImageView.setImageResource(R.drawable.sandwich_punch);
            } else if (themePref.equals("muffinTheme")) {
                mStar4ImageView.setImageResource(R.drawable.muffin_punch);
            }
        } else {
            if (themePref.equals("defaultTheme")) {
                mStar4ImageView.setImageResource(R.drawable.star);
            } else if (themePref.equals("icecreamTheme")) {
                mStar4ImageView.setImageResource(R.drawable.icecream);
            } else if (themePref.equals("coffeeTheme")) {
                mStar4ImageView.setImageResource(R.drawable.coffee);
            } else if (themePref.equals("smoothieTheme")) {
                mStar4ImageView.setImageResource(R.drawable.smoothie);
            } else if (themePref.equals("sandwichTheme")) {
                mStar4ImageView.setImageResource(R.drawable.sandwich);
            } else if (themePref.equals("muffinTheme")) {
                mStar4ImageView.setImageResource(R.drawable.muffin);
            }
        }

        if(s5flag) {
            if (themePref.equals("defaultTheme")) {
                mStar5ImageView.setImageResource(R.drawable.star_punch);
            } else if (themePref.equals("icecreamTheme")) {
                mStar5ImageView.setImageResource(R.drawable.icecream_punch);
            } else if (themePref.equals("coffeeTheme")) {
                mStar5ImageView.setImageResource(R.drawable.coffee_punch);
            } else if (themePref.equals("smoothieTheme")) {
                mStar5ImageView.setImageResource(R.drawable.smoothie_punch);
            } else if (themePref.equals("sandwichTheme")) {
                mStar5ImageView.setImageResource(R.drawable.sandwich_punch);
            } else if (themePref.equals("muffinTheme")) {
                mStar5ImageView.setImageResource(R.drawable.muffin_punch);
            }
        } else {
            if (themePref.equals("defaultTheme")) {
                mStar5ImageView.setImageResource(R.drawable.star);
            } else if (themePref.equals("icecreamTheme")) {
                mStar5ImageView.setImageResource(R.drawable.icecream);
            } else if (themePref.equals("coffeeTheme")) {
                mStar5ImageView.setImageResource(R.drawable.coffee);
            } else if (themePref.equals("smoothieTheme")) {
                mStar5ImageView.setImageResource(R.drawable.smoothie);
            } else if (themePref.equals("sandwichTheme")) {
                mStar5ImageView.setImageResource(R.drawable.sandwich);
            } else if (themePref.equals("muffinTheme")) {
                mStar5ImageView.setImageResource(R.drawable.muffin);
            }
        }

        if(s6flag) {
            if (themePref.equals("defaultTheme")) {
                mStar6ImageView.setImageResource(R.drawable.star_punch);
            } else if (themePref.equals("icecreamTheme")) {
                mStar6ImageView.setImageResource(R.drawable.icecream_punch);
            } else if (themePref.equals("coffeeTheme")) {
                mStar6ImageView.setImageResource(R.drawable.coffee_punch);
            } else if (themePref.equals("smoothieTheme")) {
                mStar6ImageView.setImageResource(R.drawable.smoothie_punch);
            } else if (themePref.equals("sandwichTheme")) {
                mStar6ImageView.setImageResource(R.drawable.sandwich_punch);
            } else if (themePref.equals("muffinTheme")) {
                mStar6ImageView.setImageResource(R.drawable.muffin_punch);
            }
        } else {
            if (themePref.equals("defaultTheme")) {
                mStar6ImageView.setImageResource(R.drawable.star);
            } else if (themePref.equals("icecreamTheme")) {
                mStar6ImageView.setImageResource(R.drawable.icecream);
            } else if (themePref.equals("coffeeTheme")) {
                mStar6ImageView.setImageResource(R.drawable.coffee);
            } else if (themePref.equals("smoothieTheme")) {
                mStar6ImageView.setImageResource(R.drawable.smoothie);
            } else if (themePref.equals("sandwichTheme")) {
                mStar6ImageView.setImageResource(R.drawable.sandwich);
            } else if (themePref.equals("muffinTheme")) {
                mStar6ImageView.setImageResource(R.drawable.muffin);
            }
        }

        if(s7flag) {
            if (themePref.equals("defaultTheme")) {
                mStar7ImageView.setImageResource(R.drawable.star_punch);
            } else if (themePref.equals("icecreamTheme")) {
                mStar7ImageView.setImageResource(R.drawable.icecream_punch);
            } else if (themePref.equals("coffeeTheme")) {
                mStar7ImageView.setImageResource(R.drawable.coffee_punch);
            } else if (themePref.equals("smoothieTheme")) {
                mStar7ImageView.setImageResource(R.drawable.smoothie_punch);
            } else if (themePref.equals("sandwichTheme")) {
                mStar7ImageView.setImageResource(R.drawable.sandwich_punch);
            } else if (themePref.equals("muffinTheme")) {
                mStar7ImageView.setImageResource(R.drawable.muffin_punch);
            }
        } else {
            if (themePref.equals("defaultTheme")) {
                mStar7ImageView.setImageResource(R.drawable.star);
            } else if (themePref.equals("icecreamTheme")) {
                mStar7ImageView.setImageResource(R.drawable.icecream);
            } else if (themePref.equals("coffeeTheme")) {
                mStar7ImageView.setImageResource(R.drawable.coffee);
            } else if (themePref.equals("smoothieTheme")) {
                mStar7ImageView.setImageResource(R.drawable.smoothie);
            } else if (themePref.equals("sandwichTheme")) {
                mStar7ImageView.setImageResource(R.drawable.sandwich);
            } else if (themePref.equals("muffinTheme")) {
                mStar7ImageView.setImageResource(R.drawable.muffin);
            }
        }

        if(s8flag) {
            if (themePref.equals("defaultTheme")) {
                mStar8ImageView.setImageResource(R.drawable.star_punch);
            } else if (themePref.equals("icecreamTheme")) {
                mStar8ImageView.setImageResource(R.drawable.icecream_punch);
            } else if (themePref.equals("coffeeTheme")) {
                mStar8ImageView.setImageResource(R.drawable.coffee_punch);
            } else if (themePref.equals("smoothieTheme")) {
                mStar8ImageView.setImageResource(R.drawable.smoothie_punch);
            } else if (themePref.equals("sandwichTheme")) {
                mStar8ImageView.setImageResource(R.drawable.sandwich_punch);
            } else if (themePref.equals("muffinTheme")) {
                mStar8ImageView.setImageResource(R.drawable.muffin_punch);
            }
        } else {
            if (themePref.equals("defaultTheme")) {
                mStar8ImageView.setImageResource(R.drawable.star);
            } else if (themePref.equals("icecreamTheme")) {
                mStar8ImageView.setImageResource(R.drawable.icecream);
            } else if (themePref.equals("coffeeTheme")) {
                mStar8ImageView.setImageResource(R.drawable.coffee);
            } else if (themePref.equals("smoothieTheme")) {
                mStar8ImageView.setImageResource(R.drawable.smoothie);
            } else if (themePref.equals("sandwichTheme")) {
                mStar8ImageView.setImageResource(R.drawable.sandwich);
            } else if (themePref.equals("muffinTheme")) {
                mStar8ImageView.setImageResource(R.drawable.muffin);
            }
        }

        if(s9flag) {
            if (themePref.equals("defaultTheme")) {
                mStar9ImageView.setImageResource(R.drawable.star_punch_free);
            } else if (themePref.equals("icecreamTheme")) {
                mStar9ImageView.setImageResource(R.drawable.icecream_punch_free);
            } else if (themePref.equals("coffeeTheme")) {
                mStar9ImageView.setImageResource(R.drawable.coffee_punch_free);
            } else if (themePref.equals("smoothieTheme")) {
                mStar9ImageView.setImageResource(R.drawable.smoothie_punch_free);
            } else if (themePref.equals("sandwichTheme")) {
                mStar9ImageView.setImageResource(R.drawable.sandwich_punch_free);
            } else if (themePref.equals("muffinTheme")) {
                mStar9ImageView.setImageResource(R.drawable.muffin_punch_free);
            }
        } else {
            if (themePref.equals("defaultTheme")) {
                mStar9ImageView.setImageResource(R.drawable.star);
            } else if (themePref.equals("icecreamTheme")) {
                mStar9ImageView.setImageResource(R.drawable.icecream);
            } else if (themePref.equals("coffeeTheme")) {
                mStar9ImageView.setImageResource(R.drawable.coffee);
            } else if (themePref.equals("smoothieTheme")) {
                mStar9ImageView.setImageResource(R.drawable.smoothie);
            } else if (themePref.equals("sandwichTheme")) {
                mStar9ImageView.setImageResource(R.drawable.sandwich);
            } else if (themePref.equals("muffinTheme")) {
                mStar9ImageView.setImageResource(R.drawable.muffin);
            }
        }
    }

    private NdefRecord newTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Set up a listener whenever a key changes
        if (mAdapter != null) {
            if (!mAdapter.isEnabled()) {
                showWirelessSettingsDialog();
            }
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            mAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
            mAdapter.disableForegroundNdefPush(this);
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        Bundle temp_bundle = new Bundle();
        onSaveInstanceState(temp_bundle);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("bundle", temp_bundle);
        startActivity(intent);
    }

    BroadcastReceiver broadcast_reciever = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            if (action.equals("finish_activity")) {
                finish();
            }
        }
    };


    private void showWirelessSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.nfc_disabled);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();
        return;
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;

            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            byte[] extraID = tagFromIntent.getId();

            StringBuilder sb = new StringBuilder();
            for (byte b : extraID) {
                sb.append(String.format("%02X", b));
            };

            nfcTagSerialNum = sb.toString();
            Log.e("nfc ID", nfcTagSerialNum);

            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
                msgs = new NdefMessage[] { msg };
            }

            // Setup the views
            buildTagViews(msgs);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String dumpTagData(Parcelable p) {
        StringBuilder sb = new StringBuilder();
        Tag tag = (Tag) p;
        byte[] id = tag.getId();
        sb.append("Tag ID (hex): ").append(getHex(id)).append("\n");
        sb.append("Tag ID (dec): ").append(getDec(id)).append("\n");
        sb.append("ID (reversed): ").append(getReversed(id)).append("\n");

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                MifareClassic mifareTag = MifareClassic.get(tag);
                String type = "Unknown";
                switch (mifareTag.getType()) {
                    case MifareClassic.TYPE_CLASSIC:
                        type = "Classic";
                        break;
                    case MifareClassic.TYPE_PLUS:
                        type = "Plus";
                        break;
                    case MifareClassic.TYPE_PRO:
                        type = "Pro";
                        break;
                }
                sb.append("Mifare Classic type: ");
                sb.append(type);
                sb.append('\n');

                sb.append("Mifare size: ");
                sb.append(mifareTag.getSize() + " bytes");
                sb.append('\n');

                sb.append("Mifare sectors: ");
                sb.append(mifareTag.getSectorCount());
                sb.append('\n');

                sb.append("Mifare blocks: ");
                sb.append(mifareTag.getBlockCount());
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);

            }
        }
        return sb.toString();
    }

    private String getHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');

            sb.append(Integer.toHexString(b));

            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private long getDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long getReversed(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }


    public void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout content = mTagContent;

        // Parse the first message in the list
        // Build views for all of the sub records
        Date now = new Date();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();
        for (int i = 0; i < size; i++) {
            TextView timeView = new TextView(this);
            timeView.setText(TIME_FORMAT.format(now));
            ParsedNdefRecord record = records.get(i);
            content.addView(record.getView(this, inflater, content, i), 1 + i);
            content.addView(inflater.inflate(R.layout.tag_divider, content, false), 2 + i);

            // Getting the text from the TextView from tag_text.xml and presenting it as Toast
            TextView myTagView =  (TextView)findViewById(R.id.text);
            myTagId = myTagView.getText().toString();

            sNum = myTagId;

            if(sTagNum == null && sNum != null) {
                checkFlag();
                prefs.edit().putString("c", sNum).apply();
                sTagNum = prefs.getString("c", sNum);
                Toast t = Toast.makeText(MainActivity.this.getApplicationContext(), myTagId + " " + getString(R.string.is_registered), Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                t.show();

                logoTitleName = (TextView)findViewById(R.id.logo_text);
                cardOneTitle = (prefs.getString("loyaltyCardOneNamePref", myTagId));
                if (sTagNum == null){
                    logoTitleName.setText("My uKoo");
                } else {
                    logoTitleName.setText(sTagNum);
                }

                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putString("loyaltyCardOneNamePref", myTagId);
                prefsEditor.apply();

                onRestart();
            }

            if (sNum == null && sTagNum == null) {

                Toast t = Toast.makeText(MainActivity.this.getApplicationContext(), getString(R.string.something_wrong), Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                t.show();

            }

            if (sTagNum != null && !sTagNum.equals(sNum)) {
                Toast t = Toast.makeText(MainActivity.this.getApplicationContext(), getString(R.string.incorrect_tag), Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                t.show();
            }

            mySTagUrl = sNum;


            if (sTagNum != null && sTagNum.equals(sNum)) {
                creditPurchase();
            }


        }
    }

    public void creditPurchase(){

        // This is used to test Logic and Flow - Remark out when not debugging
        //Toast sn = Toast.makeText(MainActivity.this.getApplicationContext(), "sNum:" + sTagNum, Toast.LENGTH_SHORT);
        //sn.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        //sn.show();

        //Toast t = Toast.makeText(MainActivity.this.getApplicationContext(), qrCodeText + " " + sTagNum, Toast.LENGTH_SHORT);
        //t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        //t.show();

        //Clear the Card after Freebee
        if (sTagNum != null)
            if ((s9flag == true)) {
                Toast toast = Toast.makeText(this, getString(R.string.you_redeemed), Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();

                resetImages();

                return;

                // Punch Card begins after Card Paired
            } else if ((sTagNum != null && s1flag == false)) {

                s1flag = true;
                checkFlag();
                prefs.edit().putBoolean("s1selected", true).apply();
                Toast toast = Toast.makeText(this, getString(R.string.thankyou) + " 1 " + getString(R.string.credit), Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();

            } else if ((sTagNum != null && s1flag == true) && ((s2flag == false))) {

                s2flag = true;
                checkFlag();
                prefs.edit().putBoolean("s2selected", true).apply();
                Toast toast = Toast.makeText(this, getString(R.string.thankyou) + " 2 " + getString(R.string.credit), Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();

            } else if ((sTagNum != null && s2flag == true) && ((s3flag == false))) {

                s3flag = true;
                checkFlag();
                prefs.edit().putBoolean("s3selected", true).apply();
                Toast toast = Toast.makeText(this, getString(R.string.thankyou) + " 3 " + getString(R.string.credit), Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();

            } else if ((sTagNum != null && s3flag == true) && ((s4flag == false))) {

                s4flag = true;
                checkFlag();
                prefs.edit().putBoolean("s4selected", true).apply();
                Toast toast = Toast.makeText(this, getString(R.string.thankyou) + " 4 " + getString(R.string.credit), Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();

            } else if ((sTagNum != null && s4flag == true) && ((s5flag == false))) {

                s5flag = true;
                checkFlag();
                prefs.edit().putBoolean("s5selected", true).apply();
                Toast toast = Toast.makeText(this, getString(R.string.thankyou) + " 5 " + getString(R.string.credit), Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();

            } else if ((sTagNum != null && s5flag == true) && ((s6flag == false))) {

                s6flag = true;
                checkFlag();
                prefs.edit().putBoolean("s6selected", true).apply();
                Toast toast = Toast.makeText(this, getString(R.string.thankyou) + " 6 " + getString(R.string.credit), Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();

            } else if ((sTagNum != null && s6flag == true) && ((s7flag == false))) {

                s7flag = true;
                checkFlag();
                prefs.edit().putBoolean("s7selected", true).apply();
                Toast toast = Toast.makeText(this, getString(R.string.thankyou) + " 7 " + getString(R.string.credit), Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();

            } else if ((sTagNum != null && s7flag == true) && ((s8flag == false))) {

                s8flag = true;
                checkFlag();
                prefs.edit().putBoolean("s8selected", true).apply();
                Toast toast = Toast.makeText(this, getString(R.string.thankyou) + " 8 " + getString(R.string.credit), Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();

            } else if ((sTagNum != null && s8flag == true) && ((s9flag == false))) {

                s9flag = true;
                checkFlag();
                prefs.edit().putBoolean("s9selected", true).apply();
                Toast toast = Toast.makeText(this, getString(R.string.congrats), Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();

            }

    }

    // This is called from the CardSettings.class to reset the card
    public void resetCard(){

        //Set all the Stars to False
        prefs.edit().putBoolean("s1selected", false).clear().apply();
        prefs.edit().putBoolean("s2selected", false).clear().apply();
        prefs.edit().putBoolean("s3selected", false).clear().apply();
        prefs.edit().putBoolean("s4selected", false).clear().apply();
        prefs.edit().putBoolean("s5selected", false).clear().apply();
        prefs.edit().putBoolean("s6selected", false).clear().apply();
        prefs.edit().putBoolean("s7selected", false).clear().apply();
        prefs.edit().putBoolean("s8selected", false).clear().apply();
        prefs.edit().putBoolean("s9selected", false).clear().apply();

        checkFlag();

        //Clear the currently register Tag
        sTagNum = prefs.getString("nfctagsn", null);
        prefs.edit().putString("nfctagsn", null).apply();
        sNum = null;
        myTagId = null;

        //Clear the Current Card Name.
        prefs.edit().putString("loyaltyCardOneNamePref", "My uKoo").clear().apply();
        finish();

    }

    // Clear Punches once Freebee is redeemed
    public void resetImages(){

        //Set all the Stars to False
        prefs.edit().putBoolean("s1selected", false).apply();
        prefs.edit().putBoolean("s2selected", false).apply();
        prefs.edit().putBoolean("s3selected", false).apply();
        prefs.edit().putBoolean("s4selected", false).apply();
        prefs.edit().putBoolean("s5selected", false).apply();
        prefs.edit().putBoolean("s6selected", false).apply();
        prefs.edit().putBoolean("s7selected", false).apply();
        prefs.edit().putBoolean("s8selected", false).apply();
        prefs.edit().putBoolean("s9selected", false).apply();

        checkFlag();

        onRestart();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        displayView(item.getItemId());
        return true;
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) < 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);

        return;
    }

    @Override
    public void onDestroy() {
        if(broadcast_reciever!=null) {
            unregisterReceiver(broadcast_reciever);
        }
        super.onDestroy();
    }
}