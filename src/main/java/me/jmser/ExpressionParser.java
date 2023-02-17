package me.jmser;

public class ExpressionParser {
    private static ExpressionParser instance = new ExpressionParser();
    private ExpressionParser(){};
   
    public static ExpressionParser getInstance(){
        return instance;
    }


   public String parse(String expression){


        expression = "(" + expression + ")"; // Add parenthesis to the beginning and end of the expression
       
        // Remove all whitespace
        String parseString = expression.replaceAll("\\s+", ""); 
        String[] splitString = parseString.split("(?=[-+*/()])|(?<=[-+*/()])");
        
        // Tokenize the string
        Token[] tokens = new Token[splitString.length];
        for(int i = 0; i < splitString.length; i++){
            tokens[i] = new Token(splitString[i]);
        }
        
        // Create the expression tree
        ExpressionTree tree = new ExpressionTree();
        ExpressionTree current = tree;

        for(int i = 0; i < tokens.length; i++){
            switch(tokens[i].type){
                case LEFT_PARENTHESIS:
                    current = current.setLeft(new ExpressionTree());
                    break;
                case RIGHT_PARENTHESIS:
                    current = current.getParent();
                    break;
                case NUMBER:
                case VARIABLE:
                    current.setToken(tokens[i]);
                    current = current.getParent();
                    break;
                default:
                    current.setToken(tokens[i]);
                    current = current.setRight(new ExpressionTree());
            }
        }

        return Integer.toString(tree.evaluate());


   }


}
