package ca.sfu.dba56.cmpt276.model;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

/*
* class SaveUsingGson
* takes context from an activity
* and by utilizing shared preferences and Gson
* creates a json string of manager (class ConfigurationsManager)
* that allows to save or retrieve all the data in manager between app launches
*/
public class SaveUsingGson {

    private Context context;
    private Gson gson = new Gson();
    private SharedPreferences newPrefs;
    private String json;
    private SharedPreferences.Editor editor;
    private ConfigurationsManager manager = ConfigurationsManager.getInstance();
    private Type listType = new TypeToken<List<Configuration>>() {}.getType();

    //to save config manager
    public void saveToSharedRefs(Context newContext){
        this.context = newContext;
        newPrefs = context.getSharedPreferences("Save config manager",MODE_PRIVATE);
        editor = newPrefs.edit();
        json = gson.toJson(manager.getListOfConfigurations());
        editor.putString("MyObject", json);
        editor.apply();
    }
    //retrieve config manager
    public void retrieveFromSharedPrefs(Context newContext){
        this.context = newContext;
        newPrefs = context.getSharedPreferences("Save config manager",MODE_PRIVATE);
        editor = newPrefs.edit();
        json = newPrefs.getString("MyObject", "no manager saved");
        if (!Objects.equals(json, "no manager saved")) {
            manager.setListOfConfigurations(gson.fromJson(json, listType));
        }
    }
}
