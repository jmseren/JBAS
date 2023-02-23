package me.jmser.jbas.interfaces;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

public class Screen extends PApplet {
    private final char cursor = '\u2588';

    private int[] cursorPosition = {0, 0};

    private ArrayList<String> lines = new ArrayList<String>();

    private GUI jbasic;

    boolean programMode = false;

    float size = 4.0f;

    int cursorBlink = 60;
    boolean cursorOn = true;

    public void tab(int n){
        cursorPosition[0] = n;
        
        if(cursorPosition[0] > lines.get(cursorPosition[1]).length()){
            for(int i = lines.get(cursorPosition[1]).length(); i < cursorPosition[0]; i++){
                lines.set(cursorPosition[1], lines.get(cursorPosition[1]) + " ");
            }
        }
    }

    public Screen(GUI jbasic){
        this.jbasic = jbasic;
        runSketch(new String[] { "JBas V1.1" }, this);
    }

    public void settings(){
        size((int)(320 * size), (int)(200 * size));
       
        for(int i = 0; i < 25; i++){
            lines.add("");
        }

    }
    

    public void setup() {
        frameRate(60);
        PFont font = createFont("c64.ttf", 8 * size);
        textFont(font);
        textSize(8 * size);

    }

    private void printChar(char c, int x, int y){
        text(""+c, (x * (8 * size)), (y * (8 * size)) + (7 * size));
    }

    private void nextLine(){
        cursorPosition[1]++;
        cursorPosition[0] = 0;
        if(cursorPosition[1] >= lines.size()){
            lines.remove(0);
            lines.add("");
            cursorPosition[1]--;
        }
    }


    public void draw(){
        background(0);
        if(cursorBlink == 0){
            cursorOn = !cursorOn;
            cursorBlink = 40;
        }else{
            cursorBlink--;
        }

        for(int i = 0; i < lines.size(); i++){
            for(int j = 0; j < lines.get(i).length(); j++){
                printChar(lines.get(i).charAt(j), j, i);
            }
        }
        if(cursorOn) printChar(cursor, cursorPosition[0], cursorPosition[1]);

    }

    public void keyPressed(){
        if(keyCode == PConstants.SHIFT && (key == '￿' || key == '\u000F')){
            return; // Ignore shift key... fails on shift + shift
        }

        if(keyCode == PConstants.ENTER){
            synchronized(jbasic){
                jbasic.line = lines.get(cursorPosition[1]);
            }
            nextLine();
        }else if(keyCode == PConstants.BACKSPACE){
            if(cursorPosition[0] > lines.get(cursorPosition[1]).length()){
                cursorPosition[0]--;
                return;
            }
            if (cursorPosition[0] == 0) {
                if(cursorPosition[1] == 0) return;
                cursorPosition[1]--;
                cursorPosition[0] = lines.get(cursorPosition[1]).length();
                return;
            }

            lines.set(cursorPosition[1], lines.get(cursorPosition[1]).substring(0, cursorPosition[0] - 1) + lines.get(cursorPosition[1]).substring(cursorPosition[0], lines.get(cursorPosition[1]).length()));
            cursorPosition[0]--;
        }else if(keyCode == PConstants.LEFT){
            if(cursorPosition[0] == 0) return;
            cursorPosition[0]--;
        }else if(keyCode == PConstants.RIGHT){
            cursorPosition[0]++;
        }else if(keyCode == PConstants.UP){
            if(cursorPosition[1] == 0) return;
            cursorPosition[1]--;
        }else if(keyCode == PConstants.DOWN){
            if(cursorPosition[1] == lines.size() - 1) return;
            cursorPosition[1]++;
        }else if(cursorPosition[0] < lines.get(cursorPosition[1]).length()){

            lines.set(cursorPosition[1], lines.get(cursorPosition[1]).substring(0, cursorPosition[0]) + key + lines.get(cursorPosition[1]).substring(cursorPosition[0] + 1, lines.get(cursorPosition[1]).length()));
            // cursorPosition[0]++;
        }else if (cursorPosition[0] > lines.get(cursorPosition[1]).length()){
            String line = lines.get(cursorPosition[1]);
            for(int i = 0; i < cursorPosition[0] - line.trim().length(); i++){
                line += " ";
            }
            line += key;
            lines.set(cursorPosition[1], line);
            cursorPosition[0]++;
        }else{
            lines.set(cursorPosition[1], lines.get(cursorPosition[1]).concat(""+key));
            cursorPosition[0]++;
        }
    }

    public void printStr(String str){

        String line = lines.get(cursorPosition[1]);
        String[] ls = str.split("\n", -1);
        if(ls.length > 1){
            for(int i = 0; i < ls.length - 1; i++){
                printStr(ls[i]);
                nextLine();
            }
            printStr(ls[ls.length - 1]);
            return;
        }
        line = line.substring(0, cursorPosition[0]) + str + line.substring(cursorPosition[0], line.length());
        lines.set(cursorPosition[1], line);
        cursorPosition[0] += str.length();

    }
    

    public void printlnStr(String str){
        printStr(str);
        nextLine();
    }
}