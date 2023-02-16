package me.jmser;

public enum TokenType {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    NUMBER,
    LEFT_PARENTHESIS,
    RIGHT_PARENTHESIS;

    public static TokenType fromString(String s){
        switch(s){
            case "+":
                return ADD;
            case "-":
                return SUBTRACT;
            case "*":
                return MULTIPLY;
            case "/":
                return DIVIDE;
            case "(":
                return LEFT_PARENTHESIS;
            case ")":
                return RIGHT_PARENTHESIS;
            default:
                     
                return NUMBER;
        }
    }

    public String toString(){
        switch(this){
            case ADD:
                return "+";
            case SUBTRACT:
                return "-";
            case MULTIPLY:
                return "*";
            case DIVIDE:
                return "/";
            case LEFT_PARENTHESIS:
                return "(";
            case RIGHT_PARENTHESIS:
                return ")";
            case NUMBER:
                return "NUMBER";
            default:
                return "";
        }
    }
}
