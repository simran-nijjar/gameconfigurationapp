package ca.sfu.dba56.cmpt276;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ca.sfu.dba56.cmpt276.model.Configuration;
import ca.sfu.dba56.cmpt276.model.ConfigurationsManager;

public class ViewConfiguration extends AppCompatActivity {

    private TextView expPoorScoreEditTxt;
    private TextView expGreatScoreEditTxt;
    int currentConfigPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_configuration);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        //check what position of configuration was selected
        Bundle b = getIntent().getExtras();
        currentConfigPosition = b.getInt(getString(R.string.selected_config_position));
        ConfigurationsManager manager = ConfigurationsManager.getInstance();
        Configuration currentConfig = manager.get(currentConfigPosition);
        //Activity Name
        getSupportActionBar().setTitle("Game " + currentConfig.getGameNameFromConfig() + " Configuration");
        //find locations
        expPoorScoreEditTxt = findViewById(R.id.textFillPoorScoreConfigView);
        expGreatScoreEditTxt = findViewById(R.id.textFillGreatScoreConfigView);
        //populate them
        expPoorScoreEditTxt.setText(String.valueOf(currentConfig.getMinPoorScoreFromConfig()));
        expGreatScoreEditTxt.setText(String.valueOf(currentConfig.getMaxBestScoreFromConfig()));

        setUpGameHistoryButton();
        //setUpAddGameButton();
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, ViewConfiguration.class);
    }


    private void setUpGameHistoryButton(){
        Button addBtn = findViewById(R.id.btnHistoryConfig);
        addBtn.setOnClickListener(v -> {
            Intent intent2 = GameHistory.makeIntent(ViewConfiguration.this);
            intent2.putExtra("game name2", currentConfigPosition);
            startActivity(intent2);
        });
    }
}