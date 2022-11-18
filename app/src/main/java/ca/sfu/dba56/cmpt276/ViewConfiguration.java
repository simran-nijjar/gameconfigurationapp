package ca.sfu.dba56.cmpt276;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ca.sfu.dba56.cmpt276.model.Configuration;
import ca.sfu.dba56.cmpt276.model.ConfigurationsManager;
import ca.sfu.dba56.cmpt276.model.SaveUsingGson;

/*
* view config activity class shows basic info of config after it was clicked from main activity
* it has buttons for edit, delete, view achievements,
* add new game and history (when there were played games)
 */

public class ViewConfiguration extends AppCompatActivity {

    private TextView expPoorScoreEditTxt;
    private TextView expGreatScoreEditTxt;
    private Button editConfigScreenBtn;
    private int currentConfigPosition;
    private ConfigurationsManager manager = ConfigurationsManager.getInstance();
    private SaveUsingGson toSaveUsingGsonAndSP = new SaveUsingGson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_configuration);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        UpdateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpGameHistoryButton();
        UpdateUI();
        //to save config manager
        toSaveUsingGsonAndSP.saveToSharedRefs(ViewConfiguration.this);
    }


    private void UpdateUI() {
        currentConfigPosition = manager.getIndex();
        manager = ConfigurationsManager.getInstance();
        Configuration currentConfig = manager.getItemAtIndex(currentConfigPosition);
        //Activity Name
        getSupportActionBar().setTitle(getString(R.string.game___configuration, currentConfig.getGameNameFromConfig()));
        //find locations
        expPoorScoreEditTxt = findViewById(R.id.textFillPoorScoreConfigView);
        expGreatScoreEditTxt = findViewById(R.id.textFillGreatScoreConfigView);
        //populate them
        expPoorScoreEditTxt.setText(String.valueOf(currentConfig.getMinPoorScoreFromConfig()));
        expGreatScoreEditTxt.setText(String.valueOf(currentConfig.getMaxBestScoreFromConfig()));

        //make edit button open edit configuration screen.
        editConfigScreenBtn = findViewById(R.id.btnEditConfig);
        editConfigScreenBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ViewConfiguration.this, AddEditConfiguration.class);
            intent.putExtra(getString(R.string.selected_config_position), currentConfigPosition);
            startActivity(intent);
        });
        //set up buttons for new game and history
        setUpGameHistoryButton();
        setUpDeleteButton(currentConfigPosition);
        setUpAddGameButton();
        expGreatScoreEditTxt.setText(String.valueOf(currentConfig.getMaxBestScoreFromConfig()));
        setUpViewAchievementsButton();
        //to save config manager
        toSaveUsingGsonAndSP.saveToSharedRefs(ViewConfiguration.this);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ViewConfiguration.class);
    }

    private void setUpViewAchievementsButton() {
        Button achievementBtn = findViewById(R.id.viewAchievementsBtn);
        achievementBtn.setOnClickListener(v ->{
            Intent intent = ViewAchievements.makeIntent(ViewConfiguration.this);
            intent.putExtra("Achievement Game Name", currentConfigPosition);
            startActivity(intent);
        });
    }

    private void setUpGameHistoryButton() {
        Button historyBtn = findViewById(R.id.btnHistoryConfig);
        // image made from miro
        // https://miro.com
        ImageView image = findViewById(R.id.image_history);
        if(manager.getItemAtIndex(currentConfigPosition).getSizeOfListOfConfigs() == 0){
            historyBtn.setVisibility(View.INVISIBLE);
            image.setVisibility(View.VISIBLE);
        }else {
            //get rid of image
            image.setVisibility(View.INVISIBLE);
            historyBtn.setVisibility(View.VISIBLE);
            historyBtn.setOnClickListener(v -> {
                Intent intent2 = GameHistory.makeIntent(ViewConfiguration.this);
                //intent2.putExtra(getString(R.string.gameName2), currentConfigPosition);
                startActivity(intent2);
            });
        }
    }

    private void setUpAddGameButton() {
        Button addBtn = findViewById(R.id.addGameBtn);
        addBtn.setOnClickListener(v -> {
            Intent intent = AddNewGame.makeIntent(ViewConfiguration.this);
//            manager = ConfigurationsManager.getInstance();
//            Configuration currentConfig = manager.getItemAtIndex(currentConfigPosition);
//            intent.putExtra(getString(R.string.gameName), currentConfig.getGameNameFromConfig());
            startActivity(intent);
        });
    }

    private void setUpDeleteButton(int currentConfigPosition) {
        Button deleteBtn = findViewById(R.id.btnDeleteConfig);
        deleteBtn.setOnClickListener(v -> deleteOrCancel(currentConfigPosition));
    }

    //ask user on click of delete button
    //if he intents to delete config or not
    private void deleteOrCancel(int currentConfigPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.delete_msg))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.delete), (dialog, id) -> {
                    manager.remove(currentConfigPosition);
                    //saves the change
                    toSaveUsingGsonAndSP.saveToSharedRefs(ViewConfiguration.this);
                    Intent k = new Intent(ViewConfiguration.this, MainActivity.class);
                    startActivity(k);

                })
                .setNegativeButton(getString(R.string.cancel), (dialog, id) -> dialog.cancel());

        AlertDialog alert = builder.create();
        alert.show();
    }

}