package me.jmser;

import java.util.TreeMap;

/**
 * Hello world!
 *
 */
public class Interpreter 
{
    private static VariableManager variableManager = VariableManager.getInstance();
    private static ExpressionParser parser = ExpressionParser.getInstance();

    private static TreeMap<Integer, String> lines = new TreeMap<Integer, String>();
    public static int instructionPointer = -1;
    
    private void setFlags(boolean expressionResult){
        // Flags are used to make conditionals work
        variableManager.setVariable("FLAG_ELSE", !expressionResult ? "1" : "0");  
        variableManager.setVariable("FLAG_SKIP", !expressionResult ? "1" : "0");
    }

    public boolean interpret(String input){
        Command c = new Command(input);
        switch(c.command){
            case PRINT:
                for(String arg : c.args){
                    System.out.print(arg + " ");
                }
                System.out.println();
                break;
            case LET:
                variableManager.setVariable(c.args[0], c.args[1]);
                break;
            case EVAL:
                System.out.println(c.args[0]);
                break;
            case EXIT:
                return false;
            case LIST:
                for(Integer i : lines.keySet()){
                    System.out.println(i + " " + lines.get(i));
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
            case GOTO:
                instructionPointer = Integer.parseInt(c.args[0]) - 1;
                break;
            case GOSUB:
                variableManager.setVariable("ret", Integer.toString(instructionPointer));
                instructionPointer = Integer.parseInt(c.args[0]) - 1;
                break;
            case RETURN:
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
                }
                break;
            case INPUT:
                String inputString = System.console().readLine();
                if(inputString.matches("[A-z]+")){
                    inputString = "\"" + inputString + "\"";
                }
                variableManager.setVariable(c.args[0], inputString);
                break;
            case REM:
                break;
            default:
                System.out.println("Unknown command");
                break;
            
        }   
        return true;
    }

    public void run(){
        // Set flags and variables to default values
        variableManager.clear();

        while(instructionPointer <= lines.lastKey()){
            if(variableManager.getVariable("FLAG_EXIT").equals("1")) return; // Exit without resetting the flags/variables
            if(lines.containsKey(instructionPointer)){
                if(variableManager.getVariable("FLAG_SKIP").equals("1")){
                    variableManager.setVariable("FLAG_SKIP", "0");
                }else{
                    interpret(lines.get(instructionPointer));
                }
            }
            instructionPointer++;
        }

        variableManager.clear();
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
