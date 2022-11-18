package ca.sfu.dba56.cmpt276.model;

/*
* Class Achievements calculates the achievement level, expected min score and max score,
* and the range and bounds of the scores in each level.
* Keeps/populates the array of achievements names and allows to retrieve achievement names
 */

public class Achievements {

    private final String FANTASY = "Fantasy";
    private final String STAR_WARS = "Star Wars";
    private int numOfBoundedLevels;
    private String achievementsFruits[];
    private String achievementsFantasy[];
    private String achievementsStarWars[];
    private int intAchievements[] = new int[10];
    private String levelAchieved;
    private int minScore;
    private int maxScore;
    private String theme;

    public Achievements(String theme){
        //Three different arrays for three themes
        this.achievementsFruits = new String[]{"Beautiful Bananas", "Wonderful Watermelons",
                "Outstanding Oranges", "Admirable Apricots", "Good Grapefruits", "Amazing Apples",
                "Great Grapes", "Better Blueberries", "Super Strawberries", "Perfect Peaches"};
        this.achievementsFantasy = new String[]{"Stinky Orc", "Friendly Troll", "Grumpy Dwarf",
                "High Elf", "Powerful Lich", "Golden Pegasus", "Ferocious Minotaur", "Majestic Unicorn",
                "Shadow Wyvern", "Azure Dragon"};
        this.achievementsStarWars = new String[]{"Grogu", "Chewbacca", "Anakin", "Cara Dune", "Ahsoka Tano",
                "Luke Skywalker", "Yoda", "Darth Vader", "Asajj Ventress", "Leia Organa"};
        this.numOfBoundedLevels = 8;
        this.theme = theme;
    }

    public int getNumOfBoundedLevels(){
        return numOfBoundedLevels;
    }
    public String getLevelAchieved() {
        return levelAchieved;
    }
    public void setAchievementTheme(String theme){
        this.theme = theme;
    }
    public String getAchievementTheme(){
        return theme;
    }

    public String getAchievementLevel(int index){
        //Get the achievement level for the theme selected in radio button
        if (this.theme.equals(FANTASY)){
            return achievementsFantasy[index];
        }
        if (this.theme.equals(STAR_WARS)){
            return achievementsStarWars[index];
        }
        return achievementsFruits[index];
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