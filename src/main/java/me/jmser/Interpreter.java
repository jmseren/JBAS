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
    private static int instructionPointer = -1;
    
    private void setFlags(boolean expressionResult){
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
            case IF:
                switch(c.args[1]){
                    case "==":
                        setFlags(parser.parse(c.args[0]).equals(parser.parse(c.args[2])));
                        break;
                    case "!=":
                        setFlags(!parser.parse(c.args[0]).equals(parser.parse(c.args[2])));
                        break;
                    case ">":
                        setFlags(parser.parse(c.args[0]).compareTo(parser.parse(c.args[2])) > 0);
                        break;
                    case "<":
                        setFlags(parser.parse(c.args[0]).compareTo(parser.parse(c.args[2])) < 0);
                        break;
                    case ">=":
                        setFlags(parser.parse(c.args[0]).compareTo(parser.parse(c.args[2])) >= 0);
                        break;
                    case "<=":
                        setFlags(parser.parse(c.args[0]).compareTo(parser.parse(c.args[2])) <= 0);
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
                variableManager.setVariable(c.args[0], System.console().readLine());
                break;
            default:
                System.out.println("Unknown command");
                break;
        }   
        return true;
    }

    public void run(){
        // Set flags
        variableManager.clear();
        variableManager.setVariable("FLAG_ELSE", "0");
        variableManager.setVariable("FLAG_SKIP", "0");


        while(instructionPointer <= lines.lastKey()){
            if(lines.containsKey(instructionPointer)){
                if(variableManager.getVariable("FLAG_SKIP").equals("1")){
                    variableManager.setVariable("FLAG_SKIP", "0");
                }else{
                    interpret(lines.get(instructionPointer));
                }
            }
            instructionPointer++;
        }

        instructionPointer = 0;
    }

}
