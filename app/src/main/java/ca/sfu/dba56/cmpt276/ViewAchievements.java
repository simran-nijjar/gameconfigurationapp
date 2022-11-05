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

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private double lengthOfDecimalRange;

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

            double newStartRange = 0;
            formatDouble(range);
            achievementLevels += "Worst Game Level: Range < " + minScore + "\n\n";
            for (int i = 1; i < achievements.getNumOfBoundedLevels() + 1; i++){
                achievementLevels += achievements.getAchievementLevel(i);
                achievementLevels += " Range: [";
                if (i == 1){
                    Toast.makeText(this, "range " + decimalRange, Toast.LENGTH_SHORT).show();
                    achievementLevels += achievements.calculateMinMaxScore(manager.get(indexOfGame).getMinPoorScoreFromConfig(), numPlayersInt);
                    achievementLevels += ", " + formatDouble(minScore + formatDouble(range)) + "]\n\n";
                    newStartRange = formatDouble(minScore + formatDouble(range));
                    formatDouble(newStartRange);
                } else if (i == achievements.getNumOfBoundedLevels()){
                    achievementLevels += " " + (formatDouble(newStartRange)) + ", " + (maxScore) + "]\n\n";
                } else{
                    achievementLevels += " " + (formatDouble(newStartRange) + (decimalRange*decimalRange)) + ", " +
                            formatDouble((formatDouble(newStartRange) + formatDouble(decimalRange) + formatDouble(range))) + "]\n\n";
                    newStartRange += formatDouble(decimalRange) + formatDouble(range);
                }
            }
            achievementLevels += "Legendary Level: Range > " + maxScore;
        }
        displayAchievements.setText(achievementLevels);
    }

    private void getDecimalPlaces(double userInput){
        String splitter = Double.toString(Math.abs(userInput));
        int intPlaces = splitter.indexOf('.');
        int decPlaces = splitter.length() - intPlaces - 1;
        Toast.makeText(this, "length splitter + userInput " + decPlaces + userInput, Toast.LENGTH_LONG).show();
        lengthOfDecimal = decPlaces;
//        Toast.makeText(this, "lengthOfDecimal " + lengthOfDecimal, Toast.LENGTH_SHORT).show();
        getAddingValue();
    }

    private void getAddingValue(){
        decimalRange = 0.1;
        for (int i = 0; i < lengthOfDecimal; i++){
            decimalRange *= decimalRange;
        }
        //2
        Toast.makeText(this, "decimalRange " + decimalRange, Toast.LENGTH_SHORT).show();
        decimalRange = formatDouble(decimalRange);
        Toast.makeText(this, "decimalRange formatted " + decimalRange, Toast.LENGTH_SHORT).show();
        getLengthOfRange();
    }

    private void getLengthOfRange() {
        Double formattedDR = formatDouble(decimalRange);
//        Toast.makeText(this, " dr " + decimalRange, Toast.LENGTH_SHORT).show();
        String length = " " + decimalRange;
//        String[] result = decimalRange.toString().split("\\.");
//        lengthOfDecimalRange = result[1].length();
//        Toast.makeText(this, "3 = " + lengthOfDecimalRange, Toast.LENGTH_SHORT).show();
    }

    private Double formatDouble(double value) {
        BigDecimal bd = new BigDecimal(value).setScale(lengthOfDecimal, RoundingMode.CEILING);
        double newValue = bd.doubleValue();
        return newValue;
//        String lengthAfterDecimal = "";
//        for (int i = 0; i < lengthOfDecimalRange + 1; i++){
//            lengthAfterDecimal += "#";
//        }
////        Toast.makeText(this, "length " + lengthOfDecimalRange, Toast.LENGTH_SHORT).show();
//        DecimalFormat decimalFormat = new DecimalFormat("0." + lengthAfterDecimal);
//
//        return Double.parseDouble(decimalFormat.format(value));
    }


    private void calculateMinAndMaxScore() {
        minScore = achievements.calculateMinMaxScore(manager.get(indexOfGame).getMinPoorScoreFromConfig(), numPlayersInt);
        maxScore = achievements.calculateMinMaxScore(manager.get(indexOfGame).getMaxBestScoreFromConfig(), numPlayersInt);
    }
}
