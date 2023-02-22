package me.jmser;

public enum Commands {
    PRINT, EXIT, EVAL, LET, LIST, RUN, LINE, GOTO, IF, ELSE, INPUT, REM, GOSUB, RETURN, CLEAR, LOAD, SAVE, DIM, GORET, UNKNOWN;

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
            case "REM":
                return REM;
            case "GOSUB":
                return GOSUB;
            case "RETURN":
                return RETURN;
            case "CLEAR":
                return CLEAR;
            case "LOAD":
                return LOAD;
            case "SAVE":
                return SAVE;
            case "DIM":
                return DIM;
            case "GORET":
                return GORET;
                
            
            default:
                if(s.matches("[0-9]+.*")){
                    return LINE;
                } else {
                    return UNKNOWN;
                }
        }
    }
}
