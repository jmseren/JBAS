package me.jmser;

public class Command {
    public Commands command;
    public String[] args;

    private static ExpressionParser parser = ExpressionParser.getInstance();

    public Command(String s){
        String[] parts = s.split(" ");

        this.command = Commands.fromString(parts[0].toUpperCase());
        

        if(this.command == Commands.PRINT){
            String toParse = s.split(" ", 2)[1];
            
            boolean inString = false;
            boolean inMath = true;

            String current = "";
            String result = "";

            for(int i = 0; i < toParse.length(); i++){
                char c = toParse.charAt(i);
                switch(c){
                    case '"':
                        if(inString){
                            inString = false;
                            result += current;
                            current = "";
                        }else if(inMath){
                            inMath = false;
                            inString = true;

                            if(current.length() > 0){
                                // Remove the + at the end of the string
                                current = current.trim();
                                current = current.substring(0, current.length() - 1);
                            

                                // Parse the expression
                                result += (parser.parse(current));
                                current = "";
                            }

                        }else{
                            inString = true;
                        }
                        break;
                    case ' ':
                        if(inString){
                            current += c;
                        }
                        break;
                    case '+':
                        if(inString || inMath){
                            current += c;
                        }else{
                            inMath = true;
                        }
                        break;
                    default:
                        current += c;
                        break;
                }
            }
            if(inMath) {
                current = current.trim();
                result += (parser.parse(current));
            }

            this.args = new String[1];
            this.args[0] = result;
        }else if(this.command == Commands.EVAL){
            parts = s.split(" ", 2);
            this.args = new String[1];
            this.args[0] = (parser.parse(parts[1]));
        }else if(this.command == Commands.LET){
            parts = s.split(" ", 2)[1].trim().split("=");
            this.args = new String[2];
            this.args[0] = parts[0].trim();
            this.args[1] = (parser.parse(parts[1]));
        }else if(this.command == Commands.GOTO){
            parts = s.split(" ", 2);
            this.args = new String[1];
            this.args[0] = parser.parse(parts[1].replace("\\s+", ""));
        }else if(this.command == Commands.IF){
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
        }else if(this.command == Commands.INPUT){
            parts = s.split(" ", 2);
            this.args = new String[1];
            this.args[0] = parts[1].replace("\\s+", "");
        }else{
            this.args = new String[parts.length - 1];
            for(int i = 1; i < parts.length; i++){
                this.args[i - 1] = parts[i];
            }
        }

    }
}
