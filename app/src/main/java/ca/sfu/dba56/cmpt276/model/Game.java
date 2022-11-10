package ca.sfu.dba56.cmpt276.model;

/*
* Game class stores info about one game played
* that includes num of players, their overall score,
* date game for played and level of achievement received
 */
public class Game {

    private int players;
    private int scores;
    private String dateGamePlayed;
    private Achievements achievements;
    private String levelAchieved;

    public Game(int players, int scores, Configuration manager, String dateGamePlayed, boolean isCalculatingRangeLevels, String theme) {
        achievements = new Achievements(theme);
        this.players = players;
        this.scores = scores;

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
    public String getDateGamePlayed() {return dateGamePlayed;}
    public String getLevelAchieved() {return levelAchieved;}

}
