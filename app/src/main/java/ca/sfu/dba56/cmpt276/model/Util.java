package ca.sfu.dba56.cmpt276.model;

public class Util{

//    public static boolean isScoreEmpty(String str){
//        return str.length()>0;
//    }

    public static boolean isValidScore(String str){
        int score;
        try{
            score = Integer.parseInt(str);
        }catch (NumberFormatException ex){
            score = -10000001;
        }
        return score > -10000000 && score < 10000000;
    }
}