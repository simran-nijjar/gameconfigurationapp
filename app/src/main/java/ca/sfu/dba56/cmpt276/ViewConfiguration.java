package ca.sfu.dba56.cmpt276;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ca.sfu.dba56.cmpt276.model.Configuration;
import ca.sfu.dba56.cmpt276.model.ConfigurationsManager;

public class ViewConfiguration extends AppCompatActivity {

    private TextView expPoorScoreEditTxt;
    private TextView expGreatScoreEditTxt;
    ConfigurationsManager manager = ConfigurationsManager.getInstance();
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
        manager = ConfigurationsManager.getInstance();
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
        setUpDelete(currentConfigPosition);
        setUpAddGameButton();
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

    private void setUpAddGameButton(){
        Button addBtn = findViewById(R.id.addGameBtn);
        addBtn.setOnClickListener(v -> {
            Intent intent = AddNewGame.makeIntent(ViewConfiguration.this);
            ConfigurationsManager manager = ConfigurationsManager.getInstance();
            Configuration currentConfig = manager.get(currentConfigPosition);
            intent.putExtra("game name", currentConfig.getGameNameFromConfig());
            startActivity(intent);
        });
    }

    private void setUpDelete(int currentConfigPosition){
        Button deleteBtn = findViewById(R.id.btnDeleteConfig);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOrCancel(currentConfigPosition);
            }
        });
    }

    private void deleteOrCancel(int currentConfigPosition){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete?")
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        manager.remove(currentConfigPosition);
                        Intent k = new Intent(ViewConfiguration.this, MainActivity.class);
                        startActivity(k);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }
}