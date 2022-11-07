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
        list.setSelector(android.R.color.transparent);
        list.setAdapter(adapter);
    }


}