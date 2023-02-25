package me.jmser.jbas.interfaces;

public interface JBasicInterface {
    public void print(String output);
    public void println(String output);
    public void println();
    public void putPixel(int x, int y, int color); // Put a pixel on the screen, color is an integer between 0 and 15. Reference C64 colors here: https://www.c64-wiki.com/wiki/Color
    public void tab(int n); // Move the cursor n spaces to the right in the CLI, or to column n in the GUI 
    public String getLine(); // Let the user input a line
    public void clear();
    public void run();
}
