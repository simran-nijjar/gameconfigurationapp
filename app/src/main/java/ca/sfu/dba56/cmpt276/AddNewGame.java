package ca.sfu.dba56.cmpt276;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class AddNewGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_game);
        chooseGame();
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, AddNewGame.class);
    }

    private void chooseGame() {
        Spinner dropdown = findViewById(R.id.gameName);
        String[] items = new String[]{"Poker", "Bingo", "Wordle New"};
        // create an adapter to describe how the items are displayed
        // basic variant
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        // set the spinners adapter to the dropdown menu
        dropdown.setAdapter(adapter);

    }

    private void saveInput(){
        EditText num_player = findViewById(R.id.num_players_input);
        EditText combined_score = findViewById(R.id.combined_score_input);

    }


}