# Parallel N-Queen Problem
N-Queen Problem solution using parallel programming.

The following is a reimplementation of the classic backtracking solution for the N-Queens problem, 
taking advantage of the fork/join framework, provided by Java, to take advantage of multiple 
processors.

As backtracking implies checking the correctness of each Queen placed in the board, dividing the 
problem into smaller recursive problems, allows increase the solution speed.

    Given a 4x4 board:

    ....
    ....
    ....
    ....

The number of rows is divided by the THRESHOLD (default value of 2). Which in this case means that 
two tasks will be generated. Each one in charge of generating a solution for the first or last two rows.

## Using the program
Existing compiled files created with: **javac 1.8.0_252**

Compile using the **makefile**, with the command **make** in a terminal.
Run using: **java Solucion**

If the user doesn't input arguments, the program runs with **8 queens**.

![No Argument Execution](https://github.com/RedRichard/N-Queen-Problem/blob/master/programScreenshots/default.png)

The user can specify the **number of queens** to run the program. By default
the results are not printed:
**java Solucion <num_queens>**

![NumQueens Argument Execution](https://github.com/RedRichard/N-Queen-Problem/blob/master/programScreenshots/arguments.png)

If the user wants to see the printed results:

**java Solucion <num_queens> <-p>**

![NumQueens and Print Argument Execution](https://github.com/RedRichard/N-Queen-Problem/blob/master/programScreenshots/arguments2.png)
