package ca.sfu.dba56.cmpt276.model;

import java.util.ArrayList;
import java.util.List;

/*
* Game class stores info about one game played
* that includes num of players, their overall score,
* date game for played and level of achievement received
 */
public class Game {

    private int players;
    private int scores;
    private String dateGamePlayed;
    private Achievements achievements = new Achievements();
    private String levelAchieved;
    private List<Integer> listOfValues = new ArrayList<>();

    public Game(int players, int scores, List<Integer> listOfValues, Configuration manager, String dateGamePlayed, boolean isCalculatingRangeLevels) {

        this.players = players;
        this.scores = scores;
        this.listOfValues = listOfValues;

        if (isCalculatingRangeLevels) {
            achievements.setAchievementsBounds(manager.getMinPoorScoreFromConfig(), manager.getMaxBestScoreFromConfig(), players);
            achievements.calculateLevelAchieved(scores);
        } else {
            achievements.setAchievementsScores(manager.getMinPoorScoreFromConfig(), manager.getMaxBestScoreFromConfig(), players);
            achievements.calculateScoreAchieved(scores);
        }
        this.levelAchieved = achievements.getLevelAchieved();
        this.dateGamePlayed = dateGamePlayed;
    }

    public int getPlayers() {return players;}
    public int getScores() {return scores;}
    public List<Integer> getListOfValues() {return listOfValues;}
    public String getDateGamePlayed() {return dateGamePlayed;}
    public String getLevelAchieved() {return levelAchieved;}

}
