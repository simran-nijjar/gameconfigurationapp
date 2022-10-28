package ca.sfu.dba56.cmpt276.model;


public class Game {
    private int players;
    private int expectedLow;
    private int expectedHigh;
    private String achievements[];

    //Default Constructor
    private Game(){
    }

    public int getPlayers(){
        return players;
    }

    public void setPlayers(int players){
        this.players = players;
    }

    public int getExpectedLow(){
        return expectedLow;
    }

    public void setExpectedLow(int low){
        this.expectedLow = low;
    }

    public int getExpectedHigh(){
        return expectedHigh;
    }

    public void setExpectedHigh(int high){
        this.expectedHigh = high;
    }
}
