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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
        createDifficultyRadioButtons();
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


        resetMinAndMaxScoreFromConfig();
        switch(achievements.getDifficultyLevel()){
            case 0:
                minScore *= 0.75;
                maxScore *= 0.75;
                break;
            case 1:
                break;
            case 2:
                minScore *= 1.25;
                maxScore *= 1.25;
                break;
        }
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
        achievementLevels += getString(R.string.worst_game_level_range_boundary) //Add worse range than expected lowest range achievement level
                + minScore + "\n\n";
        for (int i = 1; i < achievements.getNumOfBoundedLevels() + 1; i++) {
            if (newStartRange + 1 < Math.abs(maxScore)) {
                achievementLevels += achievements.getAchievementLevel(i);
                achievementLevels += " Range: [";
                if (i == 1) {
                    achievementLevels += minScore;
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
            achievementLevels += getString(R.string.legendary_level_range_boundary) //Add best level range when not all levels can be calculated for
                    + (newStartRange + 1);
        } else {
            achievementLevels += getString(R.string.legendary_level_range_boundary) //Add best level range when all levels can be calculated for
                    + maxScore;
        }
        return achievementLevels;
    }

    @NonNull
    private String displayAchievementScores(String achievementLevels) {
        achievementLevels += getString(R.string.worst_game_level_score_boundary) //Add worse than expected lowest score achievement level
                + minScore +"\n\n";
        for (int i = 1; i < (maxScore - minScore + 1); i++){
            achievementLevels += achievements.getAchievementLevel(i);
            achievementLevels += getString(R.string.score_part1);
            achievementLevels += (minScore + i - 1);
            achievementLevels += getString(R.string.score_part2);
        }
        achievementLevels += getString(R.string.legendary_level_score_boundary) //Add best score level for scores
                + maxScore;
        return achievementLevels;
    }

    private void resetMinAndMaxScoreFromConfig() {
        minScore = achievements.calculateMinMaxScore(manager.getItemAtIndex(indexOfGame).getMinPoorScoreFromConfig(), numPlayers);
        maxScore = achievements.calculateMinMaxScore(manager.getItemAtIndex(indexOfGame).getMaxBestScoreFromConfig(), numPlayers);
    }

    // create radio buttons for the achievement view that will change the levels of achievement
    // according to selected difficulty level
    private void createDifficultyRadioButtons() {
        RadioGroup difficultiesGroup = findViewById(R.id.radioGroupDifficulty);

        RadioButton easyDifBtn = findViewById(R.id.radioBtnDifEasy);
        RadioButton normalDifBtn = findViewById(R.id.radioBtnDifNormal);
        RadioButton hardDifBtn = findViewById(R.id.radioBtnDifHard);

        difficultyButtonClicked(easyDifBtn);
        difficultyButtonClicked(normalDifBtn);
        difficultyButtonClicked(hardDifBtn);
    }

    private void difficultyButtonClicked(RadioButton diffBtn) {
        diffBtn.setOnClickListener(view -> {
            switch (diffBtn.getText().toString()) {
                case "Easy":
                    Toast.makeText(ViewAchievements.this, "you selected Difficulty Easy", Toast.LENGTH_SHORT).show();
                    achievements.setDifficultyLevel(0);
                    if(!numPlayersFromUser.getText().toString().isEmpty()){displayAchievementLevels();}
                    break;
                case "Normal":
                    Toast.makeText(ViewAchievements.this, "you selected Difficulty Normal", Toast.LENGTH_SHORT).show();
                    achievements.setDifficultyLevel(1);
                    if(!numPlayersFromUser.getText().toString().isEmpty()){displayAchievementLevels();}

                    break;
                case "Hard":
                    Toast.makeText(ViewAchievements.this, "you selected Difficulty Hard", Toast.LENGTH_SHORT).show();
                    achievements.setDifficultyLevel(2);
                    if(!numPlayersFromUser.getText().toString().isEmpty()){displayAchievementLevels();}
                    break;

            }

        });
    }
}
