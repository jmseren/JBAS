package me.jmser;

public class Token {
    public TokenType type;
    public int value;

    public Token(String s){
        this.type = TokenType.fromString(s);
        if(this.type == TokenType.NUMBER){
            this.value = Integer.parseInt(s);
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
