package me.jmser.jbas.commands;

public enum Commands {
    PRINT, 
    EXIT, 
    EVAL, 
    LET, 
    LIST, 
    RUN, 
    LINE, 
    GOTO, 
    IF, 
    ELSE, 
    INPUT, 
    REM, 
    GOSUB, 
    RETURN, 
    CLEAR,
    LOAD, 
    SAVE, 
    DIM, 
    GORET, 
    UNKNOWN, 
    TAB,
    CLS,
    THEN,
    ENDIF,
    IMP,
    FUN, // Deprecated
    DEF,
    FOR,
    NEXT,
    DUMP,
    LIB,
    PUSH,
    POP,
    POKE;

    public static Commands fromString(String s){
        s = s.toUpperCase();
        switch(s){
            case "PRINT":
                return PRINT;
            case "QUIT":
            case "END":
            case "EXIT":
                return EXIT;
            case "EVAL":
                return EVAL;
            case "LET":
            case "FUN":
            case "DEF":
                return LET;
            case "LIST":
                return LIST;
            case "RUN":
                return RUN;
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
            case "TAB":
                return TAB;
            case "POKE":
                return POKE;
            case "CLS":
                return CLS;
            case "THEN":
                return THEN;
            case "IMP":
                return IMP;
            case "FOR":
                return FOR;
            case "NEXT":
                return NEXT;
            case "LIB":
                return LIB;
            case "ENDIF":
            case "ENDELSE":
                return ENDIF;
            case "DUMP":
                return DUMP;
            case "PUSH":
                return PUSH;
            case "POP": 
                return POP;
                
            
            default:
                if(s.matches("[0-9]+.*")){
                    return LINE;
                } else {
                    return UNKNOWN;
                }
        }
    }
}
