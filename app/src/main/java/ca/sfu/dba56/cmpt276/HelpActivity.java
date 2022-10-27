package ca.sfu.dba56.cmpt276;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setInfo();
        setDescription();
        setAchievement();
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, HelpActivity.class);
    }

    private void setInfo() {
        Button info = findViewById(R.id.info);
        TextView info_content = findViewById(R.id.info_content);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info_content.setText(R.string.info_content);
                info_content.setMovementMethod(LinkMovementMethod.getInstance());
            }
        });
    }

    private void setDescription(){
        Button description = findViewById(R.id.description);
        TextView description_content = findViewById(R.id.description_content);
        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description_content.setText(R.string.description_content);
            }
        });

    }

    private void setAchievement(){
        Button achieve = findViewById(R.id.achieve);
        TextView achieve_content = findViewById(R.id.achieve_content);
        achieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                achieve_content.setText(R.string.achieve_content);
            }
        });
    }

}