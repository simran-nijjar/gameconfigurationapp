package ca.sfu.dba56.cmpt276;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import ca.sfu.dba56.cmpt276.model.Achievements;
import ca.sfu.dba56.cmpt276.model.ConfigurationsManager;

public class ViewAchievements extends AppCompatActivity {
    ConfigurationsManager manager = ConfigurationsManager.getInstance();
    Achievements achievements = new Achievements();
    private EditText numPlayers;
    private String numPlayersStr;
    private int numPlayersInt;
    private int indexOfGame;
    private TextView noAchievementsDisplayed;
    private TextView displayAchievements;
    private int minScore;
    private int maxScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_achievements);
        noAchievementsDisplayed = findViewById(R.id.emptyAchievementsList);
        numPlayers = findViewById(R.id.userInputPlayers);
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

    private void updateUI(){
        numPlayers.addTextChangedListener(textWatcher);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, ViewAchievements.class);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            noAchievementsDisplayed.setText(R.string.empty_achievements_msg);
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            numPlayersStr = numPlayers.getText().toString();
            if(!numPlayersStr.isEmpty()){
                numPlayersInt = Integer.parseInt(numPlayersStr);
                if (numPlayersInt < 1){
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
        }
    };

    private void displayAchievementLevels(){
        String achievementLevels = "";
        numPlayers = findViewById(R.id.userInputPlayers);
        numPlayersStr = numPlayers.getText().toString();
        numPlayersInt = Integer.parseInt(numPlayersStr);

        calculateMinAndMaxScore();
        int range = achievements.calculateLevelRange(minScore, maxScore);
        boolean lessLevels = false;
        if (Math.abs(maxScore - minScore) > 8) {
            int newStartRange = 0;
            achievementLevels += "Worst Game Level: Range < " + minScore + "\n\n";
            for (int i = 1; i < achievements.getNumOfBoundedLevels() + 1; i++) {
                if (newStartRange + 1 + range < maxScore) {
                    achievementLevels += achievements.getAchievementLevel(i);
                    achievementLevels += " Range: [";
                    if (i == 1) {
                        achievementLevels += achievements.calculateMinMaxScore(manager.get(indexOfGame).getMinPoorScoreFromConfig(), numPlayersInt);
                        achievementLevels += ", " + (minScore + range) + "]\n\n";
                        newStartRange = (minScore + range);
                    } else if (i == achievements.getNumOfBoundedLevels()) {
                        achievementLevels += " " + (newStartRange) + ", " + (maxScore) + "]\n\n";
                    } else {
                        achievementLevels += " " + (newStartRange + 1) + ", " + (newStartRange + 1 + range) + "]\n\n";
                        newStartRange += 1 + range;
                    }
                }
                else{
                    lessLevels = true;
                }
            }
            if (lessLevels){
                achievementLevels += "Legendary Level: Range >= " + (newStartRange + 1);
            }
            else {
                achievementLevels += "Legendary Level: Range > " + maxScore;
            }
        }
        else{
            achievementLevels += "Worst Game Level: Score < " + minScore +"\n\n";
            for (int i = 1; i <= (maxScore - minScore + 1); i++){
                achievementLevels += achievements.getAchievementLevel(i);
                achievementLevels += " Score : [";
                achievementLevels += (achievements.calculateMinMaxScore(manager.get(indexOfGame).getMinPoorScoreFromConfig(), numPlayersInt) + i - 1);
                achievementLevels += "]\n\n";
            }
            achievementLevels += "Legendary Level: Score > " + maxScore;
        }
        displayAchievements.setText(achievementLevels);
    }



    private void calculateMinAndMaxScore() {
        minScore = achievements.calculateMinMaxScore(manager.get(indexOfGame).getMinPoorScoreFromConfig(), numPlayersInt);
        maxScore = achievements.calculateMinMaxScore(manager.get(indexOfGame).getMaxBestScoreFromConfig(), numPlayersInt);
    }
}
