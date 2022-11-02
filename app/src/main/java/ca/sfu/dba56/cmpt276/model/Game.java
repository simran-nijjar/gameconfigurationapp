package ca.sfu.dba56.cmpt276.model;


public class Game {
    private int players;
    private int scores;
    private String achievements[];


    public Game() {
    }

    public Game(int players, int scores) {
        this.players = players;
        this.scores = scores;
    }

    public Game(int players, int scores, String[] achievements) {
        this.players = players;
        this.scores = scores;
        this.achievements = achievements;
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


}
