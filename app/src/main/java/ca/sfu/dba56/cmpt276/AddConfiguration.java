package ca.sfu.dba56.cmpt276;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import ca.sfu.dba56.cmpt276.model.Configuration;
import ca.sfu.dba56.cmpt276.model.ConfigurationsManager;
import ca.sfu.dba56.cmpt276.model.SaveUsingGson;

public class AddConfiguration extends AppCompatActivity {
    private EditText gameNameTxt;
    private EditText expPoorScoreTxt;
    private EditText expGreatScoreTxt;

    private String strGameName;
    private String strExpPoorScore;
    private String strExpGreatScore;

    private int intExpPoorScore;
    private int intExpGreatScore;

    private final int MAX_NAME_LENGTH = 100;
    private final int MAX_POS_SCORE_INPUT = 100000000;
    private final int MAX_NEG_SCORE_INPUT = -100000000;

    //comment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_configuration);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getUserInput();
        checkUserInput();

        Bundle b = getIntent().getExtras();
        if(b!= null){
            //Edit Game Config Activity
            //Activity Name
            getSupportActionBar().setTitle("Edit Game Config");
            //check what position of configuration was selected
            int currentConfigPosition = b.getInt(getString(R.string.selected_config_position));
            ConfigurationsManager manager = ConfigurationsManager.getInstance();
            Configuration currentConfig = manager.get(currentConfigPosition);
            //Activity Name
            getSupportActionBar().setTitle("Edit Game " + currentConfig.getGameNameFromConfig() + " Configuration");
            //set variables from pre-existing config to the screen
            String gameName = String.valueOf(currentConfig.getGameNameFromConfig());
            String minScoreStr = String.valueOf(currentConfig.getMinPoorScoreFromConfig());
            String maxScoreStr = String.valueOf(currentConfig.getMaxBestScoreFromConfig());
            setVariablesFromExistingConfig(gameName, minScoreStr, maxScoreStr);
            setUpSaveConfigButton();
        }
        else{
            //Add Game Config Activity
            //Activity Name
            getSupportActionBar().setTitle("Add New Game Config");
            getUserInput();
            setUpSaveConfigButton();
        }
    }

  //add new configuration methods

    public static Intent makeIntent(Context context){
        return new Intent(context, AddConfiguration.class);
    }

    private void checkUserInput(){
        gameNameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Does not do anything
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                strGameName = gameNameTxt.getText().toString();
                if (strGameName.length() >= MAX_NAME_LENGTH){
                    displayMaxNameLengthMsg();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Does not do anything
            }
        });

        expPoorScoreTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Does not do anything
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                strExpPoorScore = expPoorScoreTxt.getText().toString();
                try{
                    intExpPoorScore = Integer.parseInt(strExpPoorScore);
                     if (intExpPoorScore >= MAX_POS_SCORE_INPUT || intExpPoorScore <= MAX_NEG_SCORE_INPUT) {
                        displayMaxScoreMsg(true);
                    }
                }catch (NumberFormatException ex){
                    if (expPoorScoreTxt.length() == 0) {
                        Toast.makeText(AddConfiguration.this, "Your input is empty or invalid", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Does not do anything
            }
        });

        expGreatScoreTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Does not do anything
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                strExpGreatScore = expGreatScoreTxt.getText().toString();
                try{
                    intExpGreatScore = Integer.parseInt(strExpGreatScore);
                    if (intExpGreatScore >= MAX_POS_SCORE_INPUT || intExpGreatScore <= MAX_NEG_SCORE_INPUT){
                        displayMaxScoreMsg(false);
                    }
                }catch (NumberFormatException ex){
                    if (expGreatScoreTxt.length() == 0) {
                        Toast.makeText(AddConfiguration.this, "Your input is empty or invalid", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Does not do anything
            }
        });
    }

    private void displayMaxNameLengthMsg(){
        AlertDialog alertDialog = new AlertDialog.Builder(AddConfiguration.this).create(); //Read Update
        alertDialog.setTitle("Game name is too long");
        alertDialog.setMessage("Sorry, the game name is too long. Please try a shorter name");
        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Stay on ViewAchievement activity
            }
        });
        alertDialog.show();
        //set num of player to the minimum
        gameNameTxt = findViewById(R.id.inputName);
        gameNameTxt.setText("");
    }

    private void displayMaxScoreMsg(boolean isPoorScore){
        AlertDialog alertDialog = new AlertDialog.Builder(AddConfiguration.this).create(); //Read Update
        alertDialog.setTitle("Score length is too long");
        alertDialog.setMessage("Sorry, score length is too long. Please try a number of shorter length");
        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Stay on ViewAchievement activity
            }
        });
        alertDialog.show();
        //set num of player to the minimum
        if (isPoorScore) {
            expPoorScoreTxt = findViewById(R.id.inputLowScore);
            expPoorScoreTxt.setText("");
        }
        else{
            expGreatScoreTxt = findViewById(R.id.inputHighScore);
            expGreatScoreTxt.setText("");
        }
    }

    private void getUserInput(){
        //Input into TextEdit variables
        gameNameTxt = findViewById(R.id.inputName);
        expPoorScoreTxt = findViewById(R.id.inputLowScore);
        expGreatScoreTxt = findViewById(R.id.inputHighScore);
    }

    private void setUpSaveConfigButton(){
        Button saveConfigBtn = findViewById(R.id.saveConfigBtn);
        saveConfigBtn.setOnClickListener(v ->{
            Bundle b = getIntent().getExtras();
            //get variables checked again for the new input
            getUserInput();
            convertTxtToStr();
            //if it is an add new config activity
            if(b == null){
                convertTxtToStr();
                //Save game if all values are valid
                if (isUserInputValid()) {
                    Configuration newConfig = new Configuration(strGameName, intExpPoorScore, intExpGreatScore);
                    ConfigurationsManager manager = ConfigurationsManager.getInstance();
                    manager.add(newConfig);
                    Toast.makeText(this, "You saved the configuration", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            //if it an edit config activity
            else{
                if (isUserInputValid()) {
                    int currentConfigPosition = b.getInt(getString(R.string.selected_config_position));
                    ConfigurationsManager manager = ConfigurationsManager.getInstance();
                    Configuration currentConfig = manager.get(currentConfigPosition);
                    currentConfig.setGameNameInConfig(strGameName);
                    currentConfig.setMinPoorScoreInConfig(intExpPoorScore);
                    currentConfig.setMaxBestScoreInConfig(intExpGreatScore);
                    manager.set(currentConfigPosition, currentConfig);
                    Toast.makeText(this, "You safely edited configuration", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void convertTxtToStr(){
        //Convert TextEdit into string
        strGameName = gameNameTxt.getText().toString();
        strExpPoorScore = expPoorScoreTxt.getText().toString();
        strExpGreatScore = expGreatScoreTxt.getText().toString();
    }

    private void convertStringToInt(){
        intExpPoorScore = Integer.parseInt(strExpPoorScore);
        intExpGreatScore = Integer.parseInt(strExpGreatScore);
    }

    private boolean isUserInputValid(){
        if (TextUtils.isEmpty(strGameName)
                || TextUtils.isEmpty(strExpPoorScore)
                || TextUtils.isEmpty(strExpGreatScore)){
            Toast.makeText(this, "ERROR: Text fields cannot be empty. Please enter correct values and try again",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        convertStringToInt();
        if (intExpPoorScore >= intExpGreatScore){
            Toast.makeText(this, "Poor score must be less than great score. Try again", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setVariablesFromExistingConfig(String gameName,String minScoreStr, String maxScoreStr) {
        //setting variables for the edit config activity
        getUserInput();

        gameNameTxt.setText(gameName);
        expPoorScoreTxt.setText(minScoreStr);
        expGreatScoreTxt.setText(maxScoreStr);

        convertTxtToStr();
        convertStringToInt();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //
    //end of the class
}
