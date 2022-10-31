package ca.sfu.dba56.cmpt276;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddNewGame extends AppCompatActivity {
    private int players;
    private int scores;

    private EditText num_player;
    private EditText combined_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_game);
        chooseGame();
        saveInput();
        showResult();
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, AddNewGame.class);
    }

    private void chooseGame() {
        Spinner dropdown = findViewById(R.id.gameName);
        // testing
        String[] items = new String[]{"Poker", "Bingo", "Wordle New"};
        // create an adapter to describe how the items are displayed
        // basic variant
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        // set the spinners adapter to the dropdown menu
        dropdown.setAdapter(adapter);

    }

    private void saveInput(){
        num_player = findViewById(R.id.num_players_input);
        combined_score = findViewById(R.id.combined_score_input);
        players = Integer.parseInt(String.valueOf(num_player));
        scores = Integer.parseInt(String.valueOf(combined_score));
    }

    private boolean isEmpty(String input){
        if(TextUtils.isEmpty(input)){
            Toast.makeText(this, "Text field is empty", Toast.LENGTH_SHORT).show();
            return false;
        }else return true;
    }

    private boolean isValid(){
        if(isEmpty(String.valueOf(num_player))){
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            return false;
        }else if(isEmpty(String.valueOf(combined_score))){
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }

    private void showResult(){
        Button save = findViewById(R.id.save_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

    }


}