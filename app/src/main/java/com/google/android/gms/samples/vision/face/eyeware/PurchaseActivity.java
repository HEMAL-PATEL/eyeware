package com.google.android.gms.samples.vision.face.eyeware;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PurchaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        Bundle extras = getIntent().getExtras();

        String framesName = extras.getString("framesName");
        String framesColor = extras.getString("framesColor");
        int framesPrice = extras.getInt("framesPrice");
        int framesPictureID = extras.getInt("framesPictureID");

        TextView nameText = (TextView) findViewById(R.id.purchaseFrameName);
        TextView colorText = (TextView) findViewById(R.id.purchaseFrameColor);
        TextView priceText = (TextView) findViewById(R.id.purchaseFramePrice);
        ImageView framesImage = (ImageView) findViewById(R.id.generalFrames);

        nameText.setText(framesName);
        colorText.setText(framesColor);
        priceText.setText("$" + framesPrice + ".00");
        framesImage.setImageResource(framesPictureID);

        Button buyButton = (Button)findViewById(R.id.buyButton);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PurchaseActivity.this, "Thank you for your purchase! You're awesome!", Toast.LENGTH_LONG).show();

            }
        });



    }



}
