package me.jmser.jbas.interpreter;

import java.util.ArrayList;

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

        expression = expression.trim();
        
        // If it is wholely parenthesized, remove the parentheses
        String wholelyParenthesized = "^\\((?:[^()]*|\\((?:[^()]*|\\([^()]*\\))*\\))*\\)$";
        while(expression.matches(wholelyParenthesized)) expression = expression.substring(1, expression.length() - 1);
        
        expression = parenthesize(expression);
        
        // Remove all whitespace
        String parseString = expression.replaceAll("\\s+", ""); 
        String[] splitString = parseString.split("(?=[-+%^*/()])|(?<=[-+%^*/()])");
        // Fully parenthesize the expression
        
        // Tokenize the string
        ArrayList<Token> tokens = new ArrayList<Token>();
        for(int i = 0; i < splitString.length; i++){
            tokens.add( new Token(splitString[i]));
        }

        
        // Create the expression tree
        ExpressionTree tree = new ExpressionTree();

        ExpressionTree current = tree;

        for(int i = 0; i < tokens.size(); i++){
            switch(tokens.get(i).type){
                case LEFT_PARENTHESIS:
                    current = current.setLeft(new ExpressionTree());
                    break;
                case RIGHT_PARENTHESIS:
                    current = current.getParent();
                    break;
                case NUMBER:
                case VARIABLE:
                    current.setToken(tokens.get(i));
                    current = current.getParent();
                    break;
                default:
                    current.setToken(tokens.get(i));
                    current = current.setRight(new ExpressionTree());
            }
        }

        return Integer.toString(tree.evaluate());


   }

   private static Boolean isFulllyParenthesized(String s){
        // Check if the expression is fully parenthesized
        int leftParenthesis = 0;
        int rightParenthesis = 0;
        int operators = 0;

        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) == '('){
                leftParenthesis++;
            }else if(s.charAt(i) == ')'){
                rightParenthesis++;
            }else if(s.charAt(i) == '+' || s.charAt(i) == '-' || s.charAt(i) == '*' || s.charAt(i) == '/' || s.charAt(i) == '^' || s.charAt(i) == '%'){
                operators++;
            }
        }

        return (leftParenthesis == rightParenthesis && operators <= leftParenthesis);
        


   }
    private static String[] splitExpression(String expression){
        ArrayList<String> list = new ArrayList<String>();
        String tempString = "";
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '+' || expression.charAt(i) == '-' || expression.charAt(i) == '*'
                    || expression.charAt(i) == '/' || expression.charAt(i) == '^' || expression.charAt(i) == '%') {
                list.add(tempString);
                list.add(expression.charAt(i) + "");
                tempString = "";
            } else if (expression.charAt(i) == '(') {
                // Dont split parenthesized expressions
                int count = 1;

                while (count != 0) {
                    tempString += expression.charAt(i);
                    i++;
                    if (expression.charAt(i) == '(') {
                        count++;
                    } else if (expression.charAt(i) == ')') {
                        count--;
                    }
                }
                tempString += expression.charAt(i);
            } else {
                tempString += expression.charAt(i);
            }
        }

        list.add(tempString);

        return list.toArray(new String[list.size()]);
    }

   private static String parenthesize(String s){
        

        // Fully parenthesize the expression

        // Split string at operators, but not inside parenthesis
        s = s.replaceAll("\\s+", "");
        String[] splitString = splitExpression(s);

        String result = "";

        // Are we already fully parenthesized?
        if (isFulllyParenthesized(s))
            return s;
        
        for(int i = 0; i < splitString.length; i++){
            if(splitString[i].contains("(")){
                if(isFulllyParenthesized(splitString[i])) result += splitString[i];
                else result +=  "(" + parenthesize(splitString[i].substring(1, splitString[i].length() - 1)) + ")";
            }else{
                result += splitString[i];
            }
        }
        
        if (isFulllyParenthesized(result))
            return result;

        splitString = splitExpression(result);

        // Find the operator with the highest precedence, and parenthesize the expression
        int highestPrecedenceIndex = 0;
        int precedenceValue = -1;
        for(int i = 0; i < splitString.length; i++){
            if(splitString[i].contains("(")) continue;
            if(splitString[i].contains("^")){
                highestPrecedenceIndex = i;
                break;
            }else if(splitString[i].contains("*") || splitString[i].contains("/") || splitString[i].contains("%")){
                highestPrecedenceIndex = i;
                precedenceValue = 1;
            }else if(splitString[i].contains("+") || splitString[i].contains("-")){
                if(precedenceValue < 0){
                    highestPrecedenceIndex = i;
                    precedenceValue = 0;
                }
            }
        }

        result = "";
        // Add the parenthesized expression to the result
        for(int i = 0; i < splitString.length; i++){
            if(i == highestPrecedenceIndex - 1){
                result += "(" + splitString[i] + splitString[i+1] + splitString[i + 2] + ")";
                i += 2;
            }else{
                result += splitString[i];
            }
        }

        // Check if the expression is fully parenthesized
        // If not, recursively call the function
        if(isFulllyParenthesized("(" + result + ")")) return "(" + result + ")";
        else if(isFulllyParenthesized(result)) return result;
        else return parenthesize(result);
   }

}
