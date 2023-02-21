package me.jmser;

import java.util.Hashtable;
import java.util.Stack;

public class VariableManager {
    private static ExpressionParser parser = ExpressionParser.getInstance();
    
    private static Hashtable<String, String> variables = new Hashtable<String, String>();

    private static Hashtable<String, Stack<String>> variableStack = new Hashtable<String, Stack<String>>();
    
    private static VariableManager instance;

    public static VariableManager getInstance(){
        if(instance == null){
            instance = new VariableManager();
        }
        return instance;
    }

    private VariableManager(){
    }

    public void setVariable(String name, String value){
        if(name.equals("line")){
            Interpreter.instructionPointer = Integer.parseInt(value);
            return;
        }
        if(name.contains("_")){
            String arrayName = name.substring(0, name.lastIndexOf("_"));
            String index = name.substring(name.lastIndexOf("_") + 1);

            if(variables.containsKey(arrayName + "_0")){
                // Array
                try{
                    index = parser.parse(index);
                    if(variables.containsKey(arrayName + "_" + index)){
                        variables.put(arrayName + "_" + index, value.trim());
                        return;
                    }
                }catch(Exception e){
                    // Do nothing
                }
                
            }
        }

        variables.put(name, value.trim());
    }

    public void createArray(String name, int size){
        for(int i = 0; i < size; i++){
            variables.put(name + "_" + i, "0");
        }
    }

    public String getVariable(String name){

        // Special variables
        String specialArgument = "";
        String arrayName = "";
        if(name.contains("_")){
            specialArgument = name.split("_")[1];
            arrayName = name.substring(0, name.lastIndexOf("_"));
        }

        if(name.startsWith("rnd_")){
            int max = Integer.parseInt(parser.parse(specialArgument));
            return Integer.toString((int)(Math.random() * max));
        }else if(name.equals("time")){
            // Return the current time in seconds
            return Long.toString(System.currentTimeMillis() / 1000);
        }else if(name.startsWith("neg_")){
            int value = Integer.parseInt(parser.parse(specialArgument));
            return Integer.toString(-value);
        }else if(name.startsWith("abs_")){
            int value = Integer.parseInt(parser.parse(specialArgument));
            return Integer.toString(Math.abs(value));
        }else if(name.startsWith("sqrt_")){
            int value = Integer.parseInt(parser.parse(specialArgument));
            return Long.toString(Math.round(Math.sqrt(value)));
        }else if(name.equals("line")){
            return Integer.toString(Interpreter.instructionPointer);
        }else if(name.equals("next")){
            return Integer.toString(Interpreter.findNextLine());
        }else if(name.equals("prev")){
            return Integer.toString(Interpreter.findPrevLine());
        }else if(name.equals("first")){
            return Integer.toString(Interpreter.findFirstLine());
        }else if(name.equals("last")){
            return Integer.toString(Interpreter.findLastLine());
        }else if(name.equals("count")){
            return Integer.toString(Interpreter.lineCount());
        }else if(variables.containsKey(arrayName + "_0")){
            // Array
            String index = name.substring(name.lastIndexOf("_") + 1);
            try{
                index = parser.parse(index);
                return variables.get(arrayName + "_" + index);
            }catch(Exception e){
                // Do nothing
            }
        }


        return variables.get(name);
    }

    public void clear(){
        variables.clear();
        setVariable("FLAG_ELSE", "0");
        setVariable("FLAG_SKIP", "0");
        setVariable("FLAG_EXIT", "0");
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
    
}
