package ca.sfu.dba56.cmpt276.model;


public class Game {
    private int players;
    private int scores;
    private String dateGamePlayed;
    private Achievements achievements = new Achievements();
    String levelAchieved;

    public Game(int players, int scores, Configuration manager, String dateGamePlayed) {
        this.players = players;
        this.scores = scores;

        achievements.setAchievementsBounds(manager.getMinPoorScoreFromConfig(), manager.getMaxBestScoreFromConfig(), players);
        achievements.calculateLevelAchieved(scores);

        this.levelAchieved = achievements.getLevelAchieved();
        this.dateGamePlayed = dateGamePlayed;
    }

    public int getPlayers(){
        return players;
    }

    public void setPlayers(int players){
        this.players = players;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public String getDateGamePlayed(){
        return dateGamePlayed;
    }

    public String getLevelAchieved(){
        return levelAchieved;
    }
}
