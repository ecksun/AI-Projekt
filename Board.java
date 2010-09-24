/**
 *
 */
public class Board
{
    // Input values
    final byte WALL = 0x01;
    final byte BOX =  0x02;
    final byte GOAL = 0x04;
    // Generated values
    final byte NO_BOX = 0x08; 
    // Bitmasks
    final byte OCCUPIED = 0x03;
    
    final int width;
    final int height;
    
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
        this.height = cells.length;
        this.width = cells[0].length;
        
        this.playerX = playerX;
        this.playerY = playerY;
    }
    
    /**
     * Updates some variables after a board has been loaded.
     */
    public void refresh() {
        countBoxes();
    }
    
    /**
     * Counts the boxes that are not in a goal, and updates remainingBoxes. 
     */
    private void countBoxes()
    {
        int remaining = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if ((cells[y][x] & (BOX | GOAL)) == BOX) remaining++;
            }
        }
        remainingBoxes = remaining;
    }
}
