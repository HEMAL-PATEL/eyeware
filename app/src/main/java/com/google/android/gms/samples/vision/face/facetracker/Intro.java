package com.google.android.gms.samples.vision.face.facetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Intro extends Activity {

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
                //Toast.makeText(Intro.this, "You've successfully picked Male", Toast.LENGTH_LONG).show();
            }
        });

        Button femaleButton = (Button)findViewById(R.id.femaleButton);
        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("gender", "Female");
                startActivity(i);
                //Toast.makeText(Intro.this, "You've successfully picked Male", Toast.LENGTH_LONG).show();
            }
        });

        Button keywordButton = (Button)findViewById(R.id.keywordButton);
        keywordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("gender", "Keyword");
                startActivity(i);
                //Toast.makeText(Intro.this, "You've successfully picked Male", Toast.LENGTH_LONG).show();
            }
        });

    }

    /*ListView list = (ListView)findViewById(R.id.framesListView);
    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {

            Frames choice = mFrames.get(position);
            //String message = "These are the " + clickedFrames.getName() + " frames";
            //Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), FaceTrackerActivity.class);
            i.putExtra("selectedFrames", choice.getPictureID());

            startActivity(i);

        }
    });*/
}
