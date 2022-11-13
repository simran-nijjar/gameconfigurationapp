package ca.sfu.dba56.cmpt276;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import ca.sfu.dba56.cmpt276.model.Achievements;
import ca.sfu.dba56.cmpt276.model.ConfigurationsManager;

/*
* class view achievements activity
* activity displays achievements after entering the potential number of players
* it gives a different achievement levels corresponding to the expected poor/great values from config
* and limits the input of user if it goes over the limit
*/
public class ViewAchievements extends AppCompatActivity {

    private final String FANTASY = "Fantasy";
    private final String STAR_WARS = "Star Wars";

    private ConfigurationsManager manager = ConfigurationsManager.getInstance();
    private Achievements achievements;
    private EditText numPlayersFromUser;
    private String numPlayersAsStr;
    private int numPlayers;
    private int indexOfGame;
    private TextView noAchievementsDisplayed;
    private TextView displayAchievements;
    private int minScore;
    private int maxScore;
    private final int MAX_PLAYERS = 100000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        manager.changeTheme(this);
        super.onCreate(savedInstanceState);
        achievements = new Achievements(getAchievementTheme(this));
        setContentView(R.layout.view_achievements);

        noAchievementsDisplayed = findViewById(R.id.emptyAchievementsList);
        numPlayersFromUser = findViewById(R.id.userInputPlayers);
        displayThemeRadioButtons();
        updateUI();
        Bundle b = getIntent().getExtras();
        indexOfGame = b.getInt("Achievement Game Name");
        displayAchievements = findViewById(R.id.listOfAchievements);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ViewAchievements.class);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Change theme in view config if back button is clicked
                Intent intent = ViewConfiguration.makeIntent(ViewAchievements.this);
                startActivity(intent);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        numPlayersFromUser.addTextChangedListener(textWatcher);}

    private void displayThemeRadioButtons(){
        RadioGroup themeGroup = findViewById(R.id.radioBtnsThemes);
        String[] themesArray = getResources().getStringArray(R.array.achievementThemes);

        for (int i = 0; i < themesArray.length; i++){
            final String achievementTheme = themesArray[i];

            RadioButton btn = new RadioButton(this);
            btn.setText(getString(R.string.display_string_option, achievementTheme));
            btn.setOnClickListener(v ->{
                saveAchievementTheme(achievementTheme);
                achievements.setAchievementTheme(achievementTheme);
                ViewAchievements.this.recreate();
            });
            themeGroup.addView(btn);
            if (achievementTheme.equals(getAchievementTheme(this))){
                achievements.setAchievementTheme(achievementTheme);
                btn.setChecked(true);
            }
        }
    }

    private void saveAchievementTheme(String theme){
        //Saves the achievement theme in shared preferences
        SharedPreferences preferences = this.getSharedPreferences("Theme Preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Achievement Theme", theme);
        editor.apply();
    }

    static public String getAchievementTheme(Context context){
        //Gets the saved achievement theme depending on clicked radio button
        SharedPreferences preferences = context.getSharedPreferences("Theme Preferences", MODE_PRIVATE);
        String defaultTheme = context.getResources().getString(R.string.defaultTheme);
        return preferences.getString("Achievement Theme",defaultTheme);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            noAchievementsDisplayed.setText(R.string.empty_achievements_msg);
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            numPlayersAsStr = numPlayersFromUser.getText().toString();
            if(!numPlayersAsStr.isEmpty()){
                numPlayers = Integer.parseInt(numPlayersAsStr);
                displayMaxPlayerMsg(numPlayers);
                if (numPlayers < 1){
                    noAchievementsDisplayed.setText(R.string.invalid_players_msg);
                }
                else {
                    noAchievementsDisplayed.setVisibility(View.GONE);
                    displayAchievementLevels();
                }
            }
            else{
                noAchievementsDisplayed.setText(R.string.empty_achievements_msg);
                noAchievementsDisplayed.setVisibility(View.VISIBLE);
                displayAchievements.setText("");
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
            numPlayersAsStr = numPlayersFromUser.getText().toString();
            if (numPlayersAsStr.startsWith("0")){
                numPlayersFromUser.setText("");
                noAchievementsDisplayed.setText(R.string.invalid_players_msg);
            }
        }
    };

    private void displayMaxPlayerMsg(int players) {
        if (players >= MAX_PLAYERS) {
            AlertDialog alertDialog = new AlertDialog.Builder(ViewAchievements.this).create();
            alertDialog.setTitle(getString(R.string.too_many_players));
            alertDialog.setMessage(getString(R.string.Sorry_too_many_players));
            alertDialog.setButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //Stay on ViewAchievement activity
                }
            });
            alertDialog.show();
            //set num of player to the minimum
            numPlayersFromUser = findViewById(R.id.userInputPlayers);
            numPlayersFromUser.setText("1");
        }
    }

    private void displayAchievementLevels(){
        String achievementLevels = "";
        numPlayersFromUser = findViewById(R.id.userInputPlayers);
        numPlayersAsStr = numPlayersFromUser.getText().toString();
        numPlayers = Integer.parseInt(numPlayersAsStr);

        calculateMinAndMaxScore();
        int range = achievements.calculateLevelRange(minScore, maxScore);
        boolean lessLevels = false;
        //Achievement levels will be displayed in ranges when max - min > 8
        if (Math.abs(maxScore - minScore) > 8) {
            achievementLevels = displayAchievementRanges(achievementLevels, range, lessLevels);
        }
        //Achievement levels will be displayed in scores otherwise
        else{
            achievementLevels = displayAchievementScores(achievementLevels);
        }
        displayAchievements.setText(achievementLevels);
    }

    private String getWorstRangeOfTheme(String theme){
        //Worst range level depends on selected theme in radio button
        if (theme.equals(FANTASY)){
            return getString(R.string.worst_game_level_range_boundary_fantasy) //Add worse range than expected lowest range achievement level
                    + minScore + "\n\n";
        }
        if (theme.equals(STAR_WARS)){
            return  getString(R.string.worst_game_level_range_boundary_starwars) //Add worse range than expected lowest range achievement level
                    + minScore + "\n\n";
        }
        return getString(R.string.worst_game_level_range_boundary_fruits) //Add worse range than expected lowest range achievement level
                + minScore + "\n\n";
    }

    private String getBestRangeOfTheme(String theme, int newStartRange){
        //Best range level depends on selected theme in radio button
        if (theme.equals(FANTASY)){
            return getString(R.string.best_level_range_boundary_fantasy) //Add best level range when not all levels can be calculated for
                    + (newStartRange);
        }
        if (theme.equals(STAR_WARS)){
            return getString(R.string.best_level_range_boundary_starwars) //Add best level range when not all levels can be calculated for
                    + (newStartRange);
        }
        return getString(R.string.best_level_range_boundary_fruits) //Add best level range when not all levels can be calculated for
                + (newStartRange);
    }

    private String getWorstScoreOfTheme(String theme){
        //Worst score level depends on selected theme in radio button
        if (theme.equals(FANTASY)){
            return getString(R.string.worst_game_level_score_boundary_fantasy)
                    + minScore +"\n\n";
        }
        if (theme.equals(STAR_WARS)){
            return getString(R.string.worst_game_level_score_boundary_starwars)
                    + minScore +"\n\n";
        }
        return getString(R.string.worst_game_level_score_boundary_fruits)
                + minScore +"\n\n";
    }

    private String getBestScoreOfTheme(String theme){
        //Best score level depends on selected theme in radio button
        if (theme.equals(FANTASY)){
            return getString(R.string.best_level_score_boundary_fantasy)
                    + maxScore;
        }
        if (theme.equals(STAR_WARS)){
            return getString(R.string.best_level_score_boundary_starwars)
                    + maxScore;
        }
        return getString(R.string.best_level_score_boundary_fruits)
                + maxScore;
    }

    @NonNull
    private String displayAchievementRanges(String achievementLevels, int range, boolean lessThanEightLevels) {
        int newStartRange = 0;
        achievementLevels += getWorstRangeOfTheme(getSavedAchievementTheme());
        for (int i = 1; i < achievements.getNumOfBoundedLevels() + 1; i++) {
            if (newStartRange + 1 < Math.abs(maxScore)) {
                achievementLevels += achievements.getAchievementLevel(i);
                achievementLevels += " Range: [";
                if (i == 1) {
                    achievementLevels += achievements.calculateMinMaxScore(manager.getItemAtIndex(indexOfGame).getMinPoorScoreFromConfig(), numPlayers);
                    achievementLevels += ", " + (minScore + range) + "]\n\n";
                    newStartRange = (minScore + range);
                } else if (newStartRange + range > Math.abs(maxScore)) {
                    achievementLevels += " " + (newStartRange + 1) + ", " + (maxScore) + "]\n\n";
                    newStartRange = maxScore;
                    lessThanEightLevels = true;
                } else if (i == achievements.getNumOfBoundedLevels()) {
                    achievementLevels += " " + (newStartRange) + ", " + (maxScore) + "]\n\n";
                } else {
                    achievementLevels += " " + (newStartRange + 1) + ", " + (newStartRange + 1 + range) + "]\n\n";
                    newStartRange += 1 + range;
                }
            } else{
                lessThanEightLevels = true;
            }
        }
        if (lessThanEightLevels) { //Add best level range when not all levels can be calculated for
            achievementLevels += getBestRangeOfTheme(getSavedAchievementTheme(), (newStartRange + 1));
        } else { //Add best level range when all levels can be calculated for
            achievementLevels += getBestRangeOfTheme(getSavedAchievementTheme(), maxScore);
        }
        return achievementLevels;
    }

    private String getSavedAchievementTheme() {
        //Gets the string of saved achievement
        return achievements.getAchievementTheme();
    }

    @NonNull
    private String displayAchievementScores(String achievementLevels) {
        achievementLevels += getWorstScoreOfTheme(getSavedAchievementTheme()); //Add worse than expected lowest score achievement level
        for (int i = 1; i < (maxScore - minScore + 1); i++){
            achievementLevels += achievements.getAchievementLevel(i);
            achievementLevels += getString(R.string.score_part1);
            achievementLevels += (achievements.calculateMinMaxScore(manager.getItemAtIndex(indexOfGame).getMinPoorScoreFromConfig(), numPlayers) + i - 1);
            achievementLevels += getString(R.string.score_part2);
        }
        achievementLevels += getBestScoreOfTheme(getSavedAchievementTheme()); //Add best score level for scores
        return achievementLevels;
    }

    private void calculateMinAndMaxScore() {
        minScore = achievements.calculateMinMaxScore(manager.getItemAtIndex(indexOfGame).getMinPoorScoreFromConfig(), numPlayers);
        maxScore = achievements.calculateMinMaxScore(manager.getItemAtIndex(indexOfGame).getMaxBestScoreFromConfig(), numPlayers);
    }
}
