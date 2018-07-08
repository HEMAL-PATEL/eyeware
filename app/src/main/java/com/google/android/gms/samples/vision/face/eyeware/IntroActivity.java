package com.google.android.gms.samples.vision.face.eyeware;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IntroActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Button maleButton = (Button)findViewById(R.id.maleButton);
        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("gender", "Male");
                startActivity(i);
                //Toast.makeText(IntroActivity.this, "You've successfully picked Male", Toast.LENGTH_LONG).show();
            }
        });

        Button femaleButton = (Button)findViewById(R.id.femaleButton);
        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("gender", "Female");
                startActivity(i);
                //Toast.makeText(IntroActivity.this, "You've successfully picked Male", Toast.LENGTH_LONG).show();
            }
        });

        Button keywordButton = (Button)findViewById(R.id.keywordButton);
        keywordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("gender", "Keyword");
                startActivity(i);
                //Toast.makeText(IntroActivity.this, "You've successfully picked Male", Toast.LENGTH_LONG).show();
            }
        });

        Button favoritesButton = (Button)findViewById(R.id.favoritesButton);
        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*SharedPreferences settings = getSharedPreferences("Favorites", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.clear();

                // Commit the edits!
                editor.commit();*/
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("gender", "Favorites");
                startActivity(i);
            }
        });

    }
}
