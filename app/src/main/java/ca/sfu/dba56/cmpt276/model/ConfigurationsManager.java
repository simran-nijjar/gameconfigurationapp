package ca.sfu.dba56.cmpt276.model;

import java.util.ArrayList;
import java.util.List;

/*
 * Singleton class ConfigurationsManager
 * Keeps an array of all the game configurations and index of selected config
 * allows add and remove items in the array as well as items access to get and set them
 */
public class ConfigurationsManager {

    private static ConfigurationsManager instance;
    private int index;
    private List<Configuration> ListOfConfigurations = new ArrayList<>();

    private ConfigurationsManager() {
        //private to prevent anyone else from instantiating
    }

    public static ConfigurationsManager getInstance(){
        if(instance == null){
            instance = new ConfigurationsManager();
        }
        return instance;
    }

    public void add(Configuration newConfig) {ListOfConfigurations.add(newConfig);}
    public void remove(int i) {ListOfConfigurations.remove(i);}
    public boolean isEmpty() {return this.ListOfConfigurations.isEmpty();}
    public  int configListSize() {return ListOfConfigurations.size();}

    //setters
    public void setListOfConfigurations(List<Configuration> newList) {this.ListOfConfigurations = newList;}
    public void setItemAtIndex(int currentConfigPosition, Configuration newConfig){ListOfConfigurations.set(currentConfigPosition, newConfig);}
    public void setIndex(int index) {this.index = index;}
    //getters
    public List<Configuration> getListOfConfigurations() {return ListOfConfigurations;}
    public Configuration getItemAtIndex(int i){return ListOfConfigurations.get(i);}
    public int getIndex() {return index;}


}