package ca.sfu.dba56.cmpt276;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import ca.sfu.dba56.cmpt276.model.ConfigurationsManager;

/*
* activity class GameHistory
* populates and shows a none clickable list view of all the games played
 */

public class GameHistory extends AppCompatActivity {

    private ConfigurationsManager manager = ConfigurationsManager.getInstance();
    private int indexOfGame = 0;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history);
        // get selected game name from ViewConfiguration
        Bundle b = getIntent().getExtras();
        indexOfGame = b.getInt(getString(R.string.game_name_2));
        populateListView(manager, indexOfGame);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, GameHistory.class);
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    //populates a list view with all the games played before in the given config
    private void populateListView(ConfigurationsManager manager, int indexOfGame) {
        // creating list of games items
        ArrayList<String> items = new ArrayList<String>();
        //array of games
        int count = 0;
        while(count < manager.getItemAtIndex(indexOfGame).getSizeOfListOfConfigs()){
            String strResult = "\n" + manager.getItemAtIndex(indexOfGame).get(count) + "\n";
            items.add(strResult);
            count++;
        }
        //adapter
        adapter = new ArrayAdapter<String>(this, R.layout.game_items, items);
        ListView list = findViewById(R.id.HistoryList);
        list.setSelector(android.R.color.transparent);
        list.setAdapter(adapter);
    }


}