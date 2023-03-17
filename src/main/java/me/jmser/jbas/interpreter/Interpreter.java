package me.jmser.jbas.interpreter;

import java.util.*;

import me.jmser.jbas.commands.Command;
import me.jmser.jbas.commands.LibraryManager;
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
        iface.println("\nJBAS 2.0 (c) 2023 JMSER\n\n");
    }

    public Interpreter(){
        variableManager.setVariable("FLAG_EXIT", "1");
        variableManager.setVariable("FLAG_THEN", "0");
    }

    public boolean interpret(String input){
        input = input.trim();
        if(input.equals("")) return true;
        ArrayList<String> cmds = multiCommands(input);
        if(cmds.size() > 1){
            for(String cmd : cmds){
                if(!interpret(cmd)) return false;
            }
            return true;
        }

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
                variableManager.pushVariable("ret");
                variableManager.setVariable("ret", Integer.toString(instructionPointer));
                variableManager.pushVariable("FLAG_ELSE");
                variableManager.pushVariable("FLAG_SKIP");
                variableManager.pushVariable("FLAG_THEN");
                instructionPointer = Integer.parseInt(c.args[0]) - 1;
                break;
            case RETURN:
                instructionPointer = Integer.parseInt(variableManager.getVariable("ret"));
                variableManager.popVariable("ret");
                variableManager.popVariable("FLAG_ELSE");
                variableManager.popVariable("FLAG_SKIP");
                variableManager.popVariable("FLAG_THEN");
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
                variableManager.setVariable("FLAG_THEN", "0");
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
                if(!inputString.matches("[0-9]+")) inputString = "\"" + inputString + "\"";
                variableManager.setVariable(c.args[0], inputString);
                break;
            case REM:
                break;
            case CLEAR:
                lines.clear();
                variableManager.clear();
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
                    iface.println("Error loading file: " + e.getMessage());
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
                    iface.println("Error saving file: " + e.getMessage());
                }
                break;
            case TAB:
                iface.tab(Integer.parseInt(c.args[0]));
                break;
            case POKE:
                int address = Integer.parseInt(c.args[0]);
                if(address < (80*50) && address >= 0){
                    // Screen memory
                    int x = address % 80;
                    int y = address / 80;

                    int color = Integer.parseInt(c.args[1]);
                    if(color < 0 || color > 15) break;
                    iface.putPixel(x, y, color);
                }
                break;
            case CLS:
                iface.clear();
                break;
            case THEN:
                variableManager.setVariable("FLAG_THEN", "1");
                break;
            case ENDIF:
                variableManager.setVariable("FLAG_THEN", "0");
                break;
            case FOR:
                variableManager.setVariable(c.args[0], c.args[1]);
                variableManager.pushVariable("FLAG_ELSE");
                variableManager.pushVariable("FLAG_SKIP");
                variableManager.pushVariable("FOR_TO");
                variableManager.pushVariable("FLAG_THEN");
                variableManager.pushVariable("ret");
                variableManager.setVariable("ret", Integer.toString(instructionPointer + 1));
                variableManager.setVariable("FOR_TO", c.args[2]);
                break;
            case NEXT:
                int next = Integer.parseInt(variableManager.getVariable(c.args[0]));
                next++;
                variableManager.setVariable(c.args[0], Integer.toString(next));
                if(next > Integer.parseInt(variableManager.getVariable("FOR_TO"))){
                    variableManager.popVariable("ret");
                    variableManager.popVariable("FOR_TO");
                    variableManager.popVariable("FLAG_THEN");
                    variableManager.popVariable("FLAG_SKIP");
                    variableManager.popVariable("FLAG_ELSE");

                }else{
                    instructionPointer = Integer.parseInt(variableManager.getVariable("ret"));
                }
                break;
            case PUSH:
                // Push a variable onto the stack
                variableManager.pushVariable(c.args[0]);
                break;
            case POP:
                // Pop a variable off the stack
                variableManager.popVariable(c.args[0]);
                break;
            case DUMP:
                // Dump the contents of all variables to the screen
                iface.println(variableManager.dump());
                break;
            case LIB:
                // Load a library file (*.jlib)
                // Libraries are nothing more than a JSON file with a list of commands, and an
                // accompanying executable file. These are packaged together in a .jlib file.
                // The executable file is loaded into memory, and the commands are added to the
                // command list.
                LibraryManager.load(c.args[0]);
                break;
            case IMP:
                // Import a file by appending it to the current file. 
                // Runs the beginning of the file to set up variables, then 
                // returns to the beginning of the current file.
                
                // Push the current instruction pointer onto the stack
                variableManager.pushVariable("ret");
                variableManager.setVariable("ret", Integer.toString(instructionPointer));
                variableManager.pushVariable("FLAG_ELSE");
                variableManager.pushVariable("FLAG_SKIP");
                variableManager.pushVariable("FLAG_THEN");


                
                // Get the last line number of the current file, rounded up to the nearest 100 and adding 1000
                int lastLine = lines.lastKey();
                lastLine = ((lastLine / 100 + 1) * 100) + 1000;

                // Set the import variable to the last line number (first line of the imported file)
                // Used to clear the imported file from memory after execution
                if(variableManager.getVariable("import").equals("-1")){
                    variableManager.setVariable("import", Integer.toString(lastLine));
                }


                try {
                    BufferedReader reader = new BufferedReader(new FileReader(c.args[0]));
                    String line;
                    while((line = reader.readLine()) != null){
                        if(!line.matches("[0-9]+ .*")) continue; // Skip lines that don't start with a number.
                        String[] parts = line.split(" ", 2);
                        if(parts[1].trim().toUpperCase().startsWith("DEF") || parts[1].trim().toUpperCase().startsWith("FUN") ){ // "FUN" is deprecated, and will be removed in a future version. Use "DEF" instead.
                            // This is a subroutine definition, so we have to change the line number
                            // to be the last line number of the current file, plus the line number
                            // of the subroutine definition.

                            // Get the line number of the subroutine definition and add it to the last line number
                            String funLineStr = parts[1].split("=", 2)[1].trim();
                            int funLine = Integer.parseInt(funLineStr);
                            funLine += lastLine;
                            parts[1] = parts[1].replace(funLineStr, Integer.toString(funLine));

                        }else if(parts[1].trim().toUpperCase().startsWith("GOTO")){
                            // This is a goto statement, so we have to change the line number as well.
                            String gotoLineStr = parts[1].split(" ", 2)[1].trim();
                            int gotoLine = Integer.parseInt(gotoLineStr);
                            gotoLine += lastLine;
                            parts[1] = parts[1].replace(gotoLineStr, Integer.toString(gotoLine));
                        }
                        lines.put(lastLine + Integer.parseInt(parts[0]), parts[1]);
                    }
                    reader.close();

                    // Set the instruction pointer to the beginning of the imported file
                    instructionPointer = lastLine;

                } catch (Exception e) {
                    iface.println("Error loading file: " + e.getMessage());
                }
                break;
            default:
                iface.println("Unknown command or variable: " + c.args[0]);
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
                if(lines.get(instructionPointer).toUpperCase().matches("THEN.*")){
                    variableManager.setVariable("FLAG_THEN", "1");
                }else if(lines.get(instructionPointer).toUpperCase().matches("ENDIF.*") || lines.get(instructionPointer).toUpperCase().matches("ELSE.*") || lines.get(instructionPointer).toUpperCase().matches("ENDELSE.*")){
                    variableManager.setVariable("FLAG_THEN", "0");
                }

                if(variableManager.getVariable("FLAG_SKIP").equals("1")){
                    if(!variableManager.getVariable("FLAG_THEN").equals("1")) {
                        if(instructionPointer != lines.lastKey() && (lines.get(instructionPointer).toUpperCase().matches("IF .*"))){
                            variableManager.setVariable("FLAG_SKIP", "1");
                        }else{
                            variableManager.setVariable("FLAG_SKIP", "0");
                        }
                    }
                }else{
                    interpret(lines.get(instructionPointer));
                    
                }
            }
            if(instructionPointer == lines.lastKey()) break;
            instructionPointer = lines.higherKey(instructionPointer);
        }
        if(!variableManager.getVariable("import").equals("-1")){
            // Clear the imported file from memory
            int importLine = Integer.parseInt(variableManager.getVariable("import"));
            while(lines.lastKey() >= importLine){
                lines.remove(lines.lastKey());
            }
        }


        // variableManager.clear();
        variableManager.setVariable("FLAG_EXIT", "1");
    }

    private ArrayList<String> multiCommands(String line){
        boolean inQuotes = false;

        ArrayList<String> commands = new ArrayList<String>();

        String command = "";

        for(int i = 0; i < line.length(); i++){
            if(line.charAt(i) == '"'){
                inQuotes = !inQuotes;
            }else if(line.charAt(i) == ';' && !inQuotes){
                commands.add(command);
                command = "";
                continue;
            }
            command += line.charAt(i);
        }
        
        commands.add(command);
        return commands;
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
