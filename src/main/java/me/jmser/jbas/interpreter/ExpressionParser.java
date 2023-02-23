package me.jmser.jbas.interpreter;

public class ExpressionParser {
    private static ExpressionParser instance;
    private static VariableManager variableManager = VariableManager.getInstance();

    private ExpressionParser(){};
   
    public static ExpressionParser getInstance(){
        if(instance == null){
            instance = new ExpressionParser();
        }
        return instance;
    }



    public String parse(String expression){
        if(expression.equals("")) return "";
        if(variableManager.getVariable(expression.trim()) != null && variableManager.getVariable(expression.trim()).contains("\"")){
            String value = variableManager.getVariable(expression.trim());
            if(value.equals("")) return "";
            value = value.substring(1, value.length() - 1);
            return value;
        }else if(expression.contains("\"")){
            expression = expression.trim();
            return expression.substring(1, expression.length() - 1);
        }

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
