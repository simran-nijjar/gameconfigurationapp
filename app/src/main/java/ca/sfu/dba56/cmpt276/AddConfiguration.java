package ca.sfu.dba56.cmpt276;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class AddConfiguration extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_configuration);
        setUpSaveConfigButton();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, AddConfiguration.class);
    }

    private void setUpSaveConfigButton(){
        Button saveConfigBtn = findViewById(R.id.saveConfigBtn);
        saveConfigBtn.setOnClickListener(v ->{
            //Toast message is temporary
            //TO DO: save game using ConfigurationsManager
            Toast.makeText(this, "You saved the configuration", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
