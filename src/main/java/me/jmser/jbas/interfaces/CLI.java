package me.jmser.jbas.interfaces;

import me.jmser.jbas.interpreter.Interpreter;

import java.util.*;


public class CLI implements JBasicInterface {

    public void print(String output){
        System.out.print(output);
    }
    public void println(String output){
        System.out.println(output);
    }
    public void println(){
        System.out.println();
    }

    public void tab(int n){
        for(int i = 0; i < n; i++){
            System.out.print(" ");
        }
    }

    public String getLine(){
        return System.console().readLine();
    }

    public void clear(){
        System.console().flush();
    }

    public void run(){
        Interpreter interpreter = new Interpreter();
        interpreter.hook(this);

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
                System.out.print("Unknown variable or command in: " + input);
                if(Interpreter.instructionPointer >= 0) System.out.print("\t\tAround Line: " + Interpreter.instructionPointer);
                System.out.print("\n");
            }

            // Prompt
            System.out.print("=> ");

            input = scanner.nextLine();
        }
        scanner.close();

    }
}
