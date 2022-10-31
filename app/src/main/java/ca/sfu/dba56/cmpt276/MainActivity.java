package ca.sfu.dba56.cmpt276;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import ca.sfu.dba56.cmpt276.model.ConfigurationsManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpHelpButton();
        getSupportActionBar().setTitle("Configurations");
        UpdateUI();

        setUpAddConfigurationButton();
    }

    private void setUpHelpButton() {
        Button helpBtn = (Button)findViewById(R.id.helpBtn);
        helpBtn.setOnClickListener(v -> {
            Intent intent = HelpActivity.makeIntent(MainActivity.this);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateUI();
    }

    private void UpdateUI() {
        ConfigurationsManager manager = ConfigurationsManager.getInstance();
        TextView txt = findViewById(R.id.TextConfiguresEmpty);
        if(manager.isEmpty()){
            txt.setText("No Configurations yet.\n \n You can add one by pressing 'plus' button in the bottom right corner" );
        }
        else {
            //get rid of text
            txt.setText("");
            //populate list view with games from manager
            populateListView(manager);
        }
    }

    private void populateListView(ConfigurationsManager manager) {
        // creating list of config items
        ArrayList<String> items = new ArrayList<String>();
        //array of string config names
        int count = 0;
        while(count < manager.configListSize()){
            String strResult = "\n" + manager.get(count).getGameNameFromConfig() + "\n";
            items.add(strResult);
            count++;
        }
        //adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.text_view_config_items,
                items);
        ListView list = findViewById(R.id.configList);
        list.setAdapter(adapter);
    }


    private void setUpAddConfigurationButton(){
        FloatingActionButton addConfigBtn = findViewById(R.id.addBtn);
        addConfigBtn.setOnClickListener(v ->{
            Intent intent = AddConfiguration.makeIntent(MainActivity.this);
            startActivity(intent);
        });
    }

}