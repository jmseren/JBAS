# Libraries

Libraries are a way to extend the functionality of JBAS. They allow you to create your own commands that can be used in the interpreter. They can be written in any language, as long as they can be compiled into an executable file or ran as a script. The executable file will be packaged into a `.jlib` file, which can be used in JBAS.

This page will explain how to create a library and how to use it in JBAS.

## Metadata Syntax
```JSON
{
  "name": "Library Name",
  "id": "library.id", // Unique lowercase, no space name for the library.
  "version": "Library Version",
  "author": "Library Author",
  "description": "Library Description",
  "prefix": "prefix", // When called from JBAS, the library will be called with this prefix
  "functions": [
   {
    "name": "Command Name",
    "return": "Return Type",
    "description": "Command Description", // Optional
    "args": [
     {
     "type": "Argument Type"
     }
    ]
  }
]
}
```
- `name` is the name of the library.

- `id` is the unique lowercase, no space name for the library. This is used when creating the executable file.

- `version` is the version of the library.

- `author` is the author of the library.

- `description` is the description of the library.

- `prefix` is the prefix that will be used when calling the library from JBAS. For example, if the prefix is `lib`, then the library will be called with `lib <executable> <command> <args>`.

- "functions" is an array of functions that the library contains.
  - "name" is the name of the command.
  - "return" is the return type of the command.
  - "description" is the description of the command. This is optional.
  - "args" is an array of arguments that the command takes.
    - "type" is the type of the argument.

## Example
### The Hello World Library
#### The Code
```Java
public class HelloWorld {
    public static void main(String[] args) {
        if (args[0].equals("hello")) {
            System.out.println("Hello, " + args[1] + "!");
        } else if (args[0].equals("goodbye")) {
            System.out.println("Goodbye, " + args[1] + "!");
        }
    }
}
```
This library contains two commands, `hello` and `goodbye`. Each prints out its respective message, with the name of the person being greeted. The Java file will be compiled into an executable file (`.jar`) and packaged into the `.jlib` file.

#### The JSON
```JSON
{
    "name": "Hello World Library",
    "id": "helloworld", 
    "version": "1.0",
    "author": "JMSER",
    "description": "A simple library that prints out hello and goodbye messages.",
    "prefix": "java -jar",
    "flags": "", 
    "functions": [
        {
            "name": "hello",
            "description": "Prints out a hello message.",
            "return": "string",
            "args": [
                {
                    "type": "string"
                }
            ]
        },
        {
            "name": "goodbye",
            "description": "Prints out a goodbye message.",
            "return": "string",
            "args": [
                {
                    "type": "string"
                }
            ]
        }
    ]
}
```
This is the JSON file for our library. It contains all the necessary information for the library to be used in JBAS.

### Compiling the Library
To compile the library, you can use the JBAS Library Utility. This is a command line utility that will compile the library into a `.jlib` file. To use it, run the following command:
```bash
java -jar libraryutil.jar -b <library.json> <code.jar> <output.jlib>
```

You will now have a `.jlib` file that you can use in JBAS. Additionally, since we wrote the library in Java, your library will be able to run on any platform that has Java installed.

## Usage
To use a library in JBAS, you must first import it. To do this, run the following command:
```BASIC
LIB <library.jlib>
```
This will import the library into JBAS. You can now use the library by calling it with the command names specified in the JSON file. For example, if we had imported our Hello World Library, we could call it with the following command:
```BASIC
PRINT hello("John")
```

## Technical Details
The structure of a `.jlib` file is as follows:
* 10kb of data reserved for the library information (in JSON format)
* Executable Code

## More Libraries
* [Math Library](math.jlib)