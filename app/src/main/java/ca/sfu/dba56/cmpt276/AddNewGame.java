package ca.sfu.dba56.cmpt276;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.Arrays;
import java.util.Objects;

import ca.sfu.dba56.cmpt276.model.Achievements;
import ca.sfu.dba56.cmpt276.model.ConfigurationsManager;
import ca.sfu.dba56.cmpt276.model.Game;

public class AddNewGame extends AppCompatActivity {
    private int players_int; // int user input
    private int scores_int; // int user input
    private String dateGamePlayed; // date time
    private String num_players_str = "";  // String user input
    private String combined_scores_str = ""; // String user input
    private EditText num_player;
    private EditText combined_score;
    boolean isPlayerValid; // check if user input is valid
    boolean isScoresValid; // check if user input is valid
    private TextView player_msg; // alert message
    private TextView score_msg; // alert message
    private ConfigurationsManager manager = ConfigurationsManager.getInstance();
    private int selectedGameInt;
    private int indexOfGame;
    private int adjustedMax;
    private int adjustedMin;
    private Achievements addNewGameAchievements = new Achievements();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_game);
        chooseGame();
        storeSelectedGame();
        Bundle b = getIntent().getExtras();
        indexOfGame = b.getInt("game name");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, AddNewGame.class);
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
            String strResult = manager.get(count).getGameNameFromConfig();
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
                selectedGameInt = dropdown.getSelectedItemPosition();

                // set text again when the user changes selection
                num_player = findViewById(R.id.num_players_input);
                combined_score = findViewById(R.id.combined_score_input);
                player_msg = findViewById(R.id.player_msg);
                score_msg = findViewById(R.id.score_msg);
                num_player.setText("");
                combined_score.setText("");
                player_msg.setText("");
                score_msg.setText("");
                // call function according to current selection
                checkInput(selectedGameInt);
                saveInput(selectedGameInt);
            }
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
    }

    // check if user input is valid
    private void checkInput(int selectedGameInt){
        num_player = findViewById(R.id.num_players_input);
        combined_score = findViewById(R.id.combined_score_input);
        player_msg = findViewById(R.id.player_msg); // alert message
        player_msg.setTextColor(getResources().getColor(R.color.purple_700));
        score_msg = findViewById(R.id.score_msg); // alert message
        score_msg.setTextColor(getResources().getColor(R.color.purple_700));

        num_player.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                num_players_str = num_player.getText().toString();
                try {
                    players_int = Integer.parseInt(num_players_str);
                    if (players_int < 1) {
                        isPlayerValid = false;
                        player_msg.setText("Invalid input: 1 player minimum");
                    }else {
                        isPlayerValid = true;
                        player_msg.setText("");
                        adjustedMax = addNewGameAchievements.calculateMinMaxScore(manager.get(selectedGameInt).getMaxBestScoreFromConfig(), players_int);
                        adjustedMin = addNewGameAchievements.calculateMinMaxScore(manager.get(selectedGameInt).getMinPoorScoreFromConfig(), players_int);
                    }
                }catch (NumberFormatException ex){
                    Toast.makeText(AddNewGame.this, "Text field is empty", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        combined_score.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                combined_scores_str = combined_score.getText().toString();
                try {
                    scores_int = Integer.parseInt(combined_scores_str);
                        if (scores_int < 0 && adjustedMin > 0){
                            isScoresValid = false;
                            score_msg.setText(R.string.negCombinedScoresMsg);
                        }
                        else if(scores_int > adjustedMax){
                        isScoresValid = false;
                        score_msg.setText(R.string.greaterCombinedScoreMsg);
                    }
                    else {
                        isScoresValid = true;
                        score_msg.setText("");
                    }
                }catch (NumberFormatException ex){
                    Toast.makeText(AddNewGame.this, "Text field is empty", Toast.LENGTH_SHORT).show();
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
                    Game gamePlayed = new Game(players_int, scores_int, manager.get(selectedGameInt), saveDatePlayed());
                    manager.get(selectedGameInt).add(gamePlayed);
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
        alertDialog.setTitle("Achievement");
        alertDialog.setMessage("" + achievements);
        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                AddNewGame.this.finish(); // back to View Configuration page
            }
        });
        alertDialog.show();
    }

    protected void onResume(){
        super.onResume();
    }

}