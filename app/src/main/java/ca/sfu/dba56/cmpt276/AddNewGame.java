package ca.sfu.dba56.cmpt276;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import ca.sfu.dba56.cmpt276.model.Achievements;
import ca.sfu.dba56.cmpt276.model.ConfigurationsManager;
import ca.sfu.dba56.cmpt276.model.Game;

/*
* add new game activity class adds new game to the list of games in configuration
* checks user input and gives warning in case input is bot valid
 */
public class AddNewGame extends AppCompatActivity {

    private int numOfPlayers; // int user input
    private int scores; // int user input
    private String dateGamePlayed; // date time
    private String numOfPlayersAsStr = "";  // String user input
    private String combinedScoresAsStr = ""; // String user input
    private EditText numOfPlayerFromUser;
    private EditText combinedScoreFromUser;
    private boolean isPlayerValid; // check if user input is valid
    private boolean isScoresValid; // check if user input is valid
    private TextView playerMsg; // alert message
    private TextView scoreMsg; // alert message
    private ConfigurationsManager manager = ConfigurationsManager.getInstance();
    private int selectedGame; // user selected game config index
    private int adjustedMax;
    private int adjustedMin;
    private Achievements addNewGameAchievements = new Achievements();
    private boolean isCalculatingRangeForLevels;
    private final int MAX_USER_INPUT = 100000000;
    private final int MIN_USER_INPUT = -100000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_game);
        chooseGame();
        storeSelectedGame();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, AddNewGame.class);
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

    private void chooseGame() {
        // get selected game name from ViewConfiguration
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("game name");

        // drop down menu for games
        Spinner dropdown = findViewById(R.id.gameName);

        // list of items
        ArrayList<String> items = new ArrayList<>();
        int count = 0;
        int defaultGameIndex = 0;
        while(count < manager.configListSize()){
            String strResult = manager.getItemAtIndex(count).getGameNameFromConfig();
            items.add(strResult);
            if(Objects.equals(items.get(count), name)){
                defaultGameIndex = count;
            }
            count++;
        }
        // create an adapter to describe how the items are displayed
        // basic variant
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        // set the spinners adapter to the dropdown menu
        dropdown.setAdapter(adapter);
        dropdown.setSelection(defaultGameIndex); // set default game in drop down menu
    }

    private void storeSelectedGame(){
        Spinner dropdown = findViewById(R.id.gameName);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                selectedGame = dropdown.getSelectedItemPosition();

                // set text again when the user changes selection
                numOfPlayerFromUser = findViewById(R.id.num_players_input);
                combinedScoreFromUser = findViewById(R.id.combined_score_input);
                playerMsg = findViewById(R.id.player_msg);
                scoreMsg = findViewById(R.id.score_msg);
                numOfPlayerFromUser.setText("");
                combinedScoreFromUser.setText("");
                playerMsg.setText("");
                scoreMsg.setText("");
                // call function according to current selection
                checkInput(selectedGame);
                saveInput(selectedGame);
            }
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
    }

    private void displayMaxPlayerMsg(){
        AlertDialog alertDialog = new AlertDialog.Builder(AddNewGame.this).create(); //Read Update
        alertDialog.setTitle("Too many players");
        alertDialog.setMessage("Sorry, that's too many players. Please try a smaller number");
        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Stay on ViewAchievement activity
            }
        });
        alertDialog.show();
        //set num of player to the minimum default 1
        numOfPlayerFromUser = findViewById(R.id.num_players_input);
        numOfPlayerFromUser.setText("1");
    }

    private void displayMaxCombinedScoreMsg(){
        AlertDialog alertDialog = new AlertDialog.Builder(AddNewGame.this).create(); //Read Update
        alertDialog.setTitle("Combined Score Too High");
        alertDialog.setMessage("Sorry, the combined score is too high. Please try a smaller number");
        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Stay on ViewAchievement activity
            }
        });
        alertDialog.show();
        //set num of player to the minimum
        combinedScoreFromUser = findViewById(R.id.combined_score_input);
        combinedScoreFromUser.setText("");
    }

    private void displayMinCombinedScoreMsg(){
        AlertDialog alertDialog = new AlertDialog.Builder(AddNewGame.this).create(); //Read Update
        alertDialog.setTitle("Combined Score Too Low");
        alertDialog.setMessage("Sorry, the combined score is too low. Please try a bigger number");
        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Stay on ViewAchievement activity
            }
        });
        alertDialog.show();
        //set num of player to the minimum
        combinedScoreFromUser = findViewById(R.id.combined_score_input);
        combinedScoreFromUser.setText("");
    }

    // check if user input is valid
    private void checkInput(int selectedGameInt){
        numOfPlayerFromUser = findViewById(R.id.num_players_input);
        combinedScoreFromUser = findViewById(R.id.combined_score_input);
        playerMsg = findViewById(R.id.player_msg); // alert message
        playerMsg.setTextColor(getResources().getColor(R.color.purple_700));
        scoreMsg = findViewById(R.id.score_msg); // alert message
        scoreMsg.setTextColor(getResources().getColor(R.color.purple_700));

        numOfPlayerFromUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                numOfPlayersAsStr = numOfPlayerFromUser.getText().toString();
                try{
                    numOfPlayers = Integer.parseInt(numOfPlayersAsStr);
                    if (numOfPlayers < 1) {
                        isPlayerValid = false;
                        playerMsg.setText("Invalid input: 1 player minimum");
                    }else if (numOfPlayers >= MAX_USER_INPUT) {
                        isPlayerValid = false;
                        displayMaxPlayerMsg();
                    }else{
                        isPlayerValid = true;
                        playerMsg.setText("");
                        adjustedMax = addNewGameAchievements.calculateMinMaxScore(manager.getItemAtIndex(selectedGameInt).getMaxBestScoreFromConfig(), numOfPlayers);
                        adjustedMin = addNewGameAchievements.calculateMinMaxScore(manager.getItemAtIndex(selectedGameInt).getMinPoorScoreFromConfig(), numOfPlayers);
                        if (Math.abs(adjustedMax - adjustedMin) > 8) {
                            isCalculatingRangeForLevels = true;
                        } else {
                            isCalculatingRangeForLevels = false;
                        }
                    }
                }catch (NumberFormatException ex){
                    isPlayerValid = false;
                    Toast.makeText(AddNewGame.this, "Your input is empty or invalid", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        combinedScoreFromUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                combinedScoresAsStr = combinedScoreFromUser.getText().toString();
                try {
                    scores = Integer.parseInt(combinedScoresAsStr);
                    if (scores < 0 && adjustedMin > 0){
                        isScoresValid = false;
                        scoreMsg.setText(R.string.negCombinedScoresMsg);
                    }else if(scores >= MAX_USER_INPUT)  {
                        isScoresValid = false;
                        displayMaxCombinedScoreMsg();
                    }else if(scores <= MIN_USER_INPUT)  {
                        isScoresValid = false;
                        displayMinCombinedScoreMsg();
                    }else {
                        isScoresValid = true;
                        scoreMsg.setText("");
                    }
                }catch (NumberFormatException ex){
                    isScoresValid = false;
                    if(combinedScoreFromUser.length() == 0) {
                        Toast.makeText(AddNewGame.this, "Your input is empty or invalid", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // get date time
    public String saveDatePlayed(){
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMM dd, y @ h:mma");
        LocalDateTime currentDate = LocalDateTime.now();
        dateGamePlayed = currentDate.format(dateFormat);
        return dateGamePlayed;
    }

    // save input to the list
    private void saveInput(int selectedGameInt) {
        Button save = findViewById(R.id.save_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlayerValid && isScoresValid) {
                    Game gamePlayed = new Game(numOfPlayers, scores, manager.getItemAtIndex(selectedGameInt), saveDatePlayed(), isCalculatingRangeForLevels);
                    manager.getItemAtIndex(selectedGameInt).add(gamePlayed);
                    showResult(gamePlayed.getLevelAchieved());
                }else {
                    Toast.makeText(AddNewGame.this, "Your input is empty or invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // pop up a window to show achievement
    private void showResult(String achievements){
        AlertDialog alertDialog = new AlertDialog.Builder(AddNewGame.this).create(); //Read Update
        alertDialog.setTitle(getString(R.string.achievement));
        alertDialog.setMessage("" + achievements);
        alertDialog.setButton("Ok", (dialog, which) -> {
            manager.setIndex(selectedGame);
            AddNewGame.this.finish(); // back to View Configuration page
        });
        alertDialog.show();
    }

}