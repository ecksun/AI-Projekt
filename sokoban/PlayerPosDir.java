package sokoban;

/**
 * Small class containing the player position and its relative box position
 */
public class PlayerPosDir extends Position
{
    /**
     * The relative x-position of the box 
     */
    public final int bx;
    /**
     * The relative y-position of the box
     */
    public final int by; // relative box position

    /**
     * Create a new player position
     * 
     * @param x TODO
     * @param y TODO
     * @param bx TODO
     * @param by TODO
     */
    public PlayerPosDir(int x, int y, int bx, int by)
    {
        super(x, y);
        this.bx = bx;
        this.by = by;
    }
}