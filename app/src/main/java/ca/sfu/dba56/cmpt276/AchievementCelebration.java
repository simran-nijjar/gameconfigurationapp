package ca.sfu.dba56.cmpt276;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ca.sfu.dba56.cmpt276.model.Achievements;
import ca.sfu.dba56.cmpt276.model.ConfigurationsManager;

public class AchievementCelebration extends AppCompatActivity {
    private final String FRUITS = "Fruits";
    private final String FANTASY = "Fantasy";
    private final String STAR_WARS = "Star Wars";
    private ConfigurationsManager manager = ConfigurationsManager.getInstance();
    private String gameTheme;
    private int currentConfigPosition;
    private int indexOfGame;
    private int selectedTheme;
    private Achievements achievements;
    private String levelAchieved;
    private boolean isNewGame;
    private MediaPlayer mediaPlayer;
    private Animation fadeOut;
    private TextView userLevelAchieved;
    private int indexLevelAchieved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        gameTheme = getAchievementTheme(this);
        Toast.makeText(AchievementCelebration.this, "theme " + gameTheme, Toast.LENGTH_SHORT).show();
        achievements = new Achievements(getAchievementTheme(this));
        currentConfigPosition = manager.getIndex();

        Bundle bundle = getIntent().getExtras(); // from game history
        if (bundle != null){ //If game is being edited
            isNewGame = false;
            indexOfGame = bundle.getInt("selected game"); //index of game is game being edited
        } else { //If new game is being added
            isNewGame = true;
            indexOfGame = manager.getItemAtIndex(currentConfigPosition).getSizeOfListOfConfigs() - 1; //index of game is last position
        }
        //Get the level user achieved when editing or adding new game
        levelAchieved = manager.getItemAtIndex(currentConfigPosition).getGame(indexOfGame).getLevelAchieved();
        //Get the index position of level achieved
        indexLevelAchieved = manager.getItemAtIndex(currentConfigPosition).getGame(indexOfGame).getIndexGameLevelAchieved();
        displayAchievementThemeLayout();
        storeSelectedAchievementTheme();
    }

    //Display the matching theme layout for achievement celebration
    private void displayAchievementThemeLayout(){
        if (gameTheme.equals(FRUITS)){
            setContentView(R.layout.fruitsalertdialog);
            showFruitsResult();
        }
        else if (gameTheme.equals(FANTASY)){
            setContentView(R.layout.fantasyalertdialog);
            showFantasyResult();
        }
        else if (gameTheme.equals(STAR_WARS)){
            setContentView(R.layout.starwarsalertdialog);
            showStarWarsResult();
        }
    }

    //Change the theme if user changes theme using the dropdown
    private void storeSelectedAchievementTheme(){
        Spinner dropdown = findViewById(R.id.dropdownTheme);
        String[] themesArray = getResources().getStringArray(R.array.achievementThemes);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AchievementCelebration.this, R.array.achievementThemes, android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        //Display Set a Theme prompt
        dropdown.setPrompt(getResources().getString(R.string.select_theme_prompt));

        for (int i = 0; i < themesArray.length; i++) {
            if (themesArray[i].equals(gameTheme)) {
                dropdown.setSelection(i);
            }
        }
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //When user selects a new theme, recreate activity to display theme levels and achievements
                selectedTheme = dropdown.getSelectedItemPosition();
                String[] themesArray = getResources().getStringArray(R.array.achievementThemes);
                for (int i = 0; i < themesArray.length; i++){
                    if (i == selectedTheme && !achievements.getAchievementTheme().equals(themesArray[i])) {
                        final String achievementTheme = themesArray[i];
                        saveAchievementTheme(achievementTheme);
                        achievements.setAchievementTheme(achievementTheme);
                        //Set the level achieved for the new theme selected at the same index position of previously selected level
                        manager.getItemAtIndex(currentConfigPosition).getGame(indexOfGame).setLevelAchieved(achievements.getAchievementLevel(indexLevelAchieved));
                        AchievementCelebration.this.recreate();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });
    }

    private void playSound(){
        if(gameTheme.equals(FRUITS)){
            mediaPlayer = MediaPlayer.create(this, R.raw.fruitslice);
        }
        if(gameTheme.equals(FANTASY)){
            mediaPlayer = MediaPlayer.create(this, R.raw.fairysound);
        }
        if(gameTheme.equals(STAR_WARS)){
            mediaPlayer = MediaPlayer.create(this, R.raw.lightsaber);
        }
        mediaPlayer.start();
    }

    // pop up a window to show achievement level for fruits theme
    private void showFruitsResult(){
        //Play sound
        playSound();
        //Display the level user achieved
        userLevelAchieved = findViewById(R.id.levelAchievedTxt);
        userLevelAchieved.setText(levelAchieved);

        //Display the animation
        fadeOut = AnimationUtils.loadAnimation(this,R.anim.fadeout);
        ImageView foxCelebrationAnimation = findViewById(R.id.celebrationAlertsImage);
        foxCelebrationAnimation.startAnimation(fadeOut);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //Don't have animation code here, animation will not end properly
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //End the animation
                foxCelebrationAnimation.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //Do nothing
            }
        });
        //Set up button to leave achievement celebration page
        Button okBtn = findViewById(R.id.appleOkBtn);
        okBtn.setOnClickListener(v -> {
            //If adding a new game, go to game config
            Intent refresh;
            if (isNewGame){
                refresh = new Intent(AchievementCelebration.this, ViewConfiguration.class);
            } else{ //If editing a game, go to history
                refresh = new Intent(AchievementCelebration.this, GameHistory.class);
            }
            startActivity(refresh);
        });
    }

    // pop up a window to show achievement level for fantasy theme
    private void showFantasyResult(){
        //Play sound
        playSound();
        //Display the level user achieved
        userLevelAchieved = findViewById(R.id.levelAchievedTxt);
        userLevelAchieved.setText(levelAchieved);

        //Display the animation
        fadeOut = AnimationUtils.loadAnimation(this,R.anim.fadeout);
        ImageView foxCelebrationAnimation = findViewById(R.id.celebrationAlertsImage);
        foxCelebrationAnimation.startAnimation(fadeOut);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //Don't have animation code here, animation will not end properly
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //End the animation
                foxCelebrationAnimation.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //Do nothing
            }
        });
        //Set up button to leave achievement celebration page
        Button okBtn = findViewById(R.id.starOkBtn);
        okBtn.setOnClickListener(v -> {
            //If adding a new game, go to game config
            Intent refresh;
            if (isNewGame){
                refresh = new Intent(AchievementCelebration.this, ViewConfiguration.class);
            } else{ //If editing a game, go to history
                refresh = new Intent(AchievementCelebration.this, GameHistory.class);
            }
            startActivity(refresh);
        });
    }

    // pop up a window to show achievement level for starwars theme
    private void showStarWarsResult(){
        //Play sound
        playSound();
        //Display the level user achieved
        userLevelAchieved = findViewById(R.id.levelAchievedTxt);
        userLevelAchieved.setText(levelAchieved);

        //Display the animation
        fadeOut = AnimationUtils.loadAnimation(this,R.anim.fadeout);
        ImageView foxCelebrationAnimation = findViewById(R.id.celebrationAlertsImage);
        foxCelebrationAnimation.startAnimation(fadeOut);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //Don't have animation code here, animation will not end properly
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //End the animation
                foxCelebrationAnimation.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //Do nothing
            }
        });
        //Set up button to leave achievement celebration page
        Button okBtn = findViewById(R.id.yodaOkBtn);
        okBtn.setOnClickListener(v -> {
            //If adding a new game, go to game config
            Intent refresh;
            if (isNewGame){
                refresh = new Intent(AchievementCelebration.this, ViewConfiguration.class);
            } else{ //If editing a game, go to history
                refresh = new Intent(AchievementCelebration.this, GameHistory.class);
            }
            startActivity(refresh);
        });
    }

    private void saveAchievementTheme(String theme){
        SharedPreferences preferences = this.getSharedPreferences("Theme Preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Achievement Theme", theme);
        editor.apply();
    }

    static public String getAchievementTheme(Context context){
        SharedPreferences preferences = context.getSharedPreferences("Theme Preferences", MODE_PRIVATE);
        String defaultTheme = context.getResources().getString(R.string.defaultTheme);
        return preferences.getString("Achievement Theme",defaultTheme);
    }
}