/**
 * A board parser
 */
public class BoardParser
{
    /**
     * Parse a board from an array of bytes
     * @param boardBytes The array of bytes representing the board
     * @return A real board
     */
    public static Board parse(byte[] boardBytes)
    {
        int boardWidth = 0;
        int boardHeight = 0;
        int playerX = 0;
        int playerY = 0;
        for (int i = 0; i < boardBytes.length; ++i) {
            switch (boardBytes[i]) {
                case '\n':
                    if (boardWidth != 0) { // TODO check if the boards always
                        // are rectangular
                        boardWidth = i;
                    }
                    boardWidth = i;
                    ++boardHeight;
                    break;
                case '@':
                    playerX = boardWidth;
                    playerY = boardHeight;
                    break;
            }
        }
        Board board = new Board(boardWidth, boardHeight, playerX, playerY);

        int row = 0;
        int col = 0;
        for (int i = 0; i < boardBytes.length; ++i, ++col) {
            switch (boardBytes[i]) {
                case '\n':
                    ++row;
                    col = 0;
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
