package me.jmser.jbas.interpreter;

import java.util.*;

import me.jmser.jbas.commands.Command;
import me.jmser.jbas.interfaces.JBasicInterface;

import java.io.*;

public class Interpreter 
{
    private static VariableManager variableManager = VariableManager.getInstance();

    private static TreeMap<Integer, String> lines = new TreeMap<Integer, String>();
    public static int instructionPointer = -1;
    private JBasicInterface iface = null;
    
    private void setFlags(boolean expressionResult){
        // Flags are used to make conditionals work
        variableManager.setVariable("FLAG_ELSE", !expressionResult ? "1" : "0");  
        variableManager.setVariable("FLAG_SKIP", !expressionResult ? "1" : "0");
    }

    public void hook(JBasicInterface i){
        this.iface = i;
        iface.println("\nJBasic 1.1 (c) 2023 JMSER\n\n");
    }

    public Interpreter(){
        variableManager.setVariable("FLAG_EXIT", "1");
    }

    public boolean interpret(String input){
        if(input.trim().equals("")) return true;
        Command c = new Command(input);
        switch(c.command){
            case PRINT:
                for(String arg : c.args){
                    iface.print(arg + " ");
                }
                iface.println();
                break;
            case LET:
                variableManager.setVariable(c.args[0], c.args[1]);
                break;
            case DIM:
                variableManager.createArray(c.args[0], Integer.parseInt(c.args[1]));
                break;
            case EVAL:
                System.out.println(c.args[0]);
                break;
            case EXIT:
                if(variableManager.getVariable("FLAG_EXIT").equals("1")) return false;
                variableManager.setVariable("FLAG_EXIT", "1");
                break;
            case LIST:
                for(Integer i : lines.keySet()){
                    iface.println(i + " " + lines.get(i));
                }
                break;
            case LINE:
                if(input.matches("[0-9]+ .*")){
                    String[] parts = input.split(" ", 2);
                    lines.put(Integer.parseInt(parts[0]), parts[1]);
                }
                break;
            case RUN:
                instructionPointer = 0;
                run();
                break;
            case GORET:
            case GOTO:
                instructionPointer = Integer.parseInt(c.args[0]) - 1;
                break;
            case GOSUB:
                variableManager.setVariable("ret", Integer.toString(instructionPointer));
                variableManager.pushVariable("ret");
                variableManager.pushVariable("FLAG_ELSE");
                variableManager.pushVariable("FLAG_SKIP");
                instructionPointer = Integer.parseInt(c.args[0]) - 1;
                break;
            case RETURN:
                variableManager.popVariable("ret");
                variableManager.popVariable("FLAG_ELSE");
                variableManager.popVariable("FLAG_SKIP");
                instructionPointer = Integer.parseInt(variableManager.getVariable("ret"));
                break;
            case IF:
                switch(c.args[1]){
                    case "==":
                        setFlags((c.args[0]).equals((c.args[2])));
                        break;
                    case "!=":
                        setFlags(!(c.args[0]).equals((c.args[2])));
                        break;
                    case ">":
                        setFlags(Integer.parseInt((c.args[0])) > Integer.parseInt((c.args[2])));
                        break;
                    case "<":
                        setFlags(Integer.parseInt((c.args[0])) < Integer.parseInt((c.args[2])));
                        break;
                    case ">=":
                        setFlags(Integer.parseInt((c.args[0])) >= Integer.parseInt((c.args[2])));
                        break;
                    case "<=":
                        setFlags(Integer.parseInt((c.args[0])) <= Integer.parseInt((c.args[2])));
                        break;
                    default:
                        System.out.println("Unknown operator");
                        break;
                        
                }
                break;
            case ELSE:
                if(variableManager.getVariable("FLAG_ELSE").equals("1")){
                    variableManager.setVariable("FLAG_ELSE", "0");
                    variableManager.setVariable("FLAG_SKIP", "0");
                }else{
                    variableManager.setVariable("FLAG_SKIP", "1");
                    variableManager.setVariable("FLAG_ELSE", "0");
                }
                break;
            case INPUT:
                String inputString = iface.getLine();
                inputString = "\"" + inputString + "\"";
                variableManager.setVariable(c.args[0], inputString);
                break;
            case REM:
                break;
            case CLEAR:
                lines.clear();
                break;
            case LOAD:
                lines.clear();
                variableManager.clear();
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(c.args[0]));
                    String line;
                    while((line = reader.readLine()) != null){
                        if(!line.matches("[0-9]+ .*")) continue; // Skip lines that don't start with a number.
                        String[] parts = line.split(" ", 2);
                        lines.put(Integer.parseInt(parts[0]), parts[1]);
                    }
                    reader.close();
                } catch (Exception e) {
                    System.out.println("Error loading file: " + e.getMessage());
                }
                break;
            case SAVE:
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(c.args[0]));
                    for(Integer i : lines.keySet()){
                        writer.write(i + " " + lines.get(i));
                        writer.newLine();
                    }
                    writer.close();
                } catch (Exception e) {
                    System.out.println("Error saving file: " + e.getMessage());
                }
                break;
            case TAB:
                iface.tab(Integer.parseInt(c.args[0]));
                break;
            default:
                System.out.println("Unknown command or variable: " + c.args[0]);
                break;
            
        }   
        return true;
    }

    public void run(){
        // Set flags and variables to default values
        variableManager.clear();

        while(true){
            if(variableManager.getVariable("FLAG_EXIT").equals("1")) break;
            if(lines.containsKey(instructionPointer)){
                if(variableManager.getVariable("FLAG_SKIP").equals("1")){
                    if(instructionPointer != lines.lastKey() && lines.get(instructionPointer).toUpperCase().matches("IF .*")){
                        variableManager.setVariable("FLAG_SKIP", "1");
                    }else{
                        variableManager.setVariable("FLAG_SKIP", "0");
                    }
                }else{
                    interpret(lines.get(instructionPointer));
                    
                }
            }
            if(instructionPointer == lines.lastKey()) break;
            instructionPointer = lines.higherKey(instructionPointer);
        }

        // variableManager.clear();
        variableManager.setVariable("FLAG_EXIT", "1");
    }

    public static int findNextLine(){
        int nextLine = lines.higherKey(Interpreter.instructionPointer);
        if(nextLine == -1) return 0;
        return nextLine;
    }

    public static int findPrevLine(){
        int previousLine = lines.lowerKey(Interpreter.instructionPointer);
        if(previousLine == -1) return 0;
        return previousLine;
    }

    public static int findLastLine(){
        return lines.lastKey();
    }

    public static int findFirstLine(){
        return lines.firstKey();
    }

    public static int lineCount(){
        return lines.size();
    }



}
