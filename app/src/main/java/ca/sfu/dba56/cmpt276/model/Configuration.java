package ca.sfu.dba56.cmpt276.model;

/*
* Class Configuration Description
* works as pseudo GameManager Class
*/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Configuration {

    //variables
    //name of the game
    private String gameName;
    //range for poor score
    private int minPoorScore;
    //range for best score
    private int maxBestScore;
    //min num of players (2)
    private final int minNumOfPlayers = 2;
    //max num of player
    private int maxNumOfPlayers;
    //list of games (history)
    private List<Game> listOfGames = new ArrayList<>();

    //constructor
    public Configuration(String newGameName, int newMinScore, int newMaxScore){
        gameName = newGameName;
        minPoorScore = newMinScore;
        maxBestScore = newMaxScore;
    }

    //methods
    //add new game (to the list)
    public void add(Game game){listOfGames.add(game);}
    //delete a game (from list on position i)
    public void remove(int i){listOfGames.remove(i);}
    // show the list in game history screen
    public String get(int i){
        return  "Players: " + listOfGames.get(i).getPlayers() + " Scores: " + listOfGames.get(i).getScores() + " " + listOfGames.get(i).getDateGamePlayed()
                + " " + listOfGames.get(i).getLevelAchieved();
    }
    public int size(){
        return listOfGames.size();
    }

    //setters
    public void setGameNameInConfig(String newName){gameName = newName;}
    public void setMinPoorScoreInConfig(int newScore){minPoorScore = newScore;}
    public void setMaxBestScoreInConfig(int newScore){maxBestScore = newScore;}
    public void setMaxNumOfPlayers(int newNumMaxPlayers){maxNumOfPlayers = newNumMaxPlayers;}
    //getters
    public String getGameNameFromConfig(){return gameName;}
    public int getMinPoorScoreFromConfig(){return minPoorScore;}
    public int getMaxBestScoreFromConfig(){return maxBestScore;}
    public int getMaxNumOfPlayersFromConfig(){return maxNumOfPlayers;}

}
