package me.jmser;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class Interpreter 
{
    public void run(){
        Scanner s = new Scanner(System.in);
        while(true){
            System.out.print("=> ");
            String input = s.nextLine();
            if(input.equals("exit")){
                break;
            }
        }



        s.close();
    }
}
