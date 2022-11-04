package ca.sfu.dba56.cmpt276.model;


import android.widget.Toast;

/* This class uses the range of the game and the player score to find out what
 * Achievement the player is.
 */
public class Achievements {
    private int index;
    private String achievements[];
    private int intAchievements[] = new int[9];
    private String levelAchieved;
    private int minScore;
    private int maxScore;

    public Achievements(){
        this.achievements = new String[]{"Worst Level","Bad Level","Okay Level","Alright Level",
                "Better Level","Good Level","Almost There Level","Great Level","Best Level"};
        this.index = 8;
    }

    public int getIndex(){
        return index;
    }

    public String getAchievementLevel(int index){
        return achievements[index];
    }

    public int calculateMinMaxScore(int score, int numPlayers){
        return score * numPlayers;
    }

    public int calculateLevelRange(int min, int max){
        int range = ((max - min)/8);
        return range;
    }

    public String getLevelAchieved() {
        return levelAchieved;
    }

    public void setAchievementsBounds(int min, int max, int players) {
        minScore = calculateMinMaxScore(min, players);
        maxScore = calculateMinMaxScore(max, players);
        this.intAchievements[0] = minScore;
        this.intAchievements[1] = minScore;
        int range = calculateLevelRange(minScore, maxScore);
        for (int i = 2; i < intAchievements.length; i++){
            this.intAchievements[i] = (this.intAchievements[i-1] + range + 1);
        }
    }

    public void calculateLevelAchieved(int combinedScore){
        boolean calculatingLevel = true;
        if (combinedScore < intAchievements[0]){
            this.levelAchieved = getAchievementLevel(0);
        }
        else if (combinedScore >= intAchievements[1] && combinedScore <= intAchievements[2]){
            this.levelAchieved = getAchievementLevel(1);
        }
        else {
            while (calculatingLevel) {
                for (int i = 2; i < intAchievements.length; i++) {
                    if (i == (intAchievements.length - 1)) {
                        if (combinedScore >= intAchievements[i] && combinedScore <= maxScore) {
                            this.levelAchieved = getAchievementLevel(i);
                            calculatingLevel = false;
                        }
                    } else {
                        if (combinedScore >= intAchievements[i] && combinedScore < intAchievements[i + 1]) {
                            this.levelAchieved = getAchievementLevel(i);
                            calculatingLevel = false;
                        }
                    }
                }
            }
        }
    }
}