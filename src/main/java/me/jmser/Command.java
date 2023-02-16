package me.jmser;

public class Command {
    public Commands command;
    public String[] args;

    public Command(String s){
        s = s.trim().replaceAll("\\s+", " ");
        String[] parts = s.split(" ");
        this.command = Commands.fromString(parts[0]);
        this.args = new String[parts.length - 1];
        for(int i = 1; i < parts.length; i++){
            this.args[i - 1] = parts[i];
        }
    }
}
