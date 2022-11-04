package ca.sfu.dba56.cmpt276;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

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
    private double minScore;
    private double maxScore;
    private int lengthOfDecimal;
    private double decimalRange;

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
        double range = achievements.calculateLevelRange(minScore, maxScore);
        if (maxScore - minScore > 8) {
            Toast.makeText(this, "range " + range, Toast.LENGTH_SHORT).show();
            double newStartRange = 0;
            achievementLevels += "Worst Game Level: Range < " + minScore + "\n\n";
            for (int i = 1; i < achievements.getNumOfBoundedLevels() + 1; i++) {
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
            achievementLevels += "Legendary Level: Range > " + maxScore;
        }

        else{
            getDecimalPlaces(achievements.calculateLevelRange(minScore,maxScore));
            getAddingValue(lengthOfDecimal);

            double newStartRange = 0;
            String lengthAfterDecimal = "";
            for (int i = 0; i < lengthOfDecimal; i++){
                lengthAfterDecimal += "#";
            }
            DecimalFormat decimalFormat = new DecimalFormat("0." + lengthAfterDecimal);
            achievementLevels += "Worst Game Level: Range < " + minScore + "\n\n";
            for (int i = 1; i < achievements.getNumOfBoundedLevels() + 1; i++){
                achievementLevels += achievements.getAchievementLevel(i);
                achievementLevels += " Range: [";
                if (i == 1){
                    achievementLevels += achievements.calculateMinMaxScore(manager.get(indexOfGame).getMinPoorScoreFromConfig(), numPlayersInt);
                    achievementLevels += ", " + (minScore + decimalFormat.format(decimalRange)) + "]\n\n";
                    newStartRange = (minScore + Double.parseDouble(decimalFormat.format(decimalRange)));
                } else if (i == achievements.getNumOfBoundedLevels()){
                    achievementLevels += " " + (newStartRange) + ", " + (maxScore) + "]\n\n";
                } else{
                    achievementLevels += " " + (newStartRange + Double.parseDouble(decimalFormat.format(decimalRange))) + ", " +
                            (newStartRange + Double.parseDouble(decimalFormat.format(decimalRange)) + Double.parseDouble(decimalFormat.format(range)) + "]\n\n");
                    newStartRange += Double.parseDouble(decimalFormat.format(decimalRange)) + Double.parseDouble(decimalFormat.format(range));
                }
            }
            achievementLevels += "Legendary Level: Range > " + maxScore;
        }
        displayAchievements.setText(achievementLevels);
    }

    private void getDecimalPlaces(double userInput){
        String strUserInput = "" + userInput;
        String[] result = strUserInput.split("\\.");
        lengthOfDecimal = result[1].length();
    }

    private void getAddingValue(int lengthOfDecimal){
        decimalRange = 0.1;
        for (int i = 0; i < lengthOfDecimal; i++){
            decimalRange *= 0.1;
        }
    }


    private void calculateMinAndMaxScore() {
        minScore = achievements.calculateMinMaxScore(manager.get(indexOfGame).getMinPoorScoreFromConfig(), numPlayersInt);
        maxScore = achievements.calculateMinMaxScore(manager.get(indexOfGame).getMaxBestScoreFromConfig(), numPlayersInt);
    }
}
