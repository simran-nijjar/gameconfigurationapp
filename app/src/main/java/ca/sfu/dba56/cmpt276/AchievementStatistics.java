package ca.sfu.dba56.cmpt276;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;

import ca.sfu.dba56.cmpt276.model.ConfigurationsManager;

public class AchievementStatistics extends AppCompatActivity {
    private BarChart statsGraph;
    private ConfigurationsManager manager = ConfigurationsManager.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_achievement_statistics);
        statsGraph = findViewById(R.id.stats_graph);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, AchievementStatistics.class);
    }
}