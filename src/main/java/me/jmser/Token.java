package me.jmser;

public class Token {
    private static VariableManager variableManager = VariableManager.getInstance();
    
    public TokenType type;
    public int value;

    public Token(String s){
        this.type = TokenType.fromString(s);
        if(this.type == TokenType.NUMBER){
            this.value = Integer.parseInt(s);
        }else if(this.type == TokenType.VARIABLE){
            if(variableManager.getVariable(s).equals("")){
            }else{
                this.value = Integer.parseInt(variableManager.getVariable(s));

            }
        }
    }

    public String toString(){
        if(this.type == TokenType.NUMBER){
            return Integer.toString(this.value);
        } else {
            return this.type.toString();
        }
    }
}
