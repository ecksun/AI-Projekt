package sokoban;

/**
 * PlayerPosDir represents a player position together with its relative position
 * to a box.
 */
public class PlayerPosDir extends Position
{
    /**
     * The column index of the box relative to the player.
     */
    public final int boxColumn;

    /**
     * The row index of the box relative to the player.
     */
    public final int boxRow;

    /**
     * Creates a new player and (relative) box position.
     * 
     * @param row The row index of the player.
     * @param column The column index of the player.
     * @param boxRow The box row index relative to the player.
     * @param boxColumn The box column index relative to the player.
     */
    public PlayerPosDir(int row, int column, int boxRow, int boxColumn)
    {
        super(row, column);
        
        this.boxRow = boxRow;
        this.boxColumn = boxColumn;
    }
}