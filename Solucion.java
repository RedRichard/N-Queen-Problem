/*
    The following is a reimplementation of the classic backtracking
    solution for the N-Queens problem, taking advantage of the
    fork/join framework, provided by Java, to take advantage of
    multiple processors.

    As backtracking implies checking the correctness of each Queen
    placed in the board, dividing the problem into smaller recursive 
    problems, allows increase the solution speed.

    Given a 4x4 board:

    ....
    ....
    ....
    ....

    The number of rows is divided by the THRESHOLD (default value of 2).
    Which in this case means that two tasks will be generated. Each one
    in charge of generating a solution for the first or last two rows.
*/

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

class NQueenRecursive extends RecursiveAction {
    /*
     * This class contains the behavior for each generated task. Each task acts as a
     * WORKER, handling a smaller part of the problem. Problem is not solved until
     * it is small enough.
     */

    private static final int THRESHOLD = 2; // Limits the size of the problem
    private int minCol, maxCol;
    static long count; // Number of results

    // Defaul values. To be initialized by the main class.
    static int numQueens; // Number of queens to be placed.
    static boolean printResult; // Defines whether results should be printed or not.

    NQueenRecursive(boolean printResult, int numQueens, int minCol, int maxCol) {
        /*
         * Constructor for the initial problem. Requires the values of the "printResult"
         * and "numQueens" in order to define the rest of the behavior.
         */
        setPrintResult(printResult);
        setNumQueens(numQueens);
        this.minCol = minCol;
        this.maxCol = maxCol;
    }

    static void setPrintResult(boolean print) {
        /*
         * Sets the static "printResult" variable.
         */
        printResult = print;
    }

    static void setNumQueens(int num) {
        /*
         * Sets the static "numQueens" variable.
         */
        numQueens = num;
    }

    NQueenRecursive(int minCol, int maxCol) {
        /*
         * Constructor to be used by the main problem, for each new instance of a
         * smaller problem.
         */
        this.minCol = minCol;
        this.maxCol = maxCol;
    }

    @Override
    protected void compute() {
        /*
         * Defines the program flux. If: a problem is too large to be solved, it is
         * further subdivided. Else: solve the problem.
         */
        if ((maxCol - minCol) > THRESHOLD) {
            ForkJoinTask.invokeAll(createSubtasks());
        } else {
            processing(minCol, maxCol);
        }
    }

    private List<NQueenRecursive> createSubtasks() {
        /*
         * Method in charge of creating new subtasks out of the larger problem, defined
         * by the instance from which it was called.
         */
        List<NQueenRecursive> subtasks = new ArrayList<>();

        int middle = (minCol + maxCol) / 2;

        subtasks.add(new NQueenRecursive(minCol, middle));
        subtasks.add(new NQueenRecursive(middle, maxCol));

        return subtasks;
    }

    private void processing(int minCol, int maxCol) {
        /*
         * Solves the current problem.
         */
        ArrayList<ArrayList<Integer>> results = new ArrayList<ArrayList<Integer>>();
        for (int i = minCol; i < maxCol; i++) {
            NQueen(numQueens, 1, new ArrayList<Integer>(Arrays.asList(i)), results);
        }
        count += (results.size());
    }

    public static void NQueen(int dimension, int col, List<Integer> rowPlaceQueens,
            ArrayList<ArrayList<Integer>> results) {
        /*
         * Backtracking solution for the N-Queens problem.
         */
        if (col == dimension) {
            ArrayList<Integer> newList = new ArrayList<Integer>(rowPlaceQueens);
            results.add(newList);
            // The result is printed if necessary.
            if (printResult)
                System.out.print(resultListString(newList));
        } else {
            for (int i = 0; i < dimension; i++) {
                rowPlaceQueens.add(i);
                if (checkQueens(rowPlaceQueens)) {
                    NQueen(dimension, col + 1, rowPlaceQueens, results);
                }
                rowPlaceQueens.remove(col);
            }
        }
    }

    public static boolean checkQueens(List<Integer> rowPlaceQueens) {
        /*
         * Function in charge of checking whether the position of the Queens on a board
         * is valid or not.
         */
        int col = rowPlaceQueens.size() - 1;
        for (int i = 0; i < col; i++) {
            int diff = Math.abs(rowPlaceQueens.get(col) - rowPlaceQueens.get(i));
            if (diff == 0 || diff == col - i) {
                return false;
            }
        }
        return true;
    }

    public static String resultListString(ArrayList<Integer> placedQueens) {
        /*
         * Function in carge of transforming each solutions into a string.
         */
        int dimension = placedQueens.size();
        String board = "";
        for (int row = 0; row < dimension; row++) {
            for (int column = 0; column < dimension; column++) {
                if (row == placedQueens.get(column))
                    board += "1 ";
                else
                    board += "0 ";
            }
            board += "\n";
        }
        return board += "\n";
    }

    public long getCount() {
        /*
         * Allows to have access to the current total number of solutions.
         */
        return count;
    }
}

public class Solucion {
    // Main class
    public static void main(String[] args) {
        /*
         * Main function, in charge of generating the task required to begin the
         * solution of the problem. MANAGER
         */
        // Default variables:
        int numQueens = 8;
        boolean printResult = false;

        // Argument logic and handling
        /*
         * Try to get usable valuables from arguments provided by the user. Except:
         * finish program and print message.
         */
        try {
            if (args.length == 1) {
                numQueens = Integer.parseInt(args[0]);
            } else if (args.length == 2) {
                if (args[1].equals("-p")) {
                    numQueens = Integer.parseInt(args[0]);
                    printResult = true;
                }
            } else if (args.length > 2) {
                throw new Exception();
            }

            if (numQueens < 4 || numQueens > 50) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            handleArgumentError();
        } catch (Exception e) {
            handleInputError();
        }

        // Create a new Pool from which each task will run.
        // Number of threads defined to 12
        ForkJoinPool pool = new ForkJoinPool(12);

        // A new NQueenRecursive task is instanciated.
        NQueenRecursive task = new NQueenRecursive(printResult, numQueens, 0, numQueens);

        // The task is added to the Pool for it to be executed in parallel.
        pool.invoke(task);

        // Print total number of solutions.
        System.out.println("Number of queens: " + numQueens);
        System.out.println("Number of solutions: " + task.getCount());
    }

    static public void handleInputError() {
        /*
         * Handles an input error.
         */
        System.out.println("Invalid options. Use either:");
        System.out.println("To view number of solutions:");
        System.out.println("\tSolucion <num_queens>");
        System.out.println("To view number of solutions and print results:");
        System.out.println("\tSolucion <num_queens> -p");
        System.exit(0);
    }

    static public void handleArgumentError() {
        /*
         * Handles an invalid argument error.
         */
        System.out.println("Out of range: <num_queens>");
        System.out.println("4 <= num_queens <= 50");
        System.exit(0);
    }
}
