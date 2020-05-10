import java.util.ArrayList;
import java.util.List;

public class Solucion {
    // Default variables
    public static int numQueens = 4;

    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> results = new ArrayList<ArrayList<Integer>>();
        NQueen(numQueens, 0, new ArrayList<Integer>(), results);

        for (ArrayList<Integer> result : results) {
            System.out.println(resultListString(result));
        }
    }

    public static void NQueen(int dimension, int col, List<Integer> rowPlaceQueens,
            ArrayList<ArrayList<Integer>> results) {
        if (col == dimension) {
            results.add(new ArrayList<Integer>(rowPlaceQueens));
            System.out.print(resultListString(new ArrayList<Integer>(rowPlaceQueens)));
        } else {
            for (int i = 0; i < dimension; i++) {
                rowPlaceQueens.add(i);
                if (checkQueens(rowPlaceQueens)) {
                    NQueen(dimension, col + 1, rowPlaceQueens, results);
                }
                rowPlaceQueens.remove(rowPlaceQueens.size() - 1);
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