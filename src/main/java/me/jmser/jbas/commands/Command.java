package me.jmser.jbas.commands;

import me.jmser.jbas.interpreter.ExpressionParser;

public class Command {

    public Commands command;
    public String[] args;

    private static ExpressionParser parser = ExpressionParser.getInstance();

    private String parseString(String s){
        boolean inString = false;
        boolean inMath = true;

        String current = "";
        String result = "";

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"':
                    if (inString) {
                        inString = false;
                        result += current;
                        current = "";
                    } else if (inMath) {
                        inMath = false;
                        inString = true;

                        if (current.length() > 0) {
                            // Remove the + at the end of the string
                            current = current.trim();
                            current = current.substring(0, current.length() - 1);

                            // Parse the expression
                            result += (parser.parse(current));
                            current = "";
                        }

                    } else {
                        inString = true;
                    }
                    break;
                case ' ':
                    if (inString) {
                        current += c;
                    }
                    break;
                case '+':
                    if (inString || inMath) {
                        current += c;
                    } else {
                        inMath = true;
                    }
                    break;
                default:
                    current += c;
                    break;
            }
        }
        if (inMath) {
            current = current.trim();
            result += (parser.parse(current.trim()));
        }

        return result;
    }

    public Command(String s){
        String[] parts = s.split(" ");

        this.command = Commands.fromString(parts[0].toUpperCase());
        switch(this.command){
            case PRINT:
                String toParse = s.split(" ", 2)[1];
                String result = parseString(toParse);
                this.args = new String[1];
                this.args[0] = result;
                break;
            case LET:
                parts = s.split(" ", 2)[1].trim().split("=");
                this.args = new String[2];
                this.args[0] = parts[0].trim();
                if(parts[1].contains("\"")){
                    this.args[1] = parts[1].trim();
                    this.args[1] = "\""  + parseString(this.args[1]) + "\"";
                }else{
                    this.args[1] = (parser.parse(parts[1]));
                    try{ // This is hacky
                        Integer.parseInt(this.args[1]);
                    }catch(Exception e){
                        this.args[1] = "\"" + this.args[1] + "\"";
                    }
                }
                break;
            case GORET:
            case GOTO:
            case GOSUB:
                parts = s.split(" ", 2);
                this.args = new String[1];
                this.args[0] = parser.parse(parts[1].replace("\\s+", ""));
                break;
            case IF:
                parts = s.split(" ", 2);
                this.args = new String[3]; // Expression, operator, Expression
                
                if(parts[1].contains("==")){
                    this.args[0] = parser.parse(parts[1].split("==")[0]);
                    this.args[1] = "==";
                    this.args[2] = parser.parse(parts[1].split("==")[1]);
                }else if(parts[1].contains("!=")){
                    this.args[0] = parser.parse(parts[1].split("!=")[0]);
                    this.args[1] = "!=";
                    this.args[2] = parser.parse(parts[1].split("!=")[1]);
                }else if(parts[1].contains("<=")){
                    this.args[0] = parser.parse(parts[1].split("<=")[0]);
                    this.args[1] = "<=";
                    this.args[2] = parser.parse(parts[1].split("<=")[1]);
                }else if(parts[1].contains(">=")){
                    this.args[0] = parser.parse(parts[1].split(">=")[0]);
                    this.args[1] = ">=";
                    this.args[2] = parser.parse(parts[1].split(">=")[1]);
                }else if(parts[1].contains("<")){
                    this.args[0] = parser.parse(parts[1].split("<")[0]);
                    this.args[1] = "<";
                    this.args[2] = parser.parse(parts[1].split("<")[1]);
                }else if(parts[1].contains(">")){
                    this.args[0] = parser.parse(parts[1].split(">")[0]);
                    this.args[1] = ">";
                    this.args[2] = parser.parse(parts[1].split(">")[1]);
                }
                break;
            case INPUT:
                parts = s.split(" ", 2);
                this.args = new String[1];
                this.args[0] = parts[1].replace("\\s+", "");
                break;
            case LOAD:
            case SAVE:
                parts = s.split(" ", 2);
                this.args = new String[1];
                this.args[0] = parts[1].replace("\\s+", "");   
                break;
            case DIM:
                parts = s.replace("\\s+", " ").split(" ", 3);
                this.args = new String[2];
                this.args[0] = parts[1]; // Variable name
                this.args[1] = parts[2]; // Array size
                break;
            case UNKNOWN:
                this.args = new String[1];
                this.args[0] = s;
                break;
            case TAB:
                this.args = new String[1];
                this.args[0] = parser.parse(s.split(" ", 2)[1]);
                break;
            case POKE:
                parts = s.replace("\\s+", " ").split(" ", 2)[1].split(",");
                this.args = new String[2];
                this.args[0] = parser.parse(parts[0]); // Address
                this.args[1] = parser.parse(parts[1]);  // Value
                break;
            case FOR:
                parts = s.replace("\\s+", " ").split(" ", 2)[1].split("=");
                this.args = new String[3];
                this.args[0] = parts[0].trim(); // Variable name
                this.args[1] = parser.parse(parts[1].split("TO")[0].trim()); // Start value
                this.args[2] = parser.parse(parts[1].split("TO")[1].trim()); // End value
                break;
            case NEXT:
                parts = s.replace("\\s+", " ").split(" ", 2);
                this.args = new String[1];
                this.args[0] = parts[1].trim(); // Variable name
                break;
            default:
                this.args = new String[parts.length - 1];
                for(int i = 1; i < parts.length; i++){
                    this.args[i - 1] = parts[i];
                }
                break;
        }

    }
}
