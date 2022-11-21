package ca.sfu.dba56.cmpt276.model;

import java.util.List;

/*
* Game class stores info about one game played
* that includes num of players, their overall score,
* date game for played and level of achievement received
 */
public class Game {

    public static final String EASY = "Easy";
    public static final String HARD = "Hard";
    public static final String NORMAL = "Normal";
    private int players;
    private int scores;
    private String dateGamePlayed;
    private Achievements achievements;
    private String levelAchieved;
    private List<Integer> listOfValues;
    private String theme;
    private int difficulty;
    private int adjustDifficulty = 1;

    public Game(int players, int scores, List<Integer> listOfValues, Configuration manager,
                String dateGamePlayed, boolean isCalculatingRangeLevels, String theme, int difficultyLevel) {
        achievements = new Achievements(theme);

        this.players = players;
        this.scores = scores;
        this.listOfValues = listOfValues;
        this.theme = achievements.getAchievementTheme();
        this.difficulty = difficultyLevel;
        setDifficultyLevelAdjuster();

        if (isCalculatingRangeLevels) {
            //Set the achievement level range bounds with the expected poor * adjustment value for difficulty and expected great score for difficulty
            achievements.setAchievementsBounds(manager.getPoorDifficultyScore(getAdjustDifficulty()), manager.getGreatDifficultyScore(getAdjustDifficulty()), players);
            achievements.calculateLevelAchieved(scores);
        } else { //Set the achievement level score bounds with the expected poor * adjustment value for difficulty and expected great score for difficulty
            achievements.setAchievementsScores(manager.getPoorDifficultyScore(getAdjustDifficulty()), manager.getGreatDifficultyScore(getAdjustDifficulty()), players);
            achievements.calculateScoreAchieved(scores);
        }
        this.levelAchieved = achievements.getLevelAchieved();
        this.dateGamePlayed = dateGamePlayed;
    }

    public void setScores(int scores) {this.scores = scores;}
    public void setLevelAchieved(String levelAchieved) {this.levelAchieved = levelAchieved;}
    public void setListOfValues(List<Integer> listOfValues) {this.listOfValues = listOfValues;}
    public List<Integer> getListOfValues() {return listOfValues;}
    public int getPlayers() {return players;}
    public int getScores() {return scores;}
    public String getDateGamePlayed() {return dateGamePlayed;}
    public String getLevelAchieved() {return levelAchieved;}
    public String getTheme() {return theme;}
    public void setTheme(String theme) {this.theme = theme;}
    public String getDifficultyLevelTitle(){ //Returns the string difficulty name
        if (this.difficulty == 0){
            return EASY;
        }
        if (this.difficulty == 2){
            return HARD;
        }
        return NORMAL;
    }
    public void setDifficultyLevelAdjuster(){
        if (this.difficulty == 0){
            this.adjustDifficulty *= 0.75;
        }
        if (this.difficulty == 1){
            this.adjustDifficulty *= 1;
        }
        if (this.difficulty == 2){
            this.adjustDifficulty *= 1.25;
        }
    }
    public int getAdjustDifficulty(){ //Returns the value to adjust difficulty by
        return adjustDifficulty;
    }
}
