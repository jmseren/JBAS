package me.jmser;

public class ExpressionParser {
   public ExpressionParser(){};


   public int parse(String expression){
        // Remove all whitespace
        expression = "(" + expression + ")"; // Add parenthesis to the beginning and end of the expression
        String parseString = expression.replaceAll("\\s+", ""); 
        String[] splitString = parseString.split("(?=[-+*/()])|(?<=[-+*/()])");
        

        // Split the string into an array of characters so that we
        // can tokenize the expression.

        Token[] tokens = new Token[splitString.length];
        for(int i = 0; i < splitString.length; i++){
            tokens[i] = new Token(splitString[i]);
        }
        
        // Create the expression tree
        ExpressionTree tree = new ExpressionTree();
        ExpressionTree current = tree;

        for(int i = 0; i < tokens.length; i++){
            System.out.println("Adding: " + tokens[i] + " to the tree.");
            switch(tokens[i].type){
                case LEFT_PARENTHESIS:
                    current = current.setLeft(new ExpressionTree());
                    break;
                case RIGHT_PARENTHESIS:
                    current = current.getParent();
                    break;
                case NUMBER:
                    current.setToken(tokens[i]);
                    current = current.getParent();
                    break;
                default:
                    current.setToken(tokens[i]);
                    current = current.setRight(new ExpressionTree());
            }
        }

        return tree.evaluate();


   }


}
