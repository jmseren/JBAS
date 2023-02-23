package me.jmser.jbas;

import me.jmser.jbas.interfaces.*;

public class JBas {
    public static void main(String[] args){
        JBasicInterface iface;

        if(args.length > 0 && (args[0].equals("-g") || args[0].equals("--gui"))){
            iface = new GUI();
        } else {
            iface = new CLI();
        }

        iface.run();
    }
}
