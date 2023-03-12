package me.jmser.jbas.interpreter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Stack;

import me.jmser.jbas.commands.LibraryManager;

public class VariableManager {
    private static final String[] standard = {
        "line",
        "time",
        "milli",
        "rnd",
        "neg",
        "abs",
        "sqrt",
        "TAB"
    };


    private static ExpressionParser parser = ExpressionParser.getInstance();
    
    private static Hashtable<String, String> variables = new Hashtable<String, String>();

    private static Hashtable<String, Stack<String>> variableStack = new Hashtable<String, Stack<String>>();
    
    private static VariableManager instance;

    private static long startTime = System.currentTimeMillis();

    public static VariableManager getInstance(){
        if(instance == null){
            instance = new VariableManager();
        }
        return instance;
    }

    private VariableManager(){
    }

    public String dump(){
        ArrayList<String> vars = new ArrayList<String>();
        for(String key : variables.keySet()){
            vars.add(key + " = " + variables.get(key));
        }
        Collections.sort(vars);  
            
        return String.join("\n", vars);
    }

    public void setVariable(String name, String value){
        if(name.equals("line")){
            Interpreter.instructionPointer = Integer.parseInt(value);
            return;
        }

        if(value.matches(".*\\[.*\\]")){
            // Array
            String arrayName = value.substring(0, value.indexOf("["));
            String index = value.substring(value.indexOf("[") + 1, value.lastIndexOf("]"));
            try{
                index = parser.parse(index);
                if(variables.containsKey(arrayName + "[" + index + "]")){
                    value = variables.get(arrayName + "[" + index + "]");
                }
            }catch(Exception e){
                // Do nothing
            }

        }
        
        if(name.matches(".*\\[.*\\]")){
            // Array
            String arrayName = name.substring(0, name.indexOf("["));
            String index = name.substring(name.indexOf("[") + 1, name.lastIndexOf("]"));
            try{
                index = parser.parse(index);
                if(variables.containsKey(arrayName + "[" + index + "]")){
                    variables.put(arrayName + "[" + index + "]" , value.trim());
                    return;
                }
            }catch(Exception e){
                // Do nothing
            }

        }

        variables.put(name, value.trim());
    }

    public void createArray(String name, int size){
        for(int i = 0; i < size; i++){
            variables.put(name + "[" + i + "]", "0");
        }
    }

    public String getVariable(String name){

        // Special variables
        String specialArgument = "";
        String arrayName = "";
        if(name.matches(".*\\[.*\\]")){
            specialArgument = name.substring(name.indexOf("[") + 1, name.lastIndexOf("]"));
            arrayName = name.substring(0, name.indexOf("["));
        }

        if (name.equals("line")) {
            return Integer.toString(Interpreter.instructionPointer);
        } else if (name.equals("next")) {
            return Integer.toString(Interpreter.findNextLine());
        } else if (name.equals("prev")) {
            return Integer.toString(Interpreter.findPrevLine());
        } else if (name.equals("first")) {
            return Integer.toString(Interpreter.findFirstLine());
        } else if (name.equals("last")) {
            return Integer.toString(Interpreter.findLastLine());
        } else if (name.equals("count")) {
            return Integer.toString(Interpreter.lineCount());
        }else if(variables.containsKey(arrayName + "[0]")){
            // Array
            String index = specialArgument;
            try{
                index = parser.parse(index);
                return variables.get(arrayName + "[" + index + "]");
            }catch(Exception e){
                // Do nothing
            }
        }
        // else if(LibraryManager.functionExists(name.split("_")[0])){
        //     // External function
        //     String[] args = specialArgument.equals("") ? new String[0] : specialArgument.split("_");
            
            
            
        //     String result = LibraryManager.exec(name.split("_")[0], args);

        //     try {
        //         return Integer.toString(Integer.parseInt(result));
        //     } catch (NumberFormatException e) {
        //         return "\"" + result + "\"";
        //     }
        // }


        return variables.get(name);
    }

    public void clear(){
        variables.clear();
        setVariable("ret", "0");
        setVariable("import", "-1");
        setVariable("FOR_TO", "0");
        setVariable("FLAG_ELSE", "0");
        setVariable("FLAG_SKIP", "0");
        setVariable("FLAG_EXIT", "0");
        setVariable("FLAG_THEN", "0");

        variableStack.clear();

        LibraryManager.clear();
    }

    public int getVariableAsInt(String name){
        return Integer.parseInt(getVariable(name));
    }

    public void pushVariable(String name){
        if(!variableStack.containsKey(name)){
            variableStack.put(name, new Stack<String>());
        }
        variableStack.get(name).push(getVariable(name));
    }

    public void popVariable(String name){
        if(!variableStack.containsKey(name)){
            variableStack.put(name, new Stack<String>());
        }
        setVariable(name, variableStack.get(name).pop());
    }

    public String pollVariable(String name){
        if(!variableStack.containsKey(name)){
            variableStack.put(name, new Stack<String>());
        }
        return variableStack.get(name).peek();
    }


    public boolean isStandard(String name){
        for(String s : standard){
            if(s.equals(name)){
                return true;
            }
        }
        return false;
    }

    public String getStandard(String name, String[] args){
        if (name.equals("rnd")) {
            int max = Integer.parseInt(parser.parse(args[0]));
            return Integer.toString((int) (Math.random() * max));
        } else if (name.equals("time")) {
            // Return the current time in seconds
            return Long.toString(System.currentTimeMillis() / 1000);
        } else if (name.equals("milli")) {
            // Return the current time in milliseconds since the program started
            return Long.toString(System.currentTimeMillis() - startTime);
        } else if (name.equals("neg")) {
            int value = Integer.parseInt(parser.parse(args[0]));
            return Integer.toString(-value);
        } else if (name.equals("abs")) {
            int value = Integer.parseInt(parser.parse(args[0]));
            return Integer.toString(Math.abs(value));
        } else if (name.equals("sqrt")) {
            int value = Integer.parseInt(parser.parse(args[0]));
            return Long.toString(Math.round(Math.sqrt(value)));
        } else if (name.equals("TAB")){
            int value = Integer.parseInt(parser.parse(args[0]));
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < value; i++){
                sb.append(" ");
            }
            return "\"" + sb + "\"";

        }
        return "";
    }
    
}
