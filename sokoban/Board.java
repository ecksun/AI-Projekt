package sokoban;

import sokoban.solvers.IDS;
import sokoban.solvers.Solver;

/**
 *
 */
public class Board implements Cloneable
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
     * Boxes will get stuck in this square
     */
    public final static byte BOX_TRAP = 0x08;
    /**
     * The player has already passed this cell the since last move
     */
    public final static byte VISITED = 0x10;
    
    // Bitmasks
    /**
     * A bitmask that says that a cell can't be walked into
     */
    public final static byte REJECT_WALK = WALL | VISITED;
    /**
     * A bitmask that says that a block can't move into this cell
     */
    public final static byte REJECT_BOX = WALL | BOX | BOX_TRAP;

    /**
     * All four allowed moves:  { row, column }
     */
    private static final int moves[][] = {
            { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }
    };
    
    public enum Direction {
        UP, DOWN, LEFT, RIGHT,
    }
    
    public final int width;
    public final int height;
    
    /**
     * A bitmask for the input cell values. 
     */
    public final static byte INPUT_CELL_MASK = WALL | GOAL | BOX;

    /**
     * The actual board
     */
    public byte cells[][];
    public int playerCol;
    public int playerRow;
    private int remainingBoxes;

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
     * Gets the number of goal cells that don't have box yet.
     */
    public int getRemainingBoxes() {
        return remainingBoxes;
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
        switch (value & Board.INPUT_CELL_MASK) {
            case Board.WALL:
                return '#';
            case Board.GOAL:
                return '.';
            case Board.BOX:
                return '$';
            case Board.GOAL | Board.BOX:
                return '*';
            default:
                return ((value & Board.BOX_TRAP) == 0 ? ' ' : '-');
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
            return is(cells[row][col], Board.GOAL) ? '+' : '@';
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
                    for (int i = col-1; i > 0; i--) {
                        if (is(cells[row][i], WALL)) break;
                        else cells[row][i] |= BOX_TRAP;
                    }
                }

                if ((blocked[row - 1][col] & HORIZONTAL) != 0) {
                    // There's a wall and the preceding cells are blocked
                    // somehow
                    for (int i = row-1; i > 0; i--) {
                        if (is(cells[i][col], WALL)) break;
                        else cells[i][col] |= BOX_TRAP;
                    }
                }
            }
        }
    }
    
    /**
     * Returns a clone of this board.
     */
    public Object clone() {
       
        try {
            Board copy = (Board) super.clone();

            // Deep copy cells
            copy.cells = new byte[height][width];
            for (int row = 0; row < height; ++row) {
                for (int col = 0; col < width; ++col) {
                    copy.cells[row][col] = this.cells[row][col];
                }
            }
            
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new Error("This should not occur since we implement Cloneable");
        }
    }
    
    /**
     * Returns true if the player can move in the given direction
     */
    public boolean canMove(Direction dir) {
        int move[] = moves[dir.ordinal()];
        
        // The cell that the player moves to
        int row = playerRow + move[0];
        int col = playerCol + move[1];
        
        // The cell that the box (if any) moves to
        int row2 = playerRow + 2*move[0];
        int col2 = playerCol + 2*move[1];
        
        //System.out.println("("+playerRow+","+playerCol+") --> ("+row+","+col+"):  "+cells[row][col]);
        
        // Reject move if the player can't move there
        if (is(cells[row][col], REJECT_WALK)) return false;
        
        // Reject move if there's a box and it can't move
        // in the desired direction
        if (is(cells[row][col], BOX) &&
                is(cells[row2][col2], REJECT_BOX)) return false;
        
        // The move is possible 
        return true;
    }
    
    public void move(Direction dir) {
        int move[] = moves[dir.ordinal()];
        
        // The cell that the player moves to
        int row = playerRow + move[0];
        int col = playerCol + move[1];
        
        // The cell that the box (if any) moves to
        int row2 = playerRow + 2*move[0];
        int col2 = playerCol + 2*move[1];
        
        // Mark as visited
        cells[playerRow][playerCol] |= VISITED;
        
        // Move player
        playerRow = row;
        playerCol = col;
        
        if (is(cells[row][col], BOX)) {
            // Move box
            cells[row][col] &= ~BOX;
            cells[row2][col2] |= BOX;
            
            // Keep track of remaining boxes
            remainingBoxes +=
                (is(cells[row][col], GOAL) ? +1 : 0) +
                (is(cells[row2][col2], GOAL) ? -1 : 0);
            //System.out.println("remaining boxes: "+remainingBoxes);
            
            // Clear "visited" marks
            for (int r = 0; r < height; r++) {
                for (int c = 0; c < width; c++) {
                    cells[r][c] &= ~VISITED;
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
        
        System.out.println("----------- Clone test --------------");
        Board b2 = (Board) b.clone();
        b.cells[0][0] = GOAL;
        System.out.println(b.toString());
        System.out.println(b2.toString());
        
        Solver solver = new IDS(b);
        System.out.println(solver.solve());
    }
}
