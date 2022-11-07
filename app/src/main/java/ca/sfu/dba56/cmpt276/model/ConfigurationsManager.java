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
    private int index;

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

    public void setListOfConfigurations(List<Configuration> newList) {this.ListOfConfigurations = newList;}
    public List<Configuration> getListOfConfigurations() {return ListOfConfigurations;}
    public void add(Configuration newConfig) {ListOfConfigurations.add(newConfig);}
    public Configuration get(int i){return ListOfConfigurations.get(i);}
    public void remove(int i) {ListOfConfigurations.remove(i);}
    public boolean isEmpty() {return this.ListOfConfigurations.isEmpty();}
    public  int configListSize() {return ListOfConfigurations.size();}
    //change a config for a new config in a position in a list
    public void set(int currentConfigPosition, Configuration newConfig){ListOfConfigurations.set(currentConfigPosition, newConfig);}

    public int getIndex() {return index;}
    public void setIndex(int index) {this.index = index;}

}