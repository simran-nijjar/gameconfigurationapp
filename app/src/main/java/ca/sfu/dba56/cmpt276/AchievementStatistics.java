//Referenced https://www.youtube.com/watch?v=pi1tq-bp7uA and https://www.youtube.com/watch?v=H6QxMBI2QH4
//Creating a Simple Bar Graph for your Android Application (part 1/2) and Creating a Simple Bar Graph for your Android Application (part 2/2) by CodingWithMitch
//https://weeklycoding.com/mpandroidchart-documentation/
//https://stackoverflow.com/a/69951831

package ca.sfu.dba56.cmpt276;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import ca.sfu.dba56.cmpt276.model.ConfigurationsManager;

public class AchievementStatistics extends AppCompatActivity {
    private final int POOR_SCORE = 1;
    private final int GREAT_SCORE = 10;
    private BarChart statsGraph;
    private ConfigurationsManager manager = ConfigurationsManager.getInstance();
    private int selectedGamePosition;
    private ArrayList<Integer> achievementLevelNumbers; //Arraylist of all of the achievement levels number values
    private ArrayList<BarEntry> timesLevelEarned; //Arraylist of the number of times user achieved a level for a game configuration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_statistics);
        statsGraph = findViewById(R.id.stats_graph);
        selectedGamePosition = manager.getIndex();
        createAchievementStatisticsGraph();
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, AchievementStatistics.class);
    }

    public void createAchievementStatisticsGraph() {
        //The x-axis values for the number of achievement levels
        achievementLevelNumbers = new ArrayList<>();
        for (int i = POOR_SCORE; i <= GREAT_SCORE; i ++) {
            achievementLevelNumbers.add(i);
        }
        //The y-axis values for the times user has earned each level
        timesLevelEarned = new ArrayList<>();
        for (int i = 0; i < achievementLevelNumbers.size(); i++) {
            //Levels start from 1 so start from (i+1)
            //Second parameter gets the number of times user has achieved the level which is stored in an array specific to each game config.
            //If level is not earned at least once
            if (manager.getItemAtIndex(selectedGamePosition).getAchievementsEarnedStats(i) == 0){
                //Don't add to graph
            } else {
                timesLevelEarned.add(new BarEntry(i + 1, manager.getItemAtIndex(selectedGamePosition).getAchievementsEarnedStats(i)));
            }
        }
        //Create the data set for the number of times user has earned each level
        BarDataSet levelsEarnedDataSet = new BarDataSet(timesLevelEarned, "Times Level Earned");
        //Display the bar of the data set
        BarData barLevelsEarned = new BarData(levelsEarnedDataSet);
        statsGraph.setData(barLevelsEarned);
        statsGraph.setDragEnabled(true);
        Description description = new Description();
        description.setText("");
        statsGraph.setDescription(description);
        //x-axis modifications
        XAxis xAxis = statsGraph.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        //y-axis modifications
        YAxis leftYAxis = statsGraph.getAxisLeft();
        leftYAxis.setGranularity(1f); //Increase by 1 for y-axis
        YAxis rightYAxis = statsGraph.getAxisRight();
        rightYAxis.setEnabled(false); //Remove right y-axis
    }
}
