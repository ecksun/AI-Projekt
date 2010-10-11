package sokoban;

/**
 * Position represents a (row, column) index in a matrix.
 */
public class Position
{
    /**
     * The (0-indexed) column for this position.
     */
    public int column;
    
    /**
     * The (0-indexed) row index for this position.
     */
    public int row;

    /**
     * Constructs a new position.
     *
     * @param row The row index.
     * @param column The column index.
     */
    public Position(int row, int column)
    {
        this.row = row;
        this.column = column;
    }
}
