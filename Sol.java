import java.util.ArrayList;
import java.util.List;

public class Solucion {
    // test variables
    static int numQueens = 4;

    public static void main(String[] args) {

        ArrayList<ArrayList<Integer>> results = new ArrayList<ArrayList<Integer>>();
        nQueens(numQueens, 0, new ArrayList<Integer>(), results);

        for (ArrayList<Integer> result : results) {
            System.out.println(resultListString(result));
        }

    }

    public static void nQueens(int dimension, int col, ArrayList<Integer> placedQueens,
            ArrayList<ArrayList<Integer>> results) {
        if (col == dimension) {
            // results.add(new ArrayList<Integer>((List) placedQueens));
            System.out.println(resultListString(placedQueens));
        } else {
            for (int row = 0; row < dimension; row++) {
                placedQueens.add(row);
                if (checkValid(placedQueens)) {
                    nQueens(dimension, col + 1, placedQueens, results);
                }
                placedQueens.remove(placedQueens.size() - 1);
            }
        }
    }

    public static boolean checkValid(ArrayList<Integer> placedQueens) {
        int col = placedQueens.size() - 1;
        int diff;
        for (int i = 0; i < col; i++) {
            diff = Math.abs(placedQueens.get(col) - placedQueens.get(i));
            if (diff == 0 || diff == col - i)
                return false;
        }
        return true;
    }

    public static String resultListString(ArrayList<Integer> placedQueens) {
        int dimension = placedQueens.size();
        String board = "";
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (i == placedQueens.get(i))
                    board += placedQueens.get(i) + " ";
                else
                    board += "0 ";
            }
            board += "\n";
        }
        return board;
    }
}