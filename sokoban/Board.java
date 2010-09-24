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
    public final static byte WALL = 0x01;

    /**
     * Value for a BOX on the board
     */
    public final static byte BOX = 0x02;
    /**
     * Value for a GOAL position on the board
     */
    public final static byte GOAL = 0x04;

    // Generated values
    /**
     * Boxes will get stuck in this square
     */
    public final static byte BOX_TRAP = 0x08;
    /**
     * The player has already passed this cell the since last move
     */
    public final static byte STEPPED = 0x10;
    
    // Bitmasks
    /**
     * A bitmask that says that a cell can't be walked into
     */
    public final static byte REJECT_WALK = WALL | BOX | STEPPED;
    /**
     * A bitmask that says that a block can't move into this cell
     */
    public final static byte REJECT_BOX = WALL | BOX | BOX_TRAP;

    final int width;
    final int height;

    /**
     * The actuall board
     */
    public byte cells[][];
    int playerX;
    int playerY;
    int remainingBoxes;

    /**
     * Initialize a new board
     * 
     * @param width The width of the board
     * @param height The height of the board
     * @param playerX The X position of the player
     * @param playerY The Y position of the player
     */
    public Board(int width, int height, int playerX, int playerY)
    {
        cells = new byte[height][width];
        this.width = width;
        this.height = height;
        this.playerX = playerX;
        this.playerY = playerY;
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
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if ((cells[y][x] & (BOX | GOAL)) == BOX) {
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
                tmp += cells[i][j];
            }
            tmp += "\n";
        }
        return tmp;
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
                if (is(cells[y][x], WALL)) blocked[y][x] = ALL;
            }
        }

        // Find "non box" squares
        for (int y = 1; y < height-1; y++) {
            for (int x = 1; x < width; x++) {
                // Break the "blocked lines" if there's a goal
                if (is(cells[y][x], GOAL)) {
                    continue;
                }
                
                final boolean rightIsWall =
                    (x < width-1 ? is(cells[y][x+1], WALL) : true);
                final byte neighborWalls = (byte) (
                        (is(cells[y-1][x], WALL) ? TOP : 0) |
                        (is(cells[y+1][x], WALL) ? BOTTOM : 0) |
                        (is(cells[y][x-1], WALL) ? LEFT : 0) |
                        (rightIsWall ? RIGHT : 0));

                // How the current cell can be blocked at most,
                // taking cells to the left and above into account.
                final byte verticalBlocked = (byte) (neighborWalls
                        & blocked[y][x-1] & VERTICAL);
                final byte horizontalBlocked = (byte) (neighborWalls
                        & blocked[y-1][x] & HORIZONTAL);
                
                final boolean isWall = is(cells[y][x], WALL); 
                
                if (!isWall) {
                    // Use common vertical blocking status
                    // with the block to the left
                    blocked[y][x] |= verticalBlocked | horizontalBlocked;
                }
                
                if (isWall && (blocked[y][x-1] & VERTICAL) != 0) {
                    // There's a wall and the preceding cells are blocked
                    // somehow
                    for (int i = x-1; i > 0; i--) {
                        if (is(cells[y][i], WALL)) break;
                        else cells[y][i] |= REJECT_BOX;
                    }
                }

                if ((blocked[y-1][x] & HORIZONTAL) != 0) {
                    // There's a wall and the preceding cells are blocked
                    // somehow
                    for (int i = y-1; i > 0; i--) {
                        if (is(cells[i][x], WALL)) break;
                        else cells[i][x] |= REJECT_BOX;
                    }
                }
            }
        }
    }
    
    static public void main(String args[]) {
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
