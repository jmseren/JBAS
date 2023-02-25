package me.jmser.jbas.interfaces;

import me.jmser.jbas.interpreter.Interpreter;

public class GUI implements JBasicInterface {
    Screen window;

    public String line = "";

    private Interpreter interpreter;

    private boolean inputMode = false;

    public synchronized void newLine(String line){
        this.line = line;
    }

    public GUI(){
        this.window = new Screen(this);
        this.interpreter = new Interpreter();
        this.interpreter.hook(this);
    }

    @Override public void tab(int n){
        window.tab(n);
    }

    @Override
    public void print(String output) {
        window.printStr(output);
    }



    @Override
    public void println(String output) {
        window.printlnStr(output);
    }



    @Override
    public void println() {
        window.printlnStr("");
    }



    @Override
    public String getLine() {
        inputMode = true;
        line = "";
        while(true){
            try{ Thread.sleep(50); }catch(Exception e){}
            if(!line.equals("")){
                String temp = line;
                line = "";
                inputMode = false;
                return temp;
            }
        }
    }



    @Override
    public void run(){
        window.noStroke();
        window.fill(255);
        while(true){
            if(inputMode) continue;
            try{ Thread.sleep(50); }catch(Exception e){}
            try {
                if (!interpreter.interpret(line))
                    break;
            } catch (Exception e) {
                // Better error handling will be added later
                window.printStr("Unknown variable or command in: " + line);
                if (Interpreter.instructionPointer >= 0)
                    window.printStr("\t\tAround Line: " + Interpreter.instructionPointer);
                window.printStr("\n");
            }

            line = "";
        }

        System.exit(0);

    }

    public void putPixel(int x, int y, int color){
        window.putPixel(x, y, color);
    }

    public void clear() {
        window.clear();
    }

    
}
