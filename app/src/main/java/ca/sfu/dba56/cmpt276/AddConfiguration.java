package ca.sfu.dba56.cmpt276;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import ca.sfu.dba56.cmpt276.model.Configuration;
import ca.sfu.dba56.cmpt276.model.ConfigurationsManager;

public class AddConfiguration extends AppCompatActivity {
    private EditText gameNameTxt;
    private EditText expPoorScoreTxt;
    private EditText expGreatScoreTxt;

    private String strGameName;
    private String strExpPoorScore;
    private String strExpGreatScore;

    private int intExpPoorScore;
    private int intExpGreatScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_configuration);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        if(b!= null){
            //Edit Game Config Activity
            //Activity Name
            getSupportActionBar().setTitle("Edit Game Config");
            //TODO: edit config
        }
        else{
            //Add Game Config Activity
            //Activity Name
            getSupportActionBar().setTitle("Add new Game Config");
            getUserInput();
            setUpSaveConfigButton();
        }
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, AddConfiguration.class);
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
            //Toast message is temporary
            //TODO: save game using ConfigurationsManager
            convertTxtToStr();
            //Save game if all values are valid
            if (isUserInputValid()) {
                Configuration newConfig = new Configuration(strGameName, intExpPoorScore, intExpGreatScore);
                ConfigurationsManager manager = ConfigurationsManager.getInstance();
                manager.add(newConfig);
                Toast.makeText(this, "You saved the configuration", Toast.LENGTH_SHORT).show();
                finish();
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

    //end of the class
}
