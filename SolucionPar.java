import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

class NQueenRecursive extends RecursiveAction {
    private static final int THRESHOLD = 2;
    private int minCol, maxCol;
    static long count;

    // Defaul values
    static int numQueens;
    static boolean printResult;

    NQueenRecursive(boolean printResult, int numQueens, int minCol, int maxCol) {
        setPrintResult(printResult);
        setNumQueens(numQueens);
        this.minCol = minCol;
        this.maxCol = maxCol;
    }

    static void setPrintResult(boolean print) {
        printResult = print;
    }

    static void setNumQueens(int num) {
        numQueens = num;
    }

    NQueenRecursive(int minCol, int maxCol) {
        this.minCol = minCol;
        this.maxCol = maxCol;
    }

    @Override
    protected void compute() {
        if ((maxCol - minCol) > THRESHOLD) {
            ForkJoinTask.invokeAll(createSubtasks());
        } else {
            processing(minCol, maxCol);
        }
    }

    private List<NQueenRecursive> createSubtasks() {
        List<NQueenRecursive> subtasks = new ArrayList<>();

        int middle = (minCol + maxCol) / 2;

        subtasks.add(new NQueenRecursive(minCol, middle));
        subtasks.add(new NQueenRecursive(middle, maxCol));

        return subtasks;
    }

    private void processing(int minCol, int maxCol) {
        ArrayList<ArrayList<Integer>> results = new ArrayList<ArrayList<Integer>>();
        for (int i = minCol; i < maxCol; i++) {
            NQueen(numQueens, 1, new ArrayList<Integer>(Arrays.asList(i)), results);
        }
        count += (results.size());
    }

    public static void NQueen(int dimension, int col, List<Integer> rowPlaceQueens,
            ArrayList<ArrayList<Integer>> results) {
        if (col == dimension) {
            ArrayList<Integer> newList = new ArrayList<Integer>(rowPlaceQueens);
            results.add(newList);
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
        return count;
    }
}

public class SolucionPar {
    public static void main(String[] args) {
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

        ForkJoinPool pool = new ForkJoinPool(12);
        NQueenRecursive task = new NQueenRecursive(printResult, numQueens, 0, numQueens);
        pool.invoke(task);
        System.out.println(task.getCount());
    }

    static public void handleInputError() {
        System.out.println("Invalid options. Use either:");
        System.out.println("To view number of solutions:");
        System.out.println("\tSolucion <num_queens>");
        System.out.println("To view number of solutions and print results:");
        System.out.println("\tSolucion <num_queens> -p");
        System.exit(0);
    }

    static public void handleArgumentError() {
        System.out.println("Out of range: <num_queens>");
        System.out.println("4 <= num_queens <= 50");
        System.exit(0);
    }
}
