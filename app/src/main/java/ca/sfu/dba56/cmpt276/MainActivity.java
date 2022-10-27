package ca.sfu.dba56.cmpt276;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpHelpButton();
        setUpAddConfigurationButton();
    }

    private void setUpHelpButton() {
        Button helpBtn = (Button)findViewById(R.id.helpBtn);
        helpBtn.setOnClickListener(v -> {
            Intent intent = HelpActivity.makeIntent(MainActivity.this);
            startActivity(intent);
        });
    }

    private void setUpAddConfigurationButton(){
        FloatingActionButton addConfigBtn = findViewById(R.id.addBtn);
        addConfigBtn.setOnClickListener(v ->{
            Intent intent = AddConfiguration.makeIntent(MainActivity.this);
            startActivity(intent);
        });
    }
}