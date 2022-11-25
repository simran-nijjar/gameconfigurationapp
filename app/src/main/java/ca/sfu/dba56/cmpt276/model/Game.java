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
    private double adjustDifficulty;
    private int minScore;
    private int maxScore;

    public Game(int players, int scores, List<Integer> listOfValues, Configuration manager,
                String dateGamePlayed, boolean isCalculatingRangeLevels, String theme, int difficultyLevel) {
        achievements = new Achievements(theme);

        this.players = players;
        this.scores = scores;
        this.listOfValues = listOfValues;
        this.theme = achievements.getAchievementTheme();
        this.difficulty = difficultyLevel;
        this.adjustDifficulty = setDifficultyLevelAdjuster(difficultyLevel);

        //Get the expected poor and expected great score from game configuration
        minScore = manager.getMinPoorScoreFromConfig();
        maxScore = manager.getMaxBestScoreFromConfig();

        //Adjust expected poor and great score depending on difficulty selected in radio button
        switch(difficultyLevel){
            case 0: //Easy level
                minScore *= 0.75;
                maxScore *= 0.75;
                break;
            case 1:
                break;
            case 2: //Hard level
                minScore *= 1.25;
                maxScore *= 1.25;
                break;
        }

        if (isCalculatingRangeLevels) {
            //Set the achievement level range bounds with the expected poor * adjustment value for difficulty and expected great score for difficulty
            achievements.setAchievementsBounds(minScore, maxScore, players);
            achievements.calculateLevelAchieved(scores);
        }
        else { //Set the achievement level score bounds with the expected poor * adjustment value for difficulty and expected great score for difficulty
            achievements.setAchievementsScores(minScore, maxScore, players);
            achievements.calculateScoreAchieved(scores);
        }
        this.levelAchieved = achievements.getLevelAchieved();
        this.dateGamePlayed = dateGamePlayed;
    }

    public String setAchievementForEditGame(int players, int combined_scores, Configuration manager, boolean isCalculatingRangeLevels, String theme, int difficultyLevel){
        achievements = new Achievements(theme);
        this.players = players;
        this.scores = combined_scores;
        this.difficulty = difficultyLevel;
        this.adjustDifficulty = setDifficultyLevelAdjuster(difficultyLevel);
        minScore = manager.getMinPoorScoreFromConfig();
        maxScore = manager.getMaxBestScoreFromConfig();
        switch(difficultyLevel){
            case 0: //Easy level
                minScore *= 0.75;
                maxScore *= 0.75;
                break;
            case 1: //Normal Level
                break;
            case 2: //Hard level
                minScore *= 1.25;
                maxScore *= 1.25;
                break;
        }

        if (isCalculatingRangeLevels) {
            //Set the achievement level range bounds with the expected poor * adjustment value for difficulty and expected great score for difficulty
            achievements.setAchievementsBounds(minScore, maxScore, players);
            achievements.calculateLevelAchieved(scores);
        }
        else { //Set the achievement level score bounds with the expected poor * adjustment value for difficulty and expected great score for difficulty
            achievements.setAchievementsScores(minScore, maxScore, players);
            achievements.calculateScoreAchieved(scores);
        }
        this.levelAchieved = achievements.getLevelAchieved();
        return this.levelAchieved;
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
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
    public int getDifficulty() {
        return difficulty;
    }
    //Sets the value to adjust the expected poor and great score to multiply with
    public double setDifficultyLevelAdjuster(int difficulty){
        int adj = 1;
        if (difficulty == 0){
            adj *= 0.75;
        }else if(difficulty == 2){
            adj *= 1.25;
        }
        return (double)adj;
    }
    public int getIndexGameLevelAchieved(){return achievements.getIndexLevelAchieved();}
    public String getNextAchievementLevel(){return achievements.getNextAchievementLevel();}
    public void setNextAchievementLevel(String nextLevel){achievements.setNextAchievementLevel(nextLevel);}
    public boolean isHighestLevelAchieved(){return achievements.isHighestLevelAchieved();}
    public int getPointsAwayFromNextLevel(){return achievements.getPointsAwayFromNextLevel();}
}
