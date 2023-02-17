package me.jmser;

import java.util.*;

public class CLI {
    public static void main(String[] args){
        Interpreter interpreter = new Interpreter();
        System.out.println("JBasic 1.0 (c) 2023 JMSER\n\n");
        Scanner scanner = new Scanner(System.in);

        // Inital prompt
        System.out.print("=> ");
        
        String input = scanner.nextLine();

        while(true){
            try {
                if(!interpreter.interpret(input)) break;
            }catch(Exception e){
                // Better error handling will be added later
                System.out.println("Unknown variable or command in: " + input + " (" + e.getMessage() + ")");
            }

            // Prompt
            System.out.print("=> ");

            input = scanner.nextLine();
        }
        scanner.close();

    }
}
