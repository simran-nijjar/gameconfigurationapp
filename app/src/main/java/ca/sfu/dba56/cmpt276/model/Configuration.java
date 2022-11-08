package ca.sfu.dba56.cmpt276.model;

import java.util.ArrayList;
import java.util.List;

/*
 * Class Configuration contains information of the game
 * like game name, expected poor/great score and list of games played (history)
 * allows to add new games to the list, get/set min/max scores and name, and string with played games info
 */
public class Configuration {


    private String gameName;
    private int minPoorScore;
    private int maxBestScore;
    private List<Game> listOfGames = new ArrayList<>();

    //constructor
    public Configuration(String newGameName, int newMinScore, int newMaxScore){
        gameName = newGameName;
        minPoorScore = newMinScore;
        maxBestScore = newMaxScore;
    }

    //add new game (to the list)
    public void add(Game game){listOfGames.add(game);}
    // get a string containing info for 1 game for the history
    public String get(int i){
        return listOfGames.get(i).getDateGamePlayed() + "  Players: " + listOfGames.get(i).getPlayers() + " Scores: " + listOfGames.get(i).getScores() + " " + listOfGames.get(i).getLevelAchieved();
    }
    //setters
    public void setGameNameInConfig(String newName){gameName = newName;}
    public void setMinPoorScoreInConfig(int newScore){minPoorScore = newScore;}
    public void setMaxBestScoreInConfig(int newScore){maxBestScore = newScore;}
    //getters
    public String getGameNameFromConfig(){return gameName;}
    public int getMinPoorScoreFromConfig(){return minPoorScore;}
    public int getMaxBestScoreFromConfig(){return maxBestScore;}
    public int getSizeOfListOfConfigs(){
        return listOfGames.size();
    }
}
