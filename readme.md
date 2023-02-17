# JBasic

JBasic is a Java implementation of a BASIC programming dialect. It remains a work in progress, but is functional enough to serve as a useful tool for teaching programming concepts.

## Commands

The following commands are supported:

* `PRINT` - print a value to the console

* `INPUT` - read a value from the console

* `LET` - assign a value to a variable

* `GOTO` - jump to a line number

* `IF` - conditionally execute the next line

* `ELSE` - conditionally execute the next line (after an `IF`)

* `LIST` - list the program

* `RUN` - run the program

* `EXIT` - exit the CLI

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

Here is an example of the classic BASIC hello world program, but using interpreter variables:

```BASIC
10 "Hello World!"
20 GOTO first
```

### Flags

Flags are another kind of special variable. They are primarily used by the `IF` and `ELSE` commands to determine whether the next line should be executed. Flags are changed by assigning a new value to them. The following flags are available:

* `FLAG_ELSE` - The `ELSE` flag is set to 1 when an `IF` statement evaluates to false. The `ELSE` flag is set to 0 when the `ELSE` command is executed.

* `FLAG_SKIP` - The `SKIP` flag is set to 1 when the next line should be skipped. This flag is also set to 1 when an `IF` statement evaluates to false.  Additionally, the `SKIP` flag is set to 1 when the `ELSE` command is executed and the `ELSE` flag is set to 1.

* `FLAG_EXIT` - The `EXIT` flag is set to 1 when the `EXIT` command is executed. When the `EXIT` flag is set to 1, the program will exit without resetting the flags or variables. This can be useful for debugging.


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