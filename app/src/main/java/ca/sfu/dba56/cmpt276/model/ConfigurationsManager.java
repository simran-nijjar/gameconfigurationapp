package ca.sfu.dba56.cmpt276.model;


/*
 * Singleton class
 * Keeps an array of all the game configurations
 * allows access to the items in the array
 */

import java.util.ArrayList;
import java.util.List;

public class ConfigurationsManager {

    private static  ConfigurationsManager instance;

    private ConfigurationsManager() {
        //private to prevent anyone else from instantiating
    }

    public static ConfigurationsManager getInstance(){
        if(instance == null){
            instance = new ConfigurationsManager();
        }
        return instance;
    }

    private List<Configuration> ListOfConfigurations = new ArrayList<>();

    public void add(Configuration newConfig) {ListOfConfigurations.add(newConfig);}
    public void remove(int i) {ListOfConfigurations.remove(i);}
    public boolean isEmpty() {return this.ListOfConfigurations.isEmpty();}


}