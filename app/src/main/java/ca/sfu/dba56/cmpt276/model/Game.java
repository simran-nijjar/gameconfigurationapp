package ca.sfu.dba56.cmpt276.model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Game {
    private int players;
    private int scores;
    private String achievements[];
    private String dateGamePlayed;

    public Game(int players, int scores, String[] achievements, String dateGamePlayed) {
        this.players = players;
        this.scores = scores;
        this.achievements = achievements;
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

    public String[] getAchievements() {
        return achievements;
    }

    public void setAchievements(String[] achievements) {
        this.achievements = achievements;
    }

    public String getDateGamePlayed(){
        return dateGamePlayed;
    }
}
