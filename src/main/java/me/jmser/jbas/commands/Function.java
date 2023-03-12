package me.jmser.jbas.commands;

public class Function {
    public String name;
    public String[] argTypes;
    public String libraryId;
    public String returnType;

    public Function(String name, String[] argTypes, String returnType, String libraryId) {
        this.name = name;
        this.argTypes = argTypes;
        this.libraryId = libraryId;
        this.returnType = returnType;
    }
}
