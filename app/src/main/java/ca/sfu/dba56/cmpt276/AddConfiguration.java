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

public class AddConfiguration extends AppCompatActivity {
    private EditText gameNameTxt;
    private EditText expPoorScoreTxt;
    private EditText expGreatScoreTxt;

    private String strGameName;
    private String strExpPoorScore;
    private String strExpGreatScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_configuration);
        getUserInput();
        setUpSaveConfigButton();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
            //TO DO: save game using ConfigurationsManager
            convertTxtToStr();
            //Save game if all values are not empty
            if (checkInputNotEmpty(strGameName) == true
                    && checkInputNotEmpty(strExpPoorScore) == true
                    && checkInputNotEmpty(strExpGreatScore) == true) {
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

    private boolean checkInputNotEmpty(String userInput){
        if (TextUtils.isEmpty(userInput)){
            Toast.makeText(this, "ERROR: Text fields cannot be empty. Please enter correct values and try again",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
