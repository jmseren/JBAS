package me.jmser.jbas.commands;
import java.util.Hashtable;

public class Library {
    private static String currentPath = System.getProperty("user.dir");


    public Hashtable<String, Function> functions = new Hashtable<String, Function>();
    public String executablePath;
    public String flags = "";
    public String prefix = "";

    public Library(String executablePath) {
        this.executablePath = currentPath + "/" + executablePath;
        this.functions = new Hashtable<String, Function>();
    }

    public void addFunction(Function function) {
        this.functions.put(function.name, function);
    }

    public Function getFunction(String name) {
        return this.functions.get(name);
    }
    


}
