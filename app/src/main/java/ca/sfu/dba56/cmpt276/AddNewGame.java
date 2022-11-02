package ca.sfu.dba56.cmpt276;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddNewGame extends AppCompatActivity {
    private int players_int;
    private int scores_int;
    private String num_players_str = "";
    private String combined_scores_str = "";
    private EditText num_player;
    private EditText combined_score;
    boolean isPlayerValid;
    boolean isScoresValid;
    private TextView player_msg;
    private TextView score_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_game);
        chooseGame();
        checkInput();
        saveInput();
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, AddNewGame.class);
    }

    private void chooseGame() {
        Spinner dropdown = findViewById(R.id.gameName);
        String[] items = new String[]{"Poker", "Bingo", "Wordle New"};  // just for testing
        // create an adapter to describe how the items are displayed
        // basic variant
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        // set the spinners adapter to the dropdown menu
        dropdown.setAdapter(adapter);

    }

    private void checkInput(){
        num_player = findViewById(R.id.num_players_input);
        combined_score = findViewById(R.id.combined_score_input);
        player_msg = findViewById(R.id.player_msg);
        player_msg.setTextColor(getResources().getColor(R.color.purple_700));
        score_msg = findViewById(R.id.score_msg);
        score_msg.setTextColor(getResources().getColor(R.color.purple_700));

        num_player.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                num_players_str = num_player.getText().toString();
                try {
                    players_int = Integer.parseInt(num_players_str);
                    if (players_int < 1) {
                        isPlayerValid = false;
                        player_msg.setText("Invalid input: 1 players minimum");
                        //Toast.makeText(AddNewGame.this, "Invalid input: 1 players minimum", Toast.LENGTH_SHORT).show();
                    }else {
                        isPlayerValid = true;
                        player_msg.setText("");
                    }
                }catch (NumberFormatException ex){
                    //isScoresValid = false;
                    Toast.makeText(AddNewGame.this, "Text field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        combined_score.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                combined_scores_str = combined_score.getText().toString();
                try {
                    scores_int = Integer.parseInt(combined_scores_str);
                    if (scores_int < players_int || players_int == 0) {
                        isScoresValid = false;
                        score_msg.setText("Invalid input: 1 score minimum for each player");
                        //Toast.makeText(AddNewGame.this, "Invalid input: 1 score minimum for each player", Toast.LENGTH_SHORT).show();
                    }else if(scores_int % players_int != 0){
                        isScoresValid = false;
                        score_msg.setText("Invalid input: score must be an integer for each player");
                        //Toast.makeText(AddNewGame.this, "Invalid input: scores must be an integer for each player", Toast.LENGTH_SHORT).show();
                    }else {
                        isScoresValid = true;
                        score_msg.setText("");
                    }
                }catch (NumberFormatException ex){
                    //isScoresValid = false;
                    Toast.makeText(AddNewGame.this, "Text field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveInput() {
        Button save = findViewById(R.id.save_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlayerValid && isScoresValid) {
                    // add user input to game history


                    showResult();
                }else {

                    Toast.makeText(AddNewGame.this, "Your input is empty or invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showResult(){
        AlertDialog alertDialog = new AlertDialog.Builder(AddNewGame.this).create(); //Read Update
        alertDialog.setTitle("Achievement");
        alertDialog.setMessage("achievement level"); // just for testing, should show achievement level instead
        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(AddNewGame.this, MainActivity.class));
            }
        });
        alertDialog.show();
    }

    protected void onResume(){
        super.onResume();
    }

}