package ca.sfu.dba56.cmpt276;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ca.sfu.dba56.cmpt276.model.Configuration;
import ca.sfu.dba56.cmpt276.model.ConfigurationsManager;

public class ViewConfiguration extends AppCompatActivity {

    private TextView expPoorScoreEditTxt;
    private TextView expGreatScoreEditTxt;
    private Button editConfigScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_configuration);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        //check what position of configuration was selected
        Bundle b = getIntent().getExtras();
        int currentConfigPosition = b.getInt(getString(R.string.selected_config_position));
        ConfigurationsManager manager = ConfigurationsManager.getInstance();
        Configuration currentConfig = manager.get(currentConfigPosition);
        //Activity Name
        getSupportActionBar().setTitle("Game " + currentConfig.getGameNameFromConfig() + " Configuration");
        //find locations
        expPoorScoreEditTxt = findViewById(R.id.textFillPoorScoreConfigView);
        expGreatScoreEditTxt = findViewById(R.id.textFillGreatScoreConfigView);
        //populate them
        expPoorScoreEditTxt.setText(String.valueOf(currentConfig.getMinPoorScoreFromConfig()));
        expGreatScoreEditTxt.setText(String.valueOf(currentConfig.getMinPoorScoreFromConfig()));

        //make edit button open edit configuration screen.
        editConfigScreen = findViewById(R.id.btnEditConfig);
        editConfigScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewConfiguration.this,EditConfiguration.class);
                startActivity(intent);
            }
        });
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, ViewConfiguration.class);
    }


}