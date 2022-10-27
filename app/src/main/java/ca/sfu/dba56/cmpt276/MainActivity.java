package ca.sfu.dba56.cmpt276;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import ca.sfu.dba56.cmpt276.model.ConfigurationsManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpHelpButton();
        getSupportActionBar().setTitle("Configurations");
        UpdateUI();

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
    }

}