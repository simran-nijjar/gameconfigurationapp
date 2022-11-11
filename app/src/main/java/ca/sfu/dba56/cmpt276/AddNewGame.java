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
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
    private int combinedScores = 0;
    private String dateGamePlayed; // date time
    private String numOfPlayersAsStr = "";  // String user input
    private String scoresAsStr = ""; // String user input
    private EditText numOfPlayerFromUser;
    //private EditText combinedScoreFromUser;
    private boolean isPlayerValid; // check if user input is valid
    private boolean isScoresValid; // check if user input is valid
    private TextView playerMsg; // alert message
    //private TextView scoreMsg; // alert message
    private ConfigurationsManager manager = ConfigurationsManager.getInstance();
    private int selectedGame; // user selected game config index
    private int adjustedMax;
    private int adjustedMin;
    private Achievements addNewGameAchievements = new Achievements();
    private boolean isCalculatingRangeForLevels;
    private final int MAX_USER_INPUT = 100000000;
    private final int MIN_USER_INPUT = -100000000;
    private int indexOfPlayer = 0;
    private int indexOfScore = 0;
    private EditText[] edList;
    List<Integer> score_list;

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
                //combinedScoreFromUser = findViewById(R.id.combined_score_input);
                playerMsg = findViewById(R.id.player_msg);
                //scoreMsg = findViewById(R.id.score_msg);
                numOfPlayerFromUser.setText("");
                //combinedScoreFromUser.setText("");
                playerMsg.setText("");
                //scoreMsg.setText("");
                LinearLayout ll_left = (LinearLayout)findViewById(R.id.left_layout_tv);
                ll_left.removeAllViewsInLayout();
                LinearLayout ll_right = (LinearLayout)findViewById(R.id.right_layout_et);
                ll_right.removeAllViewsInLayout();
                // call function according to current selection
                checkInput(selectedGame);
                setSetBtn();
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
//        combinedScoreFromUser = findViewById(R.id.combined_score_input);
//        combinedScoreFromUser.setText("");
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
//        combinedScoreFromUser = findViewById(R.id.combined_score_input);
//        combinedScoreFromUser.setText("");
    }

    // check if user input is valid
    private void checkInput(int selectedGameInt){
        numOfPlayerFromUser = findViewById(R.id.num_players_input);
        //combinedScoreFromUser = findViewById(R.id.combined_score_input);
        playerMsg = findViewById(R.id.player_msg); // alert message
        playerMsg.setTextColor(getResources().getColor(R.color.purple_700));
        //scoreMsg = findViewById(R.id.score_msg); // alert message
        //scoreMsg.setTextColor(getResources().getColor(R.color.purple_700));

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

//        combinedScoreFromUser.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                combinedScoresAsStr = combinedScoreFromUser.getText().toString();
//                try {
//                    scores = Integer.parseInt(combinedScoresAsStr);
//                    if(scores >= MAX_USER_INPUT)  {
//                        isScoresValid = false;
//                        displayMaxCombinedScoreMsg();
//                    }else if(scores <= MIN_USER_INPUT)  {
//                        isScoresValid = false;
//                        displayMinCombinedScoreMsg();
//                    }else {
//                        isScoresValid = true;
//                        //scoreMsg.setText("");
//                    }
//                }catch (NumberFormatException ex){
//                    isScoresValid = false;
//                    if(combinedScoreFromUser.length() == 0) {
//                        Toast.makeText(AddNewGame.this, R.string.emptyOrInvalid, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
    }

    private void createLeftFields(){
        LinearLayout ll_left = (LinearLayout)findViewById(R.id.left_layout_tv);
        // add edittext
        TextView tv = new TextView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50,62,0,0);
        tv.setLayoutParams(lp);
        indexOfPlayer++;
        tv.setText("Player " + indexOfPlayer + ":");
        tv.setTextColor(getColor(R.color.white));
        //tv.setId(indexOfPlayer + 10);
        //tv.setTag("Player" + indexOfPlayer);
        ll_left.addView(tv);
    }

    private EditText createRightFields(){
        LinearLayout ll_right = (LinearLayout)findViewById(R.id.right_layout_et);
        // add edittext
        EditText et = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        et.setLayoutParams(lp);
        et.setText("");
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setTextSize(17);
        et.setTextColor(getColor(R.color.white));
        et.setId(indexOfScore + 1);
        ll_right.addView(et);
        indexOfScore++;
        return et;
    }

    private void setSetBtn(){
        Button setBtn = findViewById(R.id.set_btn);
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlayerValid && edList == null){
                   createFields();
                }else if(isPlayerValid){
                    LinearLayout ll_left = (LinearLayout)findViewById(R.id.left_layout_tv);
                    ll_left.removeAllViewsInLayout();
                    LinearLayout ll_right = (LinearLayout)findViewById(R.id.right_layout_et);
                    ll_right.removeAllViewsInLayout();
                    createFields();
                }
            }
        });
    }

    private void createFields(){
        edList = new EditText[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i++) {
            createLeftFields();
            edList[i] = createRightFields();
        }

        indexOfPlayer = 0;
        indexOfScore = 0;

        textWatcher tw = new textWatcher(edList);
        for (EditText editText : edList) editText.addTextChangedListener(tw);
    }


    public class textWatcher implements TextWatcher {

        EditText[] edList;

        public textWatcher(EditText[] edList) {
            this.edList = edList;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
//            for (EditText editText : edList) {
//                if(editText.getText().toString().length() < 1){
//                    final View focusView = editText;
//                    editText.setError(getString(R.string.emptyOrInvalid));
//                    focusView.requestFocus();
//                    isScoresValid = false;
//                    //break;
//                }
//            }

            isScoresValid = true;
            for (EditText editText : edList) {
                scoresAsStr = editText.getText().toString();
                try {
                    scores = Integer.parseInt(scoresAsStr);
                    if(scores >= MAX_USER_INPUT)  {
                        isScoresValid = false;
                        displayMaxCombinedScoreMsg();
                        editText.setText("");
                    }else if(scores <= MIN_USER_INPUT)  {
                        isScoresValid = false;
                        displayMinCombinedScoreMsg();
                        editText.setText("");
                    }
                }catch (NumberFormatException ex){
                    isScoresValid = false;
                    if(scoresAsStr.length() == 0) {
                        editText.setError("Must enter value");
                        //Toast.makeText(AddNewGame.this, R.string.emptyOrInvalid, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void storeScores(){
        score_list = new ArrayList<>();
        int score;
        for (EditText editText : edList) {
            score = Integer.parseInt(editText.getText().toString());
            score_list.add(score);
            combinedScores += score;
        }
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
                storeScores();
                Game gamePlayed = new Game(numOfPlayers, combinedScores, score_list, manager.getItemAtIndex(selectedGameInt), saveDatePlayed(), isCalculatingRangeForLevels);
                manager.getItemAtIndex(selectedGameInt).add(gamePlayed);
                showResult(gamePlayed.getLevelAchieved());
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

}