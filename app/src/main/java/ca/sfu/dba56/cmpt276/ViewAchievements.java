package ca.sfu.dba56.cmpt276;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

    private ConfigurationsManager manager = ConfigurationsManager.getInstance();
    private Achievements achievements = new Achievements();
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_achievements);

        noAchievementsDisplayed = findViewById(R.id.emptyAchievementsList);
        numPlayersFromUser = findViewById(R.id.userInputPlayers);
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
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        numPlayersFromUser.addTextChangedListener(textWatcher);}

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
            AlertDialog alertDialog = new AlertDialog.Builder(ViewAchievements.this).create(); //Read Update
            alertDialog.setTitle("Too many players");
            alertDialog.setMessage("Sorry, that's too many players. Please try a smaller number");
            alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
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

    @NonNull
    private String displayAchievementRanges(String achievementLevels, int range, boolean lessThanEightLevels) {
        int newStartRange = 0;
        achievementLevels += "Worst Game Level: Range < " + minScore + "\n\n";
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
        if (lessThanEightLevels) {
            achievementLevels += "Legendary Level: Range >= " + (newStartRange + 1);
        } else {
            achievementLevels += "Legendary Level: Range > " + maxScore;
        }
        return achievementLevels;
    }

    @NonNull
    private String displayAchievementScores(String achievementLevels) {
        achievementLevels += "Worst Game Level: Score < " + minScore +"\n\n";
        for (int i = 1; i < (maxScore - minScore + 1); i++){
            achievementLevels += achievements.getAchievementLevel(i);
            achievementLevels += " Score : [";
            achievementLevels += (achievements.calculateMinMaxScore(manager.getItemAtIndex(indexOfGame).getMinPoorScoreFromConfig(), numPlayers) + i - 1);
            achievementLevels += "]\n\n";
        }
        achievementLevels += "Legendary Level: Score >= " + maxScore;
        return achievementLevels;
    }

    private void calculateMinAndMaxScore() {
        minScore = achievements.calculateMinMaxScore(manager.getItemAtIndex(indexOfGame).getMinPoorScoreFromConfig(), numPlayers);
        maxScore = achievements.calculateMinMaxScore(manager.getItemAtIndex(indexOfGame).getMaxBestScoreFromConfig(), numPlayers);
    }
}
