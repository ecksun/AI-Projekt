package sokoban;

/**
 * A board parser
 */
public class BoardParser
{
    /**
     * Parse a board from an array of bytes
     * 
     * @param boardBytes
     *            The array of bytes representing the board
     * @return A real board
     */
    public static Board parse(byte[] boardBytes)
    {
        int boardWidth = 0;
        int boardHeight = 1; // board input string doesn't end with '\n'
        int playerX = 0;
        int playerY = 0;
        int rowLength = 0;
        for (int i = 0; i < boardBytes.length; ++i) {
            rowLength++;
            switch (boardBytes[i]) {
                case '\n':
                    if (rowLength > boardWidth) {
                        boardWidth = rowLength - 1; // '\n' not part of board
                    }
                    rowLength = 0;
                    ++boardHeight;
                    break;
                case '@':
                    // Player position is 0-indexed.
                    playerX = rowLength - 1;
                    playerY = boardHeight - 1; 
                    break;
            }
        }

        Board board = new Board(boardWidth, boardHeight, playerX, playerY);

        int row = 0;
        int col = 0;
        for (int i = 0; i < boardBytes.length; ++i, ++col) {
            switch (boardBytes[i]) {
                case '\n':
                    // col is incremented before first char on every row
                    col = -1;
                    ++row;
                    break;
                case '#':
                    board.cells[row][col] = Board.WALL;
                    break;
                case '$':
                    board.cells[row][col] = Board.BOX;
                    break;
                case '+':
                    board.cells[row][col] = Board.GOAL;
                    break;
                case '*':
                    board.cells[row][col] = Board.BOX & Board.GOAL;
                    break;
                case '.':
                    board.cells[row][col] = Board.GOAL;
                    break;
            }
        }
        return board;
    }
}
