package sokoban;

/**
 *
 */
public class Board
{
    // Input values
    /**
     * Value for a WALL on the board
     */
    public final static byte WALL     = 0x01;

    /**
     * Value for a BOX on the board
     */
    public final static byte BOX      = 0x02;
    /**
     * Value for a GOAL position on the board
     */
    public final static byte GOAL     = 0x04;

    // Generated values
    /**
     * No box allowed.
     */
    public final static byte NO_BOX   = 0x08;
    // Bitmasks
    /**
     * A bitmask that says if a cell is occupied by something
     */
    public final static byte OCCUPIED = WALL | BOX;

    final int                width;
    final int                height;

    /**
     * The actuall board
     */
    public byte              cells[][];
    int                      playerCol;
    int                      playerRow;
    int                      remainingBoxes;

    /**
     * Initialize a new board
     * 
     * @param width
     *            The width of the board
     * @param height
     *            The height of the board
     * @param playerCol
     *            The X position of the player
     * @param playerRow
     *            The Y position of the player
     */
    public Board(int width, int height, int playerCol, int playerRow)
    {
        cells = new byte[height][width];
        this.width = width;
        this.height = height;
        this.playerCol = playerCol;
        this.playerRow = playerRow;
    }

    /**
     * Get the width of the board
     * 
     * @return The width of the board
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * Get the height of the board
     * 
     * @return The height of the board.
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * Returns true if the square has any of the bits in the mask.
     */
    private boolean is(byte square, byte mask)
    {
        return (square & mask) != 0;
    }

    /**
     * Updates some variables after a board has been loaded.
     */
    public void refresh()
    {
        countBoxes();
        markNonBoxSquares();
    }

    /**
     * Counts the boxes that are not in a goal, and updates remainingBoxes.
     */
    private void countBoxes()
    {
        int remaining = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if ((cells[row][col] & (BOX | GOAL)) == BOX) {
                    remaining++;
                }
            }
        }
        remainingBoxes = remaining;
    }

    /**
     * Print the current board
     * 
     * @return The string representing the board
     */
    @Override
    public String toString()
    {
        String tmp = "";
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                tmp += cellToChar(i, j);
            }
            tmp += "\n";
        }
        return tmp;
    }

    /**
     * Returns the official Sokoban char for the given internal value.
     * 
     * @param value
     *            The internal byte representation of a cell.
     * @return The character that is represented by the given internal value.
     */
    private char valueToChar(byte value)
    {
        switch (value) {
            case Board.WALL:
                return '#';
            case Board.GOAL:
                return '.';
            case Board.BOX:
                return '$';
            case Board.GOAL | Board.BOX:
                return '*';
            default:
                return ' ';
        }
    }

    /**
     * Returns the Sokoban char for the given cell. Takes care of player
     * position.
     * 
     * @param row
     *            The row index of the cell to specify.
     * @param col
     *            The column index of the cel to specify.
     * @return The Sokoban char for the specified cell according to
     *         http://www.sokobano.de/wiki/index.php?title=Level_format.
     */
    private char cellToChar(int row, int col)
    {
        if (playerRow == row && playerCol == col) {
            return cells[row][col] == Board.GOAL ? '+' : '@';
        }
        else {
            return valueToChar(cells[row][col]);
        }
    }

    /**
     * Marks squares that boxes would get stuck in.
     */
    private void markNonBoxSquares()
    {
        final byte TOP = 0x1;
        final byte BOTTOM = 0x2;
        final byte LEFT = 0x4;
        final byte RIGHT = 0x8;

        final byte VERTICAL = TOP | BOTTOM;
        final byte HORIZONTAL = LEFT | RIGHT;
        final byte ALL = VERTICAL | HORIZONTAL;

        final byte blocked[][] = new byte[height][width];

        // Mark all walls as blocked
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (is(cells[y][x], WALL))
                    blocked[y][x] = ALL;
            }
        }

        // Find "non box" squares
        for (int row = 1; row < height - 1; row++) {
            for (int col = 1; col < width; col++) {
                // Break the "blocked lines" if there's a goal
                if (is(cells[row][col], GOAL)) {
                    continue;
                }

                final boolean rightIsWall = (col < width - 1 ? is(
                        cells[row][col + 1], WALL) : true);
                final byte neighborWalls = (byte) ((is(cells[row - 1][col],
                        WALL) ? TOP : 0)
                        | (is(cells[row + 1][col], WALL) ? BOTTOM : 0)
                        | (is(cells[row][col - 1], WALL) ? LEFT : 0) | (rightIsWall ? RIGHT
                        : 0));

                // How the current cell can be blocked at most,
                // taking cells to the left and above into account.
                final byte verticalBlocked = (byte) (neighborWalls
                        & blocked[row][col - 1] & VERTICAL);
                final byte horizontalBlocked = (byte) (neighborWalls
                        & blocked[row - 1][col] & HORIZONTAL);

                final boolean isWall = is(cells[row][col], WALL);

                if (!isWall) {
                    // Use common vertical blocking status
                    // with the block to the left
                    blocked[row][col] |= verticalBlocked | horizontalBlocked;
                }

                if (isWall && (blocked[row][col - 1] & VERTICAL) != 0) {
                    // There's a wall and the preceding cells are blocked
                    // somehow
                    for (int i = col - 1; i > 0; i--) {
                        if (is(cells[row][i], WALL))
                            break;
                        else
                            cells[row][i] |= NO_BOX;
                    }
                }

                if ((blocked[row - 1][col] & HORIZONTAL) != 0) {
                    // There's a wall and the preceding cells are blocked
                    // somehow
                    for (int i = row - 1; i > 0; i--) {
                        if (is(cells[i][col], WALL))
                            break;
                        else
                            cells[i][col] |= NO_BOX;
                    }
                }
            }
        }
    }

    static public void main(String args[])
    {
        Board b = new Board(4, 5, 1, 1);
        b.cells[0][0] = WALL;
        b.cells[0][1] = WALL;
        b.cells[0][2] = WALL;
        b.cells[0][3] = WALL;

        b.cells[1][0] = WALL;
        b.cells[1][1] = 0;
        b.cells[1][2] = 0;
        b.cells[1][3] = WALL;

        b.cells[2][0] = WALL;
        b.cells[2][1] = 0;
        b.cells[2][2] = BOX;
        b.cells[2][3] = WALL;

        b.cells[3][0] = WALL;
        b.cells[3][1] = 0;
        b.cells[3][2] = GOAL;
        b.cells[3][3] = WALL;

        b.cells[4][0] = WALL;
        b.cells[4][1] = WALL;
        b.cells[4][2] = WALL;
        b.cells[4][3] = WALL;

        b.refresh();
        System.out.println(b.toString());
    }
}
