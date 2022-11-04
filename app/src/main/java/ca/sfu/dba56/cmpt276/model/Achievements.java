package ca.sfu.dba56.cmpt276.model;

/* This class calculates the achievement level, expected min score and max score, and
* the range and bounds of the scores in each level.
 */
public class Achievements {
    private int numOfBoundedLevels;
    private String achievements[];
    private double intAchievements[] = new double[10];
    private String levelAchieved;
    private int minScore;
    private int maxScore;

    public Achievements(){
        this.achievements = new String[]{"Worst Level","Bad Level","Okay Level","Alright Level",
                "Better Level","Good Level","Almost There Level","Great Level","Best Level", "Legendary Level"};
        this.numOfBoundedLevels = 8;
    }

    public int getNumOfBoundedLevels(){
        return numOfBoundedLevels;
    }

    public String getAchievementLevel(int index){
        return achievements[index];
    }

    public int calculateMinMaxScore(int score, int numPlayers){
        return score * numPlayers;
    }

    public double calculateLevelRange(double min, double max){
        double range = ((max - min)/8);
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
        double range = calculateLevelRange(minScore, maxScore);
        for (int i = 2; i < intAchievements.length - 1; i++){
            this.intAchievements[i] = (this.intAchievements[i-1] + range + 1);
        }
        this.intAchievements[9] = maxScore + 1;
    }

    public void calculateLevelAchieved(int combinedScore){
        boolean calculatingLevel = true;
        if (combinedScore < intAchievements[0]){
            this.levelAchieved = getAchievementLevel(0);
        }
        else if (combinedScore >= this.intAchievements[9]){
            this.levelAchieved = getAchievementLevel(9);
        }
        else {
            while (calculatingLevel) {
                for (int i = 1; i < intAchievements.length - 1; i++) {
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