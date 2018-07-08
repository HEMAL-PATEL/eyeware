package com.google.android.gms.samples.vision.face.eyeware;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private List<Frames> mFrames = new ArrayList<Frames>();

    private String GENDER = "";
    private String KEYWORD = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        GENDER += extras.getString("gender");
        //Generate all the frames we're going to need for this session
        populateFramesList();

        //Filter the results depending on where we came from (i.e., Male, Female, Keyword, Favorites)
        populateListView();
        registerTapCallback();

    }

    private void populateFramesList() {
        //Make sure to clear the list before populating for future views!
        mFrames.clear();

        if(GENDER.equals("Female") || GENDER.equals("Keyword") || GENDER.equals("Favorites"))
            addFemaleEntriesToList();

        //Format for adding to list: (int pictureID, String name, String gender, String color, String shape, int price)

        mFrames.add(new Frames(R.drawable.begley_cedartortoise, "Begley",
                "Male", "Cedar Tortoise", "Round", 95, R.drawable.begley_cedartortoise_icon));
        mFrames.add(new Frames(R.drawable.begley_whiskeytortoise, "Begley",
                "Male", "Whiskey Tortoise", "Round", 95, R.drawable.begley_whiskeytortoise_icon));

        mFrames.add(new Frames(R.drawable.bensen_belizeblue, "Bensen",
                "Male", "Belize Blue", "Square", 95, R.drawable.bensen_belizeblue_icon));
        mFrames.add(new Frames(R.drawable.bensen_whiskeytortoise, "Bensen",
                "Male", "Whiskey Tortoise", "Square", 95, R.drawable.bensen_whiskeytortoise_icon));

        mFrames.add(new Frames(R.drawable.chilton_blueberrybuckle, "Chilton",
                "Male", "Blueberry Buckle", "Rectangle", 95, R.drawable.chilton_blueberrybuckle_icon));
        mFrames.add(new Frames(R.drawable.chilton_whiskeytortoise, "Chilton",
                "Male", "Whiskey Tortoise", "Rectangle", 95, R.drawable.chilton_whiskeytortoise_icon));

        mFrames.add(new Frames(R.drawable.clark_bluemarblewood, "Clark",
                "Male", "Blue Marblewood", "Square", 95, R.drawable.clark_bluemarblewood_icon));
        mFrames.add(new Frames(R.drawable.clark_scarlettortoise, "Clark",
                "Male", "Scarlet Tortoise", "Square", 95, R.drawable.clark_scarlettortoise_icon));

        mFrames.add(new Frames(R.drawable.crane_ti_newsprintgrey, "Crane-Ti",
                "Male", "Newsprint Grey", "Rectangle", 95, R.drawable.crane_ti_newsprintgrey_icon));
        mFrames.add(new Frames(R.drawable.crane_ti_stripedolive, "Crane-Ti",
                "Male", "Striped Olive", "Rectangle", 95, R.drawable.crane_ti_stripedolive_icon));

        mFrames.add(new Frames(R.drawable.dahl_englishoak, "Dahl",
                "Male", "English Oak", "Round", 95, R.drawable.dahl_englishoak_icon));

        mFrames.add(new Frames(R.drawable.greenleaf_whiskeytortoise, "Greenleaf",
                "Male", "Whiskey Tortoise", "Round", 95, R.drawable.greenleaf_whiskeytortoise_icon));
        mFrames.add(new Frames(R.drawable.greenleaf_woodgraintortoise, "Greenleaf",
                "Male", "Woodgrain Tortoise", "Round", 95, R.drawable.greenleaf_woodgraintortoise_icon));

        mFrames.add(new Frames(R.drawable.maynard_cortado, "Maynard",
                "Male", "Cortado", "Round", 95, R.drawable.maynard_cortado_icon));
        mFrames.add(new Frames(R.drawable.maynard_whiskeytortoise, "Maynard",
                "Male", "Whiskey Tortoise", "Round", 95, R.drawable.maynard_whiskeytortoise_icon));

        mFrames.add(new Frames(R.drawable.mitchell_earlgrey, "Mitchell",
                "Male", "Earl Grey", "Rectangle", 95, R.drawable.mitchell_earlgrey_icon));
        mFrames.add(new Frames(R.drawable.mitchell_whiskeytortoise, "Mitchell",
                "Male", "Whiskey Tortoise", "Rectangle", 95, R.drawable.mitchell_whiskeytortoise_icon));

        mFrames.add(new Frames(R.drawable.northcote_catalinablue, "Northcote",
                "Male", "Catalina Blue", "Rectangle", 95, R.drawable.northcote_catalinablue_icon));
        mFrames.add(new Frames(R.drawable.northcote_whiskeytortoise, "Northcote",
                "Male", "Whiskey Tortoise", "Rectangle", 95, R.drawable.northcote_whiskeytortoise_icon));

        mFrames.add(new Frames(R.drawable.topper_riverstonebluefade, "Topper",
                "Male", "River Stone Blue Fade", "Rectangle", 95, R.drawable.topper_riverstonebluefade_icon));
        mFrames.add(new Frames(R.drawable.topper_whiskeytortoise, "Topper",
                "Male", "Whiskey Tortoise", "Rectangle", 95, R.drawable.topper_whiskeytortoise_icon));

        mFrames.add(new Frames(R.drawable.vence_citronfade, "Vence",
                "Make", "Citron Fade", "Square", 95, R.drawable.vence_citronfade_icon));
        mFrames.add(new Frames(R.drawable.vence_saltwatermatte, "Vence",
                "Male", "Saltwater Matte", "Square", 95, R.drawable.vence_saltwatermatte_icon));

        mFrames.add(new Frames(R.drawable.watts_newsprintgrey, "Watts",
                "Male", "Newsprint Grey", "Round", 95, R.drawable.watts_newsprintgrey_icon));
        mFrames.add(new Frames(R.drawable.watts_sugarmaple, "Watts",
                "Male", "Sugar Maple", "Round", 95, R.drawable.watts_sugarmaple_icon));

        mFrames.add(new Frames(R.drawable.wilder_squallbluefade, "Wilder",
                "Male", "Squall Blue Fade", "Square", 95, R.drawable.wilder_squallbluefade_icon));
        mFrames.add(new Frames(R.drawable.wilder_whiskeytortoise, "Wilder",
                "Male", "Whiskey Tortoise", "Square", 95, R.drawable.wilder_whiskeytortoise_icon));
        //if(GENDER.equals("Keyword")) {
            createKeyWordList();
        //}
        if(GENDER.equals("Favorites")) {
            createFavoritesList();
        }
    }

    private void createFavoritesList() {
        //Make a new list from the old one by going through each
        List<Frames> newList = new ArrayList<Frames>();

        SharedPreferences preferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        for (int i = 0; i < mFrames.size(); i++) {
            if (preferences.getBoolean("" + mFrames.get(i).getPictureID(), false)) {
                newList.add(new Frames(mFrames.get(i).getPictureID(), mFrames.get(i).getName(),
                        mFrames.get(i).getGender(), mFrames.get(i).getColor(), mFrames.get(i).getShape(),
                        mFrames.get(i).getPrice(),mFrames.get(i).getIconID()));
            }
        }

        mFrames.clear();
        mFrames = null;
        mFrames = newList;
    }

    private void addFemaleEntriesToList() {
        mFrames.add(new Frames(R.drawable.annette_petaltortoise, "Annette",
                "Female", "Petal Tortoise", "Rectangle", 95, R.drawable.annette_petaltortoise_icon));
        mFrames.add(new Frames(R.drawable.annette_stripedsassafras, "Annette",
                "Female", "Striped Sassafras", "Rectangle", 95, R.drawable.annette_stripedsassafras_icon));

        mFrames.add(new Frames(R.drawable.carnaby_bluecoral, "Carnaby",
                "Female", "Blue Coral", "Square", 95, R.drawable.carnaby_bluecoral_icon));
        mFrames.add(new Frames(R.drawable.carnaby_burntlemontortoise, "Carnaby",
                "Female", "Burnt Lemon Tortoise", "Square", 95, R.drawable.carnaby_burntlemontortoise_icon));

        mFrames.add(new Frames(R.drawable.chelsea_grapefruitsoda, "Chelsea",
                "Female", "Grapefruit Soda", "Round", 135, R.drawable.chelsea_grapefruitsoda_icon));
        mFrames.add(new Frames(R.drawable.chelsea_whistlergrey, "Chelsea",
                "Female", "Whistler Grey", "Round", 135, R.drawable.chelsea_whistlergrey_icon));

        mFrames.add(new Frames(R.drawable.daisy_aureliatortoise, "Daisy",
                "Female", "Aurelia Tortoise", "Round", 95, R.drawable.daisy_aureliatortoise_icon));
        mFrames.add(new Frames(R.drawable.daisy_stripedmolasses, "Daisy",
                "Female", "Striped Molasses", "Round", 95, R.drawable.daisy_stripedmolasses_icon));

        mFrames.add(new Frames(R.drawable.duval_blueslatefade, "Duval",
                "Female", "Blue Slate Fade", "Square", 95, R.drawable.duval_blueslatefade_icon));
        mFrames.add(new Frames(R.drawable.duval_whiskeytortoise, "Duval",
                "Female", "Whiskey Tortoise", "Square", 95, R.drawable.duval_whiskeytortoise_icon));

        mFrames.add(new Frames(R.drawable.upton_oakbarrel, "Upton",
                "Female", "Oak Barrel", "Rectangle", 95, R.drawable.upton_oakbarrel_icon));
        mFrames.add(new Frames(R.drawable.upton_seasmoketortoise, "Upton",
                "Female", "Seasmoke Tortoise", "Rectangle", 95, R.drawable.upton_seasmoketortoise_icon));

    }

    private void populateListView() {
        //Alter the layout so that the overhead text describes what category of frames we're on
        TextView typeText = (TextView) findViewById(R.id.overheadText);
        if(mFrames.isEmpty())
            typeText.setText("Uh-oh! We couldn't find anything related to your search!");
        else
            typeText.setText("Browsing Frames: " + GENDER);

        ArrayAdapter<Frames> adapter = new MyListAdapter();
        ListView list = (ListView)findViewById(R.id.framesListView);
        list.setAdapter(adapter);
    }

    private void createKeyWordList() {
        //Make a new list from the old one by going through each
        List<Frames> newList = new ArrayList<Frames>();
        for(int i = 0; i < mFrames.size(); i++) {
            if(mFrames.get(i).getName().toLowerCase().contains(KEYWORD.toLowerCase())) {
                newList.add(new Frames(mFrames.get(i).getPictureID(), mFrames.get(i).getName(),
                        mFrames.get(i).getGender(), mFrames.get(i).getColor(), mFrames.get(i).getShape(),
                        mFrames.get(i).getPrice(),mFrames.get(i).getIconID()));
            }
        }
        mFrames.clear();
        mFrames = null;
        mFrames = newList;
    }

    private void createSquareList() {
        //final MyListAdapter adapter = new MyListAdapter();
        //Make a new list from the old one by going through each
        List<Frames> newList = new ArrayList<Frames>();
        for(int i = 0; i < mFrames.size(); i++) {
            if(mFrames.get(i).getShape().equals("Square")) {
                newList.add(new Frames(mFrames.get(i).getPictureID(), mFrames.get(i).getName(),
                        mFrames.get(i).getGender(), mFrames.get(i).getColor(), mFrames.get(i).getShape(),
                        mFrames.get(i).getPrice(),mFrames.get(i).getIconID()));
            }
        }
        mFrames.clear();
        mFrames = null;
        mFrames = newList;
    }

    private void createRectangleList() {
        //final MyListAdapter adapter = new MyListAdapter();
        //Make a new list from the old one by going through each
        List<Frames> newList = new ArrayList<Frames>();
        for(int i = 0; i < mFrames.size(); i++) {
            if(mFrames.get(i).getShape().equals("Rectangle")) {
                newList.add(new Frames(mFrames.get(i).getPictureID(), mFrames.get(i).getName(),
                        mFrames.get(i).getGender(), mFrames.get(i).getColor(), mFrames.get(i).getShape(),
                        mFrames.get(i).getPrice(),mFrames.get(i).getIconID()));
            }
        }
        mFrames.clear();
        mFrames = null;
        mFrames = newList;
    }

    private void createdRoundList() {
        //final MyListAdapter adapter = new MyListAdapter();
        //Make a new list from the old one by going through each
        List<Frames> newList = new ArrayList<Frames>();
        for(int i = 0; i < mFrames.size(); i++) {
            if(mFrames.get(i).getShape().equals("Round")) {
                newList.add(new Frames(mFrames.get(i).getPictureID(), mFrames.get(i).getName(),
                        mFrames.get(i).getGender(), mFrames.get(i).getColor(), mFrames.get(i).getShape(),
                        mFrames.get(i).getPrice(),mFrames.get(i).getIconID()));
            }
        }
        mFrames.clear();
        mFrames = null;
        mFrames = newList;
    }

    private void registerTapCallback() {
        ListView list = (ListView)findViewById(R.id.framesListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {

                Frames choice = mFrames.get(position);
                //String message = "These are the " + clickedFrames.getName() + " frames";
                //Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), EyewareActivity.class);
                i.putExtra("selectedFrames", choice.getPictureID());
                i.putExtra("framesName", choice.getName());
                i.putExtra("framesColor", choice.getColor());
                i.putExtra("framesPrice", choice.getPrice());

                startActivity(i);

            }
        });

        if(GENDER.equals("Favorites")) {
            ImageView imgClearFavorites = (ImageView)findViewById(R.id.imgClearFavorites);
            imgClearFavorites.setImageResource(android.R.drawable.ic_delete);
            TextView txtClearFavorites = (TextView)findViewById(R.id.txtClearFavorites);
            txtClearFavorites.setText("Clear Favorites");

            imgClearFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences settings = getSharedPreferences("Favorites", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.clear();

                    // Commit the edits!
                    editor.commit();
                    mFrames.clear();
                    populateListView();
                }
            });
        }

        final RadioGroup radioShapes = (RadioGroup)findViewById(R.id.radioShapes);
        radioShapes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            //RadioButton radioSquare = (RadioButton)findViewById(R.id.radioSquare);
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioAll) {
                    populateFramesList();
                    populateListView();
                }
                if(checkedId == R.id.radioSquare) {
                    populateFramesList();
                    createSquareList();
                    populateListView();
                }
                if(checkedId == R.id.radioRectangle) {
                    populateFramesList();
                    createRectangleList();
                    populateListView();
                }
                if(checkedId == R.id.radioRound) {
                    populateFramesList();
                    createdRoundList();
                    populateListView();
                }
            }


        });

        final SearchView searchBox = (SearchView)findViewById(R.id.searchBox);
        searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if(radioShapes.getCheckedRadioButtonId() != R.id.radioAll)
                    radioShapes.check(R.id.radioAll);
                KEYWORD = query;
                populateFramesList();
                populateListView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(radioShapes.getCheckedRadioButtonId() != R.id.radioAll)
                    radioShapes.check(R.id.radioAll);
                KEYWORD = newText;
                populateFramesList();
                populateListView();
                return false;
            }
        });

        searchBox.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                KEYWORD = "";
                return false;
            }
        });

    }

    private class MyListAdapter extends ArrayAdapter<Frames> {
        public MyListAdapter() {
            super(MainActivity.this, R.layout.item_view, mFrames);
        }
        @Override
        public View getView(int position, View convert, ViewGroup parent) {

            View itemView = convert;
            if(itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);

            //Find which frames we're working with
            Frames currentFrames = mFrames.get(position);

            //Fill the View
            ImageView imageView = (ImageView)itemView.findViewById(R.id.image_icon);
            imageView.setImageResource(currentFrames.getIconID());

            //Names
            TextView frameName = (TextView)itemView.findViewById(R.id.txtFrameName);
            frameName.setTypeface(frameName.getTypeface(), Typeface.BOLD);
            frameName.setText(currentFrames.getName());

            //Color
            TextView color = (TextView)itemView.findViewById(R.id.txtColor);
            color.setTypeface(color.getTypeface(), Typeface.ITALIC);
            color.setText(currentFrames.getColor());

            //Price
            TextView price = (TextView)itemView.findViewById(R.id.txtPrice);
            price.setText("$" + currentFrames.getPrice() + ".00");

            return itemView;
            //return super.getView(position, convertView, parent);
        }

    }
}
