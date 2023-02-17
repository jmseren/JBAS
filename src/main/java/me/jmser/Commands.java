package me.jmser;

public enum Commands {
    PRINT, EXIT, EVAL, LET, LIST, RUN, LINE, GOTO, IF, ELSE, INPUT;

    public static Commands fromString(String s){
        s = s.toUpperCase();
        switch(s){
            case "PRINT":
                return PRINT;
            case "EXIT":
                return EXIT;
            case "EVAL":
                return EVAL;
            case "LET":
                return LET;
            case "LIST":
                return LIST;
            case "RUN":
                return RUN;
            case "THEN":
            case "GOTO":
                return GOTO;
            case "IF":
                return IF;
            case "ELSE":
                return ELSE;
            case "INPUT":
                return INPUT;
            
            default:
                if(s.matches("[0-9]+.*")){
                    return LINE;
                } else {
                    return null;
                }
        }
    }
}
