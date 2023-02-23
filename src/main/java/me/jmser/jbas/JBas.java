package me.jmser.jbas;

import me.jmser.jbas.interfaces.GUI;
import me.jmser.jbas.interfaces.JBasicInterface;

public class JBas {
    public static void main(String[] args){
        JBasicInterface iface = new GUI();

        iface.run();
    }
}
