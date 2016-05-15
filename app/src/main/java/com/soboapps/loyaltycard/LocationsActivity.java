package com.soboapps.loyaltycard;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class LocationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locations_main);

        /*
        TextView link = (TextView) findViewById(R.id.www);
        String linkText = "Visit <a href='http://www.somewwwsite.com'>My Site</a>.";
        link.setText(Html.fromHtml(linkText));
        link.setLinkTextColor(Color.BLUE);
        link.setMovementMethod(LinkMovementMethod.getInstance());
        */
        TextView sobo = (TextView) findViewById(R.id.sobo);
        String soboWww = "PunchCard by <a href='http://www.soboapps.com'>SoBo Apps</a>.";
        sobo.setText(Html.fromHtml(soboWww));
        sobo.setLinkTextColor(Color.BLUE);
        sobo.setMovementMethod(LinkMovementMethod.getInstance());

        /*
        TextView my1Address = (TextView) findViewById(R.id.store1A);
        my1Address.setAutoLinkMask(Linkify.MAP_ADDRESSES);
        my1Address.setText("1212 Some Street NW\nWashington, DC 20001");
        my1Address.setGravity(Gravity.CENTER);
        */

    }
}
