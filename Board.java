/**
 *
 */
public class Board
{
    byte cells[][];
    int  playerX;
    int  playerY;
    int  remainingBoxes;

    /**
     * Initialize a new board
     * 
     * @param x The width of the board
     * @param y The height of the board
     * @param playerX The X position of the player
     * @param playerY The Y position of the player
     */
    Board(int x, int y, int playerX, int playerY)
    {
        cells = new byte[x][y];
        this.playerX = playerX;
        this.playerY = playerY;
    }
}
