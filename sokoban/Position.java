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

    /**
     * Constructs the position that is placed one step in the given direction
     * (move) from the given position.
     * 
     * @param pos The position to start from.
     * @param move Should be a 2-length array with move[0]==row and
     *            move[1]==column.
     */
    public Position(Position pos, int[] move)
    {
        this.row = pos.row + move[0];
        this.column = pos.column + move[1];
    }

    @Override
    public boolean equals(Object obj)
    {
        Position pos;
        if (obj instanceof Position) {
            pos = (Position) obj;
        }
        else {
            return false;
        }

        return this.row == pos.row && this.column == pos.column;
    }

    /**
     * Calculate the hashCode by bitshifting the row by 16 bits and oring in the
     * column.
     * 
     * @note Collisions ARE POSSIBLE if row and/or column are larger than 16
     *       bits (>65536)
     */
    public int hashCode()
    {
        return (row << 16) | column;
    }

    @Override
    public String toString()
    {
        return "[" + row + "][" + column + "]";
    }

}
