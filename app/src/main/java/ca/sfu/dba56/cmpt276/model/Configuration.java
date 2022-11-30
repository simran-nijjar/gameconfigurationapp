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
    private int achievementsEarnedStats[];

    //constructor
    public Configuration(String newGameName, int newMinScore, int newMaxScore){
        gameName = newGameName;
        minPoorScore = newMinScore;
        maxBestScore = newMaxScore;
        this.achievementsEarnedStats = new int[]{0,0,0,0,0,0,0,0,0,0};
    }

    //add new game (to the list)
    public void add(Game game){listOfGames.add(game);}
    // get a string containing info for 1 game for the history
    public String get(int i){
        return listOfGames.get(i).getDateGamePlayed() + "  Players: " + listOfGames.get(i).getPlayers() + " Combined Score: " + listOfGames.get(i).getScores() +
                " Difficulty Level: " + listOfGames.get(i).getDifficultyLevelTitle() + " Level Achieved: " + listOfGames.get(i).getLevelAchieved();
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
    public int getPlayer(int i){return listOfGames.get(i).getPlayers();}
    public List<Integer> getListOfValues(int i){return listOfGames.get(i).getListOfValues();}
    public Game getGame(int i){return listOfGames.get(i);}
    public String getTheme(int i){return listOfGames.get(i).getTheme();}
    public void addAchievementsEarnedStats(int index){this.achievementsEarnedStats[index] += 1;}
    public void removeAchievementsEarnedStats(int index){
        if (this.achievementsEarnedStats[index] == 0){
            throw new IllegalArgumentException("Cannot remove level earned from achievement stats because the stat for this level is zero\n");
        }
        this.achievementsEarnedStats[index] -= 1;
    }
    public int getAchievementsEarnedStats(int index){return this.achievementsEarnedStats[index];}
    //Returns true for game configurations added before this iteration
    public boolean isAchievementsEarnedStatsArrNull(){
        if (this.achievementsEarnedStats == null){
            return true;
        }
        return false;
    }
}
