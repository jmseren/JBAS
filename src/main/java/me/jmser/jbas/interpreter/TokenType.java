package me.jmser.jbas.interpreter;

public enum TokenType {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    MODULO,
    NUMBER,
    VARIABLE,
    EXPONENT,
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
            case "%":
                return MODULO;
            case "^":
                return EXPONENT;
            case "(":
                return LEFT_PARENTHESIS;
            case ")":
                return RIGHT_PARENTHESIS;
            default:
                if(s.matches("[0-9]+")){
                    return NUMBER;
                } else {
                    return VARIABLE;
                }
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
            case MODULO:
                return "%";
            case LEFT_PARENTHESIS:
                return "(";
            case RIGHT_PARENTHESIS:
                return ")";
            case NUMBER:
                return "NUMBER";
            case VARIABLE:
                return "VARIABLE";
            case EXPONENT:
                return "^";
            default:
                return "";
        }
    }

    public int getPrecedence(){
        switch(this){
            case ADD:
            case SUBTRACT:
                return 1;
            case MULTIPLY:
            case DIVIDE:
            case MODULO:
                return 2;
            case EXPONENT:
                return 3;
            default:
                return 0;
        }
    }
}
