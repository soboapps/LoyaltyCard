package com.soboapps.loyaltycard;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.soboapps.loyaltycard.record.ParsedNdefRecord;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //private final MainActivity mMainActivity= new MainActivity();
    //private final LocationsActivity mLocationsActivity = new LocationsActivity();

    private ListView mDrawerList;
    private ArrayAdapter<String> menuAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    private static final DateFormat TIME_FORMAT = SimpleDateFormat.getDateTimeInstance();
    private LinearLayout mTagContent;

    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private NdefMessage mNdefPushMessage;

    private AlertDialog mDialog;

    public static String myTagId;
    public static String myCTagUrl;
    //public static String mySTagId;
    public static String mySTagUrl;
    //public static String sPayload;
    public static String cardOneTitle;


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
    public static SharedPreferences settings;

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
    public static String sTagNum;

    public static TextView logoTitleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.tag_viewer);
        setContentView(R.layout.activity_main);
        mTagContent = (LinearLayout) findViewById(R.id.list);
        resolveIntent(getIntent());

        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setLogo(R.drawable.appbar);
        //actionBar.setDisplayUseLogoEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(true);

        mDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", null).create();

        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            showMessage(R.string.error, R.string.no_nfc);
            finish();
            return;
        }

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mNdefPushMessage = new NdefMessage(new NdefRecord[] { newTextRecord(
                "Message from NFC Reader :-)", Locale.ENGLISH, true) });

        prefs = this.getSharedPreferences("com.soboapps.punchcard", Context.MODE_PRIVATE);

        sTagNum = prefs.getString("nfctagsn", null);

        settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        Typeface tf = Typeface.createFromAsset(getAssets(),"rumraisin.ttf");

        logoTitleName = (TextView)findViewById(R.id.logo_text);
        logoTitleName.setTypeface(tf,Typeface.BOLD);
        cardOneTitle = (settings.getString("loyaltyCardOneNamePref", "Loyalty Card"));
        logoTitleName.setText(cardOneTitle);


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

        mStar1ImageView = (ImageView) findViewById(R.id.img_star_1);
        mStar2ImageView = (ImageView) findViewById(R.id.img_star_2);
        mStar3ImageView = (ImageView) findViewById(R.id.img_star_3);
        mStar4ImageView = (ImageView) findViewById(R.id.img_star_4);
        mStar5ImageView = (ImageView) findViewById(R.id.img_star_5);
        mStar6ImageView = (ImageView) findViewById(R.id.img_star_6);
        mStar7ImageView = (ImageView) findViewById(R.id.img_star_7);
        mStar8ImageView = (ImageView) findViewById(R.id.img_star_8);
        mStar9ImageView = (ImageView) findViewById(R.id.img_star_9);

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
                                    mStar1ImageView.setImageResource(R.drawable.star);
                                    s1flag = false;
                                    prefs.edit().putBoolean("s1selected", false).apply();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //TODO No button clicked
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Remove the Credit?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener)
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
                                    mStar2ImageView.setImageResource(R.drawable.star);
                                    s2flag = false;
                                    prefs.edit().putBoolean("s2selected", false).apply();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //TODO No button clicked
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Remove the Credit?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener)
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
                                    mStar3ImageView.setImageResource(R.drawable.star);
                                    s3flag = false;
                                    prefs.edit().putBoolean("s3selected", false).apply();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //TODO No button clicked
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Remove the Credit?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener)
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
                                    mStar4ImageView.setImageResource(R.drawable.star);
                                    s4flag = false;
                                    prefs.edit().putBoolean("s4selected", false).apply();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //TODO No button clicked
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Remove the Credit?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener)
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
                                    mStar5ImageView.setImageResource(R.drawable.star);
                                    s5flag = false;
                                    prefs.edit().putBoolean("s5selected", false).apply();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //TODO No button clicked
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Remove the Credit?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener)
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
                                    mStar6ImageView.setImageResource(R.drawable.star);
                                    s6flag = false;
                                    prefs.edit().putBoolean("s6selected", false).apply();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //TODO No button clicked
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Remove the Credit?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener)
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
                                    mStar7ImageView.setImageResource(R.drawable.star);
                                    s7flag = false;
                                    prefs.edit().putBoolean("s7selected", false).apply();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //TODO No button clicked
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Remove the Credit?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener)
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
                                    mStar8ImageView.setImageResource(R.drawable.star);
                                    s8flag = false;
                                    prefs.edit().putBoolean("s8selected", false).apply();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //TODO No button clicked
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Remove the Credit?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener)
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
                                    mStar9ImageView.setImageResource(R.drawable.star);
                                    s9flag = false;
                                    prefs.edit().putBoolean("s9selected", false).apply();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //TODO No button clicked
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Remove the Credit?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener)
                            .show();
                }
                return true;
            }
        });


    }

    private void addDrawerItems() {
        String [] menuList = getResources().getStringArray(R.array.menu_Array);
        menuAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuList);
        mDrawerList.setAdapter(menuAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
        });

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MainActivity.this, CardSettings.class));
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.storeabout);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
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

    public void checkFlag(){
        //Check Star punch
        if(s1flag) {
            mStar1ImageView.setImageResource(R.drawable.star_punch);
        } else {
            mStar1ImageView.setImageResource(R.drawable.star);
        }

        if(s2flag) {
            mStar2ImageView.setImageResource(R.drawable.star_punch);
        } else {
            mStar2ImageView.setImageResource(R.drawable.star);
        }

        if(s3flag) {
            mStar3ImageView.setImageResource(R.drawable.star_punch);
        } else {
            mStar3ImageView.setImageResource(R.drawable.star);
        }

        if(s4flag) {
            mStar4ImageView.setImageResource(R.drawable.star_punch);
        } else {
            mStar4ImageView.setImageResource(R.drawable.star);
        }

        if(s5flag) {
            mStar5ImageView.setImageResource(R.drawable.star_punch);
        } else {
            mStar5ImageView.setImageResource(R.drawable.star);
        }

        if(s6flag) {
            mStar6ImageView.setImageResource(R.drawable.star_punch);
        } else {
            mStar6ImageView.setImageResource(R.drawable.star);
        }

        if(s7flag) {
            mStar7ImageView.setImageResource(R.drawable.star_punch);
        } else {
            mStar7ImageView.setImageResource(R.drawable.star);
        }

        if(s8flag) {
            mStar8ImageView.setImageResource(R.drawable.star_punch);
        } else {
            mStar8ImageView.setImageResource(R.drawable.star);
        }

        if(s9flag) {
            mStar9ImageView.setImageResource(R.drawable.star_punch_free);
        } else {
            mStar9ImageView.setImageResource(R.drawable.star);
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
        //settings.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        //updatePreference("loyaltyCardOneNamePref");
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
        //settings.getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
            mAdapter.disableForegroundNdefPush(this);
        }
    }




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

    private String dumpTagData(Parcelable p) {
        StringBuilder sb = new StringBuilder();
        Tag tag = (Tag) p;
        byte[] id = tag.getId();
        sb.append("Tag ID (hex): ").append(getHex(id)).append("\n");
        sb.append("Tag ID (dec): ").append(getDec(id)).append("\n");
        sb.append("ID (reversed): ").append(getReversed(id)).append("\n");

        //sNum = null;
        //sNum = new String(tag.getId());

        //Toast sn = Toast.makeText(MainActivity.this.getApplicationContext(), "sNum:" + sNum, Toast.LENGTH_SHORT);
        //sn.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        //sn.show();

        //if(sTagNum == null && sNum != null) {
        //    prefs.edit().putString("nfctagsn", sNum).apply();
        //    sTagNum = prefs.getString("nfctagsn", sNum);
        //    Toast t = Toast.makeText(MainActivity.this.getApplicationContext(), sTagNum + "\nTag Now Registered", Toast.LENGTH_SHORT);
        //    t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        //    t.show();

        //}

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

    void buildTagViews(NdefMessage[] msgs) {
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
            //content.addView(timeView, 0);
            ParsedNdefRecord record = records.get(i);
            content.addView(record.getView(this, inflater, content, i), 1 + i);
            content.addView(inflater.inflate(R.layout.tag_divider, content, false), 2 + i);

            // Getting the text from the TextView from tag_text.xml and presenting it as Toast
            TextView myTagView =  (TextView)findViewById(R.id.text);
            myTagId = myTagView.getText().toString();

            sNum = myTagId;
            //sNum = new String(tag.getId());

            //Toast sn = Toast.makeText(MainActivity.this.getApplicationContext(), "sNum:" + sNum, Toast.LENGTH_SHORT);
            //sn.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            //sn.show();

            if(sTagNum == null && sNum != null) {
                prefs.edit().putString("nfctagsn", sNum).apply();
                sTagNum = prefs.getString("nfctagsn", sNum);
                Toast t = Toast.makeText(MainActivity.this.getApplicationContext(), "Tag Now Registered", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                t.show();
                logoTitleName = (TextView)findViewById(R.id.logo_text);
                cardOneTitle = (settings.getString("loyaltyCardOneNamePref", "Loyalty Card"));
                logoTitleName.setText(cardOneTitle);
            }


            if (sNum == null && sTagNum == null) {

                Toast t = Toast.makeText(MainActivity.this.getApplicationContext(), "Something is Wrong", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                t.show();

                //sTagNum = prefs.getString("nfctagsn", null);

                //Toast t = Toast.makeText(MainActivity.this.getApplicationContext(), sTagNum + "Tag Now Registered", Toast.LENGTH_SHORT);
                //t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                //t.show();
                //sTagNum = prefs.getString("nfctagsn", null);
                //sTagNum = (BaseAdapter)sTagNum.getRootAdapter();


            }

            if (sTagNum != null && !sTagNum.equals(sNum)) {
                //sTagNum = prefs.getString("nfctagsn", null);
                //prefs.edit().putString("nfctagsn", sNum).apply();
                Toast t = Toast.makeText(MainActivity.this.getApplicationContext(), "Incorrect Tag", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                t.show();
            }

            //Toast t = Toast.makeText(MainActivity.this.getApplicationContext(), myTagId, Toast.LENGTH_SHORT);
            //t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            //t.show();

            //mySTagUrl = getString(R.string.stag_url);
            mySTagUrl = sNum;


            //PunchCard
            if (sNum != null && sTagNum != null)
            if ((s9flag == true) && mySTagUrl.equals(sTagNum.toString())) {
                Toast toast = Toast.makeText(this, "You Redeemed Your Free\n" +
                        "Item, Thank You\n" +
                        "For Your Patronage!", Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();
                mStar1ImageView.setImageResource(R.drawable.star);
                mStar2ImageView.setImageResource(R.drawable.star);
                mStar3ImageView.setImageResource(R.drawable.star);
                mStar4ImageView.setImageResource(R.drawable.star);
                mStar5ImageView.setImageResource(R.drawable.star);
                mStar6ImageView.setImageResource(R.drawable.star);
                mStar7ImageView.setImageResource(R.drawable.star);
                mStar8ImageView.setImageResource(R.drawable.star);
                mStar9ImageView.setImageResource(R.drawable.star);

                s1flag = false;
                s2flag = false;
                s3flag = false;
                s4flag = false;
                s5flag = false;
                s6flag = false;
                s7flag = false;
                s8flag = false;
                s9flag = false;
                //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                prefs.edit().putBoolean("s1selected", false).apply();
                prefs.edit().putBoolean("s2selected", false).apply();
                prefs.edit().putBoolean("s3selected", false).apply();
                prefs.edit().putBoolean("s4selected", false).apply();
                prefs.edit().putBoolean("s5selected", false).apply();
                prefs.edit().putBoolean("s6selected", false).apply();
                prefs.edit().putBoolean("s7selected", false).apply();
                prefs.edit().putBoolean("s8selected", false).apply();
                prefs.edit().putBoolean("s9selected", false).apply();

                return;

            } else if ((sNum != null && s1flag == false) && mySTagUrl.equals(sTagNum.toString())) {
                mStar1ImageView.setImageResource(R.drawable.star_punch);

                s1flag = true;
                prefs.edit().putBoolean("s1selected", true).apply();

                Toast toast = Toast.makeText(this, "Thank You!\n" +
                        "You Earned 1 Credit\n" +
                        "Towards a Free Item", Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();
                break;

            } else if ((sNum != null && s1flag == true) && mySTagUrl.equals(sTagNum.toString()) && ((s2flag == false))) {
                mStar2ImageView.setImageResource(R.drawable.star_punch);

                s2flag = true;
                prefs.edit().putBoolean("s2selected", true).apply();

                Toast toast = Toast.makeText(this, "Thank You!\n" +
                        "You Have Earned 2 Credits\n" +
                        "Towards a Free Item", Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();
                break;

            } else if ((sNum != null && s2flag == true) && mySTagUrl.equals(sTagNum.toString()) && ((s3flag == false))) {
                mStar3ImageView.setImageResource(R.drawable.star_punch);

                s3flag = true;
                prefs.edit().putBoolean("s3selected", true).apply();

                Toast toast = Toast.makeText(this, "Thank You!\n" +
                        "You Have Earned 3 Credits\n" +
                        "Towards a Free Item", Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();
                break;

            } else if ((sNum != null && s3flag == true) && mySTagUrl.equals(sTagNum.toString()) && ((s4flag == false))) {
                mStar4ImageView.setImageResource(R.drawable.star_punch);

                s4flag = true;
                prefs.edit().putBoolean("s4selected", true).apply();

                Toast toast = Toast.makeText(this, "Thank You!\n" +
                        "You Have Earned 4 Credits\n" +
                        "Towards a Free Item", Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();
                break;

            } else if ((sNum != null && s4flag == true) && mySTagUrl.equals(sTagNum.toString()) && ((s5flag == false))) {
                mStar5ImageView.setImageResource(R.drawable.star_punch);

                s5flag = true;
                prefs.edit().putBoolean("s5selected", true).apply();

                Toast toast = Toast.makeText(this, "Thank You!\n" +
                        "You're Over Half Way to\n" +
                        "Free Item", Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();
                break;

            } else if ((sNum != null && s5flag == true) && mySTagUrl.equals(sTagNum.toString()) && ((s6flag == false))) {
                mStar6ImageView.setImageResource(R.drawable.star_punch);

                s6flag = true;
                prefs.edit().putBoolean("s6selected", true).apply();

                Toast toast = Toast.makeText(this, "Thank You!\n" +
                        "You Have Earned 6 Credits\n" +
                        "Towards a Free Item", Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();
                break;

            } else if ((sNum != null && s6flag == true) && mySTagUrl.equals(sTagNum.toString()) && ((s7flag == false))) {
                mStar7ImageView.setImageResource(R.drawable.star_punch);

                s7flag = true;
                prefs.edit().putBoolean("s7selected", true).apply();

                Toast toast = Toast.makeText(this, "Thank You!\n" +
                        "You Have Earned 7 Credits\n" +
                        "Towards a Free Item", Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();
                break;

            } else if ((sNum != null && s7flag == true) && mySTagUrl.equals(sTagNum.toString()) && ((s8flag == false))) {
                mStar8ImageView.setImageResource(R.drawable.star_punch);

                s8flag = true;
                prefs.edit().putBoolean("s8selected", true).apply();

                Toast toast = Toast.makeText(this, "Thank You!\n" +
                        "You Have Earned 8 Credits\n" +
                        "Towards a Free Item", Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();
                break;

            } else if ((sNum != null && s8flag == true) && mySTagUrl.equals(sTagNum.toString()) && ((s9flag == false))) {
                mStar9ImageView.setImageResource(R.drawable.star_punch_free);

                s9flag = true;
                prefs.edit().putBoolean("s9selected", true).apply();

                Toast toast = Toast.makeText(this, "CONGRATULATION!\n" +
                        "Your Next\n" +
                        "Item is on US!", Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();
                break;

            }

        }
    }

    public static void resetView(){

        mStar1ImageView.setImageResource(R.drawable.star);
        mStar2ImageView.setImageResource(R.drawable.star);
        mStar3ImageView.setImageResource(R.drawable.star);
        mStar4ImageView.setImageResource(R.drawable.star);
        mStar5ImageView.setImageResource(R.drawable.star);
        mStar6ImageView.setImageResource(R.drawable.star);
        mStar7ImageView.setImageResource(R.drawable.star);
        mStar8ImageView.setImageResource(R.drawable.star);
        mStar9ImageView.setImageResource(R.drawable.star);

        s1flag = false;
        s2flag = false;
        s3flag = false;
        s4flag = false;
        s5flag = false;
        s6flag = false;
        s7flag = false;
        s8flag = false;
        s9flag = false;
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean("s1selected", false).apply();
        prefs.edit().putBoolean("s2selected", false).apply();
        prefs.edit().putBoolean("s3selected", false).apply();
        prefs.edit().putBoolean("s4selected", false).apply();
        prefs.edit().putBoolean("s5selected", false).apply();
        prefs.edit().putBoolean("s6selected", false).apply();
        prefs.edit().putBoolean("s7selected", false).apply();
        prefs.edit().putBoolean("s8selected", false).apply();
        prefs.edit().putBoolean("s9selected", false).apply();

        sTagNum = prefs.getString("nfctagsn", null);
        prefs.edit().putString("nfctagsn", null).apply();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }

    /*
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePreference(key);

    }

    private void updatePreference(String key) {
        if (key.equals("loyaltyCardOneNamePref")){
            //settings = findPreference(key);
            if (settings instanceof EditTextPreference){
                EditTextPreference editTextPreference =  (EditTextPreference)settings;
                if (editTextPreference.getText().trim().length() > 0){
                    editTextPreference.setSummary("Current Name:  " + editTextPreference.getText());
                }else{
                    editTextPreference.setSummary("Enter Current Name");
                }
            }
        }
    }
    */
}
