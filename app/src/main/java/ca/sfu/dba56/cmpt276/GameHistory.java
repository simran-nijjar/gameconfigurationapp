package ca.sfu.dba56.cmpt276;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import ca.sfu.dba56.cmpt276.model.Configuration;
import ca.sfu.dba56.cmpt276.model.ConfigurationsManager;

public class GameHistory extends AppCompatActivity {
    ConfigurationsManager manager = ConfigurationsManager.getInstance();
    int indexOfGame = 0;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history);
        // get selected game name from ViewConfiguration
        Bundle b = getIntent().getExtras();
        indexOfGame = b.getInt("game name2");
        UpdateUI(indexOfGame);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, GameHistory.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateUI(indexOfGame);
    }

    private void UpdateUI(int indexOfGame) {
        TextView txt = findViewById(R.id.GameHistoryEmpty);
        if(manager.get(indexOfGame).size() == 0){
            txt.setText("No Game History yet.\n \n You can add one by pressing 'Add Game' button on the previous page" );
        }
        else {
            txt.setText("");
            //populate list view
            populateListView(manager, indexOfGame);
        }
    }

    private void populateListView(ConfigurationsManager manager, int indexOfGame) {
        // creating list of games items
        ArrayList<String> items = new ArrayList<String>();
        //array of games
        int count = 0;
        while(count < manager.get(indexOfGame).size()){
            String strResult = "\n" + manager.get(indexOfGame).get(count) + "\n";
            items.add(strResult);
            count++;
        }
        //adapter
        adapter = new ArrayAdapter<String>(this, R.layout.game_items, items);
        ListView list = findViewById(R.id.HistoryList);
        list.setAdapter(adapter);
    }


}