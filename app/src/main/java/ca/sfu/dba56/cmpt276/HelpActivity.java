package ca.sfu.dba56.cmpt276;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
* Help activity class
* provides a clickable buttons that give info about the course and app
 */
public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setInfoBtn();
        setDescriptionBtn();
        setAchievementBtn();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, HelpActivity.class);
    }

    private void setInfoBtn() {
        Button info = findViewById(R.id.info);
        TextView info_content = findViewById(R.id.info_content);
        info_content.setVisibility(View.GONE);
        info.setOnClickListener(v -> {

            if(info_content.getVisibility() == View.GONE) {
                info_content.setText(R.string.info_content);
                info_content.setMovementMethod(LinkMovementMethod.getInstance());
                info_content.setVisibility(View.VISIBLE);
            }else if (info_content.getVisibility() == View.VISIBLE){
                info_content.setText("");
                info_content.setVisibility(View.GONE);
            }
        });

    }

    private void setDescriptionBtn(){
        Button description = findViewById(R.id.description);
        TextView description_content = findViewById(R.id.description_content);
        description_content.setVisibility(View.GONE);
        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(description_content.getVisibility() == View.GONE) {
                    description_content.setText(R.string.description_content);
                    description_content.setVisibility(View.VISIBLE);
                    description_content.setMovementMethod(LinkMovementMethod.getInstance());
                }else if (description_content.getVisibility() == View.VISIBLE){
                    description_content.setText("");
                    description_content.setVisibility(View.GONE);
                }
            }
        });

    }

    private void setAchievementBtn(){
        Button achieve = findViewById(R.id.achieve);
        TextView achieve_content = findViewById(R.id.achieve_content);
        achieve_content.setVisibility(View.GONE);
        achieve.setOnClickListener(v -> {

            if(achieve_content.getVisibility() == View.GONE) {
                achieve_content.setText(R.string.achieve_content);
                achieve_content.setVisibility(View.VISIBLE);
            }else if (achieve_content.getVisibility() == View.VISIBLE){
                achieve_content.setText("");
                achieve_content.setVisibility(View.GONE);
            }
        });
    }

}