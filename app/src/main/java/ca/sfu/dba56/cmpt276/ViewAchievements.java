package ca.sfu.dba56.cmpt276;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_achievements);
        noAchievementsDisplayed = findViewById(R.id.emptyAchievements);
        numPlayers = findViewById(R.id.userInputPlayers);
        numPlayers.addTextChangedListener(textWatcher);
        Bundle b = getIntent().getExtras();
        indexOfGame = b.getInt("Achievement Game Name");
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, ViewAchievements.class);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            noAchievementsDisplayed.setText("Enter number of players to see achievement levels with minimum required score\n");
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            numPlayersStr = numPlayers.getText().toString();
            if(!numPlayersStr.isEmpty()){
                noAchievementsDisplayed.setText("");
                numPlayersInt = Integer.parseInt(numPlayersStr);
                displayAchievementLevels();
            }
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void displayAchievementLevels(){
        String achievementLevels = " ";
        numPlayers = findViewById(R.id.userInputPlayers);
        numPlayersStr = numPlayers.getText().toString();
        numPlayersInt = Integer.parseInt(numPlayersStr);

        int range = 0;
        int minScore = achievements.calculateMinMaxScore(manager.get(indexOfGame).getMinPoorScoreFromConfig(), numPlayersInt);
        int maxScore = achievements.calculateMinMaxScore(manager.get(indexOfGame).getMaxBestScoreFromConfig(), numPlayersInt);
        range = achievements.calculateLevelRange(minScore, maxScore);
        Toast.makeText(this, "min " + minScore + " max " + maxScore + " players " + numPlayersInt + " range " + range, Toast.LENGTH_SHORT).show();
        int newStartRange = 0;
        achievementLevels += "Worst Game Level: Range < " + minScore + "\n";
        for (int i = 1; i < achievements.getIndex() + 1; i++){
            achievementLevels += achievements.getAchievementLevel(i);
            achievementLevels += " Range: [";
            if (i == 1) {
                achievementLevels += achievements.calculateMinMaxScore(manager.get(indexOfGame).getMinPoorScoreFromConfig(), numPlayersInt);
                achievementLevels += ", " + (minScore + range) + "]\n";
                newStartRange = (minScore + range);
            }
            else if (i == achievements.getIndex()){
                achievementLevels += " " + (newStartRange + 1) + ", " + (maxScore) + "]\n";
            }
            else{
                achievementLevels += " " + (newStartRange + 1) + ", " + (newStartRange + 1 + range) + "]\n";
                newStartRange += 1 + range;
            }
        }
        achievementLevels += "\n";
        displayAchievements = findViewById(R.id.listOfAchievements);
        displayAchievements.setText(achievementLevels);
    }
}
