package me.jmser.jbas.commands;

import org.json.*;
import java.io.*;
import java.util.*;

public class LibraryManager {
    private static Hashtable<String, Library> libraries = new Hashtable<String, Library>(); // <id, Library>
    private static Hashtable<String, String> functionLib = new Hashtable<String, String>(); // <functionName, libraryId>




    private static LibraryManager instance = null;

    private LibraryManager() {
    }

    public static LibraryManager getInstance() {
        if (instance == null) {
            instance = new LibraryManager();
        }
        return instance;
    }

    public static boolean functionExists(String function) {
        return functionLib.containsKey(function);
    }

    public int numArgs(String function) {
        String libraryId = functionLib.get(function);
        if (libraryId == null) {
            return -1;
        }

        Library library = libraries.get(libraryId);
        if (library == null) {
            return -1;
        }

        for (Function f : library.functions.values()) {
            if (f.name.equals(function)) {
                return f.argTypes.length;
            }
        }

        return -1;
    }

    public static String exec(String function, String[] args) {
        String libraryId = functionLib.get(function);
        if (libraryId == null) {
            return "Function not found.";
        }

        Library library = libraries.get(libraryId);
        if (library == null) {
            return "Library not found.";
        }

        String[] command = new String[args.length + 2];
        command[0] = library.executablePath;
        command[1] = function;
        for (int i = 0; i < args.length; i++) {
            command[i + 2] = args[i];
        }
        String line = "";
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            line = reader.readLine();
            reader.close();
        } catch (IOException e) {
            System.out.println(e.toString());
            return "Error executing function.";
        }

        // Check the functions return type, if its a string add quotes
        String returnType = library.getFunction(function).returnType;
        if (returnType.equals("string")) {
            return "\"" + line + "\"";
        }

        return line;

        
    }

    public static void load(String library) {
        int infoSize = 1024 * 10;
        byte[] infoBytes = new byte[infoSize];
        byte[] executableBytes;
        File libraryFile = new File(library);

        FileInputStream libraryStream = null;

        try {
            libraryStream = new FileInputStream(libraryFile);
            libraryStream.read(infoBytes);
            libraryStream.close();
        } catch (IOException e) {
            System.out.println("Error reading library file.");
            return;
        }

        String info = new String(infoBytes);
        JSONObject infoJson = new JSONObject(info);
        String id = infoJson.getString("id");
        String version = infoJson.getString("version");

        String prefix = infoJson.getString("prefix");
        String flags = infoJson.getString("flags");


        try {
            libraryStream = new FileInputStream(libraryFile);
            libraryStream.skip(infoSize);
            executableBytes = libraryStream.readAllBytes();
            libraryStream.close();
        } catch (IOException e) {
            System.out.println("Error reading library file.");
            return;
        }

        try {
            FileOutputStream executableStream = new FileOutputStream(id);
            executableStream.write(executableBytes);
            executableStream.close();

            
        } catch (IOException e) {
            System.out.println("Error writing executable file.");
            return;
        }

        File executableFile = new File(id);
        executableFile.setExecutable(true);
        executableFile.setReadable(true);
        executableFile.setWritable(true);

        
        Library lib = new Library(id);
        lib.prefix = prefix;
        lib.flags = flags;
        JSONArray functionsJson = infoJson.getJSONArray("functions");

        for (int i = 0; i < functionsJson.length(); i++) {
            JSONObject functionJson = functionsJson.getJSONObject(i);
            String name = functionJson.getString("name");
            JSONArray args = functionJson.getJSONArray("args");
            String returnType = functionJson.getString("return");
            String[] argTypes = new String[args.length()];
            for (int j = 0; j < args.length(); j++) {
                argTypes[j] = args.getString(j);
            }

            lib.addFunction(new Function(name, argTypes, returnType, id));
            functionLib.put(name, id);

            



        }

        libraries.put(id, lib);

    }

    public static void clear(){
        // Clear libraries and delete executables

        for (Library lib : libraries.values()) {
            File executableFile = new File(lib.executablePath);
            executableFile.delete();
        }

        libraries.clear();
        functionLib.clear();


    }
    


}
