package ca.sfu.dba56.cmpt276.model;

/*
* Class Achievements calculates the achievement level, expected min score and max score,
* and the range and bounds of the scores in each level.
* Keeps/populates the array of achievements names and allows to retrieve achievement names
 */

public class Achievements {

    private int numOfBoundedLevels;
    private String achievements[];
    private int intAchievements[] = new int[10];
    private String levelAchieved;
    private int minScore;
    private int maxScore;

    public Achievements(){
        this.achievements = new String[]{//"Worst Level",
                                        "Beautiful Bananas",
                                        //"Bad Level",
                                        "Wonderful Watermelons",
                                        //"Okay Level",
                                        "Okay Oranges",
                                        //"Alright Level",
                                        "Admirable Apricots",
                                        //"Better Level",
                                        "Good Grapefruits",
                                        //"Good Level",
                                        "Amazing Apples",
                                        //"Almost There Level",
                                        "Great Grapes",
                                        //"Great Level",
                                        "Better Blueberries",
                                        //"Best Level",
                                        "Super Strawberries",
                                        //"Legendary Level"
                                        "Perfect Peaches"};
        this.numOfBoundedLevels = 8;
    }


    public int getNumOfBoundedLevels(){
        return numOfBoundedLevels;
    }
    public String getAchievementLevel(int index){
        return achievements[index];
    }
    public String getLevelAchieved() {
        return levelAchieved;
    }

    public int calculateMinMaxScore(int score, int numPlayers){
        return score * numPlayers;
    }

    public int calculateLevelRange(int min, int max){
        int range = ((max - min)/8);
        return range;
    }

    public void setAchievementsBounds(int min, int max, int players) {
        minScore = calculateMinMaxScore(min, players);
        maxScore = calculateMinMaxScore(max, players);
        this.intAchievements[0] = minScore;
        this.intAchievements[1] = minScore;
        int range = calculateLevelRange(minScore, maxScore);
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
        else if (combinedScore >= intAchievements[9]){
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

    public void setAchievementsScores(int min, int max, int players){
        minScore = calculateMinMaxScore(min, players);
        maxScore = calculateMinMaxScore(max, players);
        this.intAchievements[0] = minScore;
        this.intAchievements[1] = minScore;
        for (int i = 2; i <= (maxScore - minScore + 1); i++){
            this.intAchievements[i] = minScore + i - 1;
        }
        if ((maxScore - minScore + 1) != 8) {
            this.intAchievements[maxScore - minScore + 1] = maxScore;
        }
    }

    public void calculateScoreAchieved(int combinedScore){
        if (combinedScore < intAchievements[0]){
            this.levelAchieved = getAchievementLevel(0);
        }
        else if (combinedScore >= intAchievements[maxScore - minScore + 1]){
            this.levelAchieved = getAchievementLevel(9);
        }
        else{
            for (int i = 1; i < (maxScore - minScore + 2); i++){
                if (combinedScore == intAchievements[i]){
                    this.levelAchieved = getAchievementLevel(i);
                }
            }
        }
    }
}