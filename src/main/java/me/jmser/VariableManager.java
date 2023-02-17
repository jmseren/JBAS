package me.jmser;

import java.util.Hashtable;

public class VariableManager {
    private static Hashtable<String, String> variables = new Hashtable<String, String>();

    public static VariableManager instance = new VariableManager();

    public static VariableManager getInstance(){
        return instance;
    }

    private VariableManager(){

    }

    public void setVariable(String name, String value){
        variables.put(name, value.trim());
    }

    public String getVariable(String name){

        // Special variables
        if(name.startsWith("rnd_")){
            int max = Integer.parseInt(name.split("_")[1]);
            return Integer.toString((int)(Math.random() * max));
        }


        return variables.get(name);
    }

    public void clear(){
        variables.clear();
    }

    
}
