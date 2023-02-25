# JBAS

JBAS is a Java implementation of a BASIC programming dialect. It remains a work in progress, but is functional enough to serve as a useful tool for teaching programming concepts.

Included are two interfaces, one command line and one GUI. Both interfaces are fully functional, and can be used to run the same programs. The GUI interface is more user friendly, and is recommended for beginners. Additionally, the GUI interface is able to display graphics.

## Table of Contents

* [Compiling](#compiling)
* [Commands](#commands)
* [Expressions](#expressions)
* [Variables](#variables)
* [Control Flow](#control-flow)
* [Special Variables](#special-variables)
* [Input and Output](#input-and-output)
* [Subroutines](#subroutines)
* [Saving and Loading](#saving-and-loading)
* [Examples](#example-programs)


## Compiling

To compile the project, run the following command:

```BASH
mvn clean compile assembly:single
```

This will create a jar file in the `target` directory. To run the program, run the following command:

```BASH
java -jar target/jbasic-[version].jar
```

To run the program with the GUI interface, run the following command:

```BASH
java -jar target/jbasic-[version].jar --gui
```

## Commands

The following commands are supported:

* `PRINT` - print a value to the console

* `TAB` - move the cursor to a specified column

* `REM` - add a comment

* `INPUT` - read a value from the console into a variable

* `LET` - assign a value to a variable

* `DIM` - declare an array

* `GOTO` - jump to a line number

* `GOSUB` - jump to a line and store the current line number

* `RETURN` - return to the line number stored by the last `GOSUB` command

* `IF` - conditionally execute the next line

* `ELSE` - conditionally execute the next line (after an `IF`)

* `CLEAR` - clear the program

* `CLS` - clear the console/screen

* `POKE` - assign a value to a memory address (used for graphics)

* `LIST` - list the program

* `RUN` - run the program

* `SAVE` - save the program to a specified file

* `LOAD` - load a program from a specified file

* `EXIT` - exit the CLI or program

## Expressions
Expressions are strictly evaluated from left to right, and require parentheses for more than two operands. The following operators are supported:

* `+` - addition

* `-` - subtraction

* `*` - multiplication

* `/` - division

### Examples

```JavaScript
5 + 5
```
10



```JavaScript
5 + (5 * 2)
```
15



```JavaScript
(5 + 5) * 2
```
20



```JavaScript
5 + 5 * 2
```
Error

In the future, precedence may be added to the language.

## Variables

Variables are declared by assigning a value to them. Currently, only integer and string values are supported. To assign a value to a variable, use the `LET` command:

```BASIC
LET x = 5
LET y = "Hello World!"
```

Additionally, variables can be assigned to the result of an expression:

```BASIC
LET x = 5 + 5
LET y = x * 2
```

Variables can be reassigned to new values:

```BASIC
LET x = 5
LET x = 10
LET x = x + 5
```

Variables must start with a letter, and can contain letters, numbers, and underscores.

### Arrays

Arrays are declared by using the `DIM` command. The `DIM` command takes two arguments: the name of the array, and the size of the array. The size of the array must be a positive integer. The following example declares an array named `x` with a size of 10:

```BASIC
DIM x 10
```

Arrays are indexed starting at 0, and are accessed using square brackets. The following example assigns the value 5 to the first element of the array `x`:

```BASIC
DIM x 10
LET x[0] = 5
```

Arrays can accessed using a variable as the index:

```BASIC
DIM x 10
LET i = 5
LET x[i] = 3
PRINT x[5]
```

3


## Control Flow

The `IF` and `ELSE` commands are used to control the flow of the program. The `IF` command takes a boolean expression as its argument, and executes the next line if the expression evaluates to true The `ELSE` command executes the next line if the previous `IF` expression evaluated to false.

```BASIC
LET x = 5
LET y = 10
IF x > y
PRINT "x is greater than y"
ELSE
PRINT "x is less than or equal to y"
```

You may find yourself wanting to execute a block of statements conditionally. This can be accomplished by using the `GOTO` command to jump to a line number:

```BASIC
10 LET x = 5
20 LET y = 10
30 IF x > y
40 GOTO 60
50 PRINT "x is less than or equal to y"
60 GOTO 80
70 PRINT "x is greater than y"
80 PRINT "done"
```

Notice the lack of `ELSE` in the above example. This is because the `GOTO` command is used to jump to the next line after the `IF` statement. The `ELSE` statement is not necessary, but can be used to make the program more readable.

If you prefer, `THEN` can be used in place of `GOTO`.

### Loops

The `GOTO` command can be used to create loops. The following example prints the numbers 1 through 10:

```BASIC
10 LET i = 1
20 PRINT i
30 LET i = i + 1
40 IF i <= 10
50 GOTO 20
```

## Special Variables

Special variables can be used in any place where a variable can be used, including in expressions. 

### Pseudo-Functions

Pseudo-functions are special variables that return a value when they are used in an expression. They may not be assigned to by the user. The following pseudo-functions are available:

* `time` - The current time in seconds.

* `rnd_` - A random integer between 0 and the bound placed after the underscore. For example, `rnd_10` will return a random integer between 0 and 10.

* `neg_` - The negative of the value placed after the underscore. For example, `neg_5` will return -5.

* `abs_` - The absolute value of the value placed after the underscore. For example, `abs_-5` will return 5.

* `sqrt_` - The square root of the value placed after the underscore. For example, `sqrt_25` will return 5.

### Intepreter Variables

Interpreter variables are special variables that are used by the interpreter to keep track of the program's state. The following interpreter variables are available:

* `line` - The current line number. This is the only interpreter variable that can be assigned to.

* `first` - The first line number.

* `last` - The last line number.

* `next` - The next line number, or 0 if there is no next line. This is relative to the current line number.

* `prev` - The previous line number, or 0 if there is no previous line. This is relative to the current line number.

* `count` - The number of lines in the program.

* `ret` - The line number to return to after a `GOSUB` command.

Here is an example of the classic BASIC hello world program, but using interpreter variables:

```BASIC
10 PRINT "Hello World!"
20 GOTO first
```

### Flags

Flags are another kind of special variable. They are primarily used by the `IF` and `ELSE` commands to determine whether the next line should be executed. Flags are changed by assigning a new value to them. The following flags are available:

* `FLAG_ELSE` - The `ELSE` flag is set to 1 when an `IF` statement evaluates to false. The `ELSE` flag is set to 0 when the `ELSE` command is executed.

* `FLAG_SKIP` - The `SKIP` flag is set to 1 when the next line should be skipped. This flag is also set to 1 when an `IF` statement evaluates to false.  Additionally, the `SKIP` flag is set to 1 when the `ELSE` command is executed and the `ELSE` flag is set to 1.

* `FLAG_EXIT` - The `EXIT` flag is set to 1 when the `EXIT` command is executed. When the `EXIT` flag is set to 1, either via the `EXIT` command or setting it manually, the program will exit. In program mode, FLAG_EXIT is set to 1 when the `EXIT` command is executed. In interactive mode, FLAG_EXIT is initially set to 1, and is set to 0 when the `RUN` command is executed.

## Input and Output

### Input

The `INPUT` command is used to prompt the user for input. The `INPUT` command takes a variable name as its argument. The following example prompts the user for input and stores it in the `x` and prints it to the screen:

```BASIC
INPUT x
PRINT x
```
### Output

The `PRINT` command is used to print output to the screen. The `PRINT` command takes a string as its argument. The following example prints the string "Hello World!" to the screen:

```BASIC
PRINT "Hello World!"
```

You can also concatenate strings with either expressions or variables usingthe `+` operator:

```BASIC
PRINT "Hello " + "World!"
LET x = 5
PRINT "x = " + x
```

#### Graphics

By using the `POKE` command, you can draw graphics to the screen. The `POKE` command takes two arguments: the address and the value separated by a comma. In the GUI version of the interpreter, addresses 0-3999 correspond to the pixels on the screen. The value is the color of the pixel. The following example draws a red 10x10 square to the screen:

```BASIC
00 CLS
10 LET x = 35
20 LET y = 15
30 LET color = 2
40 POKE (x + (y * 80)), color
50 LET x = x + 1
60 IF x < 45
70 GOTO 40
80 LET x = 35
90 LET y = y + 1
100 IF y < 25
110 GOTO 40
```

Pixels are placed on top of the screen, such that the text is underneath the graphics. The `CLS` command clears the screen of both text and graphics. If you want to clear a single pixel, you can set its color to -1. 

For a list of color, see the C64 Color Chart: [https://www.c64-wiki.com/wiki/Color\_Chart](https://www.c64-wiki.com/wiki/Color_Chart)

## Subroutines

Using the `GOSUB` command, you can jump to a line number and return to next instruction when the `RETURN` command is executed. The `GOSUB` command takes a line number as its argument. The `RETURN` command does not take any arguments. Subroutines can also be nested. Here is an example of a subroutine that increments the `x` variable:

```BASIC
0 REM Program to demonstrate the use of subroutines as functions
10 LET x = 1
20 REM Function Definitions
21 LET func_incr_x = 100
30 REM Program Body
40 GOSUB func_incr_x
50 GOTO last
100 REM func_incr_x
101 LET x = x + 1
110 RETURN
120 REM End of Program
130 PRINT "x = " + x
```

Additionally, here is an example of a subroutine uses nested subroutines:

```BASIC
0 REM Program to take an input, increment it by 1 and then multiply it by 2.
10 LET x = 1
20 REM Function Definitions
21 LET func_incr_x = 100
22 LET func_mult2_x = 200
23 LET func_mult2_incr_x = 300
30 REM Program Body
40 GOSUB func_mult2_incr_x
50 GOTO last
100 REM func_incr_x
101 LET x = x + 1
110 RETURN
200 REM func_mult2_x
201 LET x = x * 2
210 RETURN
300 REM func_mult2_incr_x
301 GOSUB func_incr_x
302 GOSUB func_mult2_x
303 RETURN
310 REM End of Program
320 PRINT "x = " + x
```

## Saving/Loading Programs

Programs can be saved and loaded using the `SAVE` and `LOAD` commands respectively. They both take a filename in the current directory as their argument. 

### Example

Let's write a program that asks the user for their name, and then print's out a greeting.

```BASIC
10 PRINT "We haven't met, what's your name?"
20 INPUT name
30 PRINT "Hello " + name + "! It's nice to meet you."
```

Now, save this program to a file called "greetings.jbas":
``` 
=> SAVE greetings.jbas
```

Later, we can load this program back into the interpreter:
```
=> LOAD greetings.jbas
=> LIST
10 PRINT "We haven't met, what's your name?"
20 INPUT name
30 PRINT "Hello " + name + "! It's nice to meet you."
```

When loading a program, the interpreter will ignore any lines that do not begin with a line number. You can use this to add comments to your program, but beware as these comments will be ignored when the program is loaded and thus will not be printed when the `LIST` command is executed.

## Example Programs

### Number Guessing Game

```BASIC
10 PRINT "Guessing Game!"
20 LET n = rnd_100
30 LET num = 1
40 PRINT "Guess a number (0-100): "
50 INPUT g
60 IF g == n
61 THEN 70
62 ELSE
63 GOTO 100
70 PRINT "You got it in " + num + " tries! Play again?: (y/n)"
80 INPUT a
90 IF a == "y"
91 GOTO first
92 ELSE
93 GOTO last
100 IF g < n
101 PRINT "Too low!"
102 ELSE
103 PRINT "Too high!"
110 LET num = num + 1
120 GOTO 40
130 PRINT "Thanks for playing!"
```

### Fibonacci Sequence

```BASIC
10 PRINT "Fibonacci Sequence"
20 PRINT "How many numbers would you like to print?"
30 INPUT n
40 LET a = 1
50 LET b = 1
60 IF n <= 0
61 THEN last
62 ELSE
70 PRINT a
80 LET c = a + b
90 LET a = b
100 LET b = c
110 LET n = n - 1
120 GOTO 60
130 PRINT "Done."
```

You can find more example programs in the [examples](examples) directory.