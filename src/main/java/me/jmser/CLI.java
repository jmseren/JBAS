package me.jmser;

import java.util.*;

public class CLI {
    public static void main(String[] args){
        Interpreter interpreter = new Interpreter();
        System.out.println("JBasic 0.1 (c) 2023 JMSER");
        Scanner scanner = new Scanner(System.in);
        System.out.print("=> ");
        String input = scanner.nextLine();
        while(true){
            try {
                if(!interpreter.interpret(input)) break;
            }catch(Exception e){
                System.out.println("Unknown variable or command in: " + input);
            }
            System.out.print("=> ");
            input = scanner.nextLine();
        }
        scanner.close();

    }
}
