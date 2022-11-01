package ca.sfu.dba56.cmpt276;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddNewGame extends AppCompatActivity {
    private int players_int;
    private int scores_int;
    String num_players_str = "";
    String combined_scores_str = "";
    private EditText num_player;
    private EditText combined_score;
    boolean isPlayerValid;
    boolean isScoresValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_game);
        chooseGame();
        checkInput();
        saveInput();
        //showResult();
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
                    if (players_int < 2) {
                        isPlayerValid = false;
                        Toast.makeText(AddNewGame.this, "Invalid input: 2 players minimum", Toast.LENGTH_SHORT).show();
                    }else {
                        isPlayerValid = true;
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
                    if (scores_int < players_int) {
                        isScoresValid = false;
                        Toast.makeText(AddNewGame.this, "Invalid input: 1 score minimum for each player", Toast.LENGTH_SHORT).show();
                    }else if(scores_int % players_int != 0){
                        isScoresValid = false;
                        Toast.makeText(AddNewGame.this, "Invalid input: scores must be an integer for each player", Toast.LENGTH_SHORT).show();
                    }else {
                        isScoresValid = true;
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
                    //save.setEnabled(true);
                    showResult();
                }else {
                    //save.setEnabled(false);
                    Toast.makeText(AddNewGame.this, "not good", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showResult(){
//        Button save = findViewById(R.id.save_btn);
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog alertDialog = new AlertDialog.Builder(AddNewGame.this).create(); //Read Update
//                alertDialog.setTitle("Achievement");
//                alertDialog.setMessage("achievement level");
//
//                alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        startActivity(new Intent(AddNewGame.this, MainActivity.class));
//                    }
//                });
//                alertDialog.show();
//            }
//        });

        AlertDialog alertDialog = new AlertDialog.Builder(AddNewGame.this).create(); //Read Update
        alertDialog.setTitle("Achievement");
        alertDialog.setMessage("achievement level");

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