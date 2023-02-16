package me.jmser;

public enum Commands {
    PRINT, EXIT;

    public static Commands fromString(String s){
        s = s.toUpperCase();
        switch(s){
            case "PRINT":
                return PRINT;
            case "EXIT":
                return EXIT;
            default:
                return null;
        }
    }
}
