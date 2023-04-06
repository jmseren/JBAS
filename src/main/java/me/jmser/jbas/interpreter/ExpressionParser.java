package me.jmser.jbas.interpreter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.jmser.jbas.commands.LibraryManager;

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
        String value = variableManager.getVariable(expression.trim());
        if(value != null && value.contains("\"")){
            if(value.equals("")) return "";
            value = value.substring(1, value.length() - 1);
            return value;
        }else if(expression.matches("\".*\"")){
            expression = expression.trim();
            return expression.substring(1, expression.length() - 1);
        }else if(value != null && value.contains("@")){
            return value;
        }
        expression = expression.trim();
        
        // Find any function and replace it with its value
        String functionRegex = "[A-z1-9]+\\([^\\)]*\\)"; // Matches a function of the form "functionName(argument1, argument2, ...)"
        Pattern functionPattern = Pattern.compile(functionRegex, Pattern.CASE_INSENSITIVE);
        Matcher functionMatcher = functionPattern.matcher(expression);
        while(functionMatcher.find()){
            String function = functionMatcher.group();
            String functionName = function.substring(0, function.indexOf("("));
            String functionArguments = function.substring(function.indexOf("(") + 1, function.lastIndexOf(")"));
            String[] arguments = functionArguments.split(",");
            for(int i = 0; i < arguments.length; i++){
                arguments[i] = parse(arguments[i]);
            }

            // Check if the function is part of the built-in library
            if(variableManager.isStandard(functionName)){
                String functionValue = variableManager.getStandard(functionName, arguments);
                expression = expression.replace(function, functionValue);
                if(functionValue.equals("")) return "";
                if(functionValue.contains("\"")){
                    functionValue = functionValue.substring(1, functionValue.length() - 1);
                    return functionValue;
                }
                
            }else{
                String functionValue = LibraryManager.exec(functionName, arguments);
                expression = expression.replace(function, functionValue);
                if (functionValue.equals(""))
                    return "";
                if (functionValue.contains("\"")) {
                    functionValue = functionValue.substring(1, functionValue.length() - 1);
                    return functionValue;
                }
            }




        }

        if (expression.contains("\"")) {
            expression = expression.substring(1, expression.length() - 1);
            return expression;
        }

        
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
