import java.util.ArrayList;

class Queen {

    private int posX;
    private int posY;

    Queen(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    /**
     * @return the posX
     */
    public int getPosX() {
        return posX;
    }

    /**
     * @return the posY
     */
    public int getPosY() {
        return posY;
    }

}

class Board {
    int dimension;
    int board[][];
    ArrayList<Integer> queenRows;

    Board(int n) {
        dimension = n;
        board = new int[n][n];
        queenRows = new ArrayList<Integer>();
        /*
         * for (int i = 0; i < n; i++) { for (int j = 0; j < n; j++) { clearSpace(i, j);
         * } }
         */
    }

    /*
     * public void placeQueen(Queen newQueen) { queenList.add(newQueen); //
     * board[i][j] = 1; }
     * 
     * public void removeLastQueen() { queenList.remove(queenList.size() - 1); }
     * 
     * public void clearSpace(int i, int j) { board[i][j] = 0; }
     */

    public void placeQueen(int row) {
        queenRows.add(new Integer(row));
    }

    public void removeLastQueen() {
        queenRows.remove(queenRows.size() - 1);
    }

    public boolean isValid() {
        int col = queenRows.size() - 1;
        for (int i = 0; i < col; i++) {
            int diff = Math.abs(queenRows.get(i) - queenRows.get(col));
            if (diff == 0 || diff == col - i)
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (i == queenRows.get(j)) {
                    result += "1 ";
                } else {
                    result += "0 ";
                }
            }
            result += "\n";
        }
        return result;
        /*
         * for (int i = 0; i < dimension; i++) { for (int j = 0; j < dimension; j++) {
         * result += board[i][j] + " "; } result += "\n"; } return result;
         */
    }
}

public class Solution {
    // test variables
    static int numQueens = 10;

    public static void main(String[] args) {
        // Board board = new Board(numQueens);
        // System.out.print(board.toString());

        ArrayList<Board> results = new ArrayList<Board>();
        nQueens(numQueens, 0, new Board(numQueens), results);

        // System.out.println(results.size());
        // System.out.println(results.get(0));

        /*
         * for (Board board : results) { System.out.print(board.toString()); }
         */

    }

    public static void nQueens(int dimension, int col, Board board, ArrayList<Board> results) {
        if (col == dimension) {
            // results.add(board);
            System.out.println(board);
        } else {
            for (int row = 0; row < dimension; row++) {
                board.placeQueen(row);
                if (board.isValid()) {
                    nQueens(dimension, col + 1, board, results);
                }
                board.removeLastQueen();
            }
        }
    }
}