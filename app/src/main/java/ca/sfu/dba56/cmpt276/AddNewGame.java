package ca.sfu.dba56.cmpt276;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

    private final String FRUITS = "Fruits";
    private final String FANTASY = "Fantasy";
    private final String STAR_WARS = "Star Wars";
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
    private Achievements addNewGameAchievements;
    private boolean isCalculatingRangeForLevels;
    private final int MAX_USER_INPUT = 100000000;
    private final int MIN_USER_INPUT = -100000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeTheme();
        addNewGameAchievements = new Achievements(getAchievementTheme(this));
        setContentView(R.layout.activity_add_new_game);
        chooseGame();
        storeSelectedGame();
        displayThemeRadioButtons();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, AddNewGame.class);
    }

    private void changeTheme(){
        if (AddNewGame.getAchievementTheme(this).equals(FRUITS)) {
            setTheme(R.style.fruitsTheme);
        }if (AddNewGame.getAchievementTheme(this).equals(FANTASY)){
            setTheme(R.style.fantasyTheme);
        } if (AddNewGame.getAchievementTheme(this).equals(STAR_WARS)){
            setTheme(R.style.starWarsTheme);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = ViewConfiguration.makeIntent(AddNewGame.this);
                startActivity(intent);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void chooseGame() {
        // get selected game name from ViewConfiguration
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString(getString(R.string.gameName));

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
        AlertDialog alertDialog = new AlertDialog.Builder(AddNewGame.this).create();
        alertDialog.setTitle(getString(R.string.too_many_players));
        alertDialog.setMessage(getString(R.string.Sorry_too_many_players));
        alertDialog.setButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
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
        AlertDialog alertDialog = new AlertDialog.Builder(AddNewGame.this).create();
        alertDialog.setTitle(getString(R.string.scoreTooHigh));
        alertDialog.setMessage(getString(R.string.scoreTooHighMsg));
        alertDialog.setButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
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
        AlertDialog alertDialog = new AlertDialog.Builder(AddNewGame.this).create();
        alertDialog.setTitle(getString(R.string.scoreTooLow));
        alertDialog.setMessage(getString(R.string.scoreTooLowMsg));
        alertDialog.setButton(getString(R.string.OK), (dialog, which) -> {
            //Stay on ViewAchievement activity
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
                        playerMsg.setText(R.string.PlayerMinimum1);
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
                    Toast.makeText(AddNewGame.this, R.string.emptyOrInvalid, Toast.LENGTH_SHORT).show();
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
                    if(scores >= MAX_USER_INPUT)  {
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
                        Toast.makeText(AddNewGame.this, R.string.emptyOrInvalid, Toast.LENGTH_SHORT).show();
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
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(getString(R.string.date_format));
        LocalDateTime currentDate = LocalDateTime.now();
        dateGamePlayed = currentDate.format(dateFormat);
        return dateGamePlayed;
    }

    // save input to the list
    private void saveInput(int selectedGameInt) {
        Button save = findViewById(R.id.save_btn);
        save.setOnClickListener(v -> {
            if (isPlayerValid && isScoresValid) {
                Game gamePlayed = new Game(numOfPlayers, scores, manager.getItemAtIndex(selectedGameInt),
                        saveDatePlayed(), isCalculatingRangeForLevels, ViewAchievements.getAchievementTheme(this));
                manager.getItemAtIndex(selectedGameInt).add(gamePlayed);
                showResult(gamePlayed.getLevelAchieved());
                Intent intent = ViewConfiguration.makeIntent(AddNewGame.this);
                startActivity(intent);
            }else {
                Toast.makeText(AddNewGame.this, R.string.emptyOrInvalid, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // pop up a window to show achievement
    private void showResult(String achievements){
        AlertDialog alertDialog = new AlertDialog.Builder(AddNewGame.this).create();
        alertDialog.setTitle(getString(R.string.achievement));
        alertDialog.setMessage("" + achievements);
        alertDialog.setButton(getString(R.string.OK), (dialog, which) -> {
            manager.setIndex(selectedGame);
            AddNewGame.this.finish(); // back to View Configuration page
        });
        alertDialog.show();
    }

    private void displayThemeRadioButtons(){
        RadioGroup themeGroup = findViewById(R.id.radioBtnsThemes);
        String[] themesArray = getResources().getStringArray(R.array.achievementThemes);

        for (int i = 0; i < themesArray.length; i++){
            final String achievementTheme = themesArray[i];

            RadioButton btn = new RadioButton(this);
            btn.setText(getString(R.string.display_string_option, achievementTheme));
            btn.setOnClickListener(v ->{
                saveAchievementTheme(achievementTheme);
                addNewGameAchievements.setAchievementTheme(achievementTheme);
                AddNewGame.this.recreate();
            });
            themeGroup.addView(btn);
            if (achievementTheme.equals(getAchievementTheme(this))){
                addNewGameAchievements.setAchievementTheme(achievementTheme);
                btn.setChecked(true);
            }
        }
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