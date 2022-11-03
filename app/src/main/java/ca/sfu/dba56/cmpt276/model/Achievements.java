package ca.sfu.dba56.cmpt276.model;


/* This class uses the range of the game and the player score to find out what
 * Achievement the player is.
 */
public class Achievements {
    private int index;
    private String achievements[];

    public Achievements(){
        this.achievements = new String[]{"0","A","B","C","D","E","F","G","H"};
        this.index = 8;
    }

    public int getIndex(){
        return index;
    }

    public String getAchievementLevel(int index){
        return achievements[index];
    }

    public int calculateMinMaxScore(int score, int numPlayers){
        return score * numPlayers;
    }

    public int calculateLevelRange(int min, int max){
        int range = ((max - min)/8);
        return range;
    }
}